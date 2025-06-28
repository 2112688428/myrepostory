package org.bx.idgenerator.base.getter.snowflake;


import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.kv.PutResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bx.idgenerator.base.getter.snowflake.bean.EndPointReportData;
import org.bx.idgenerator.base.getter.snowflake.bean.NotifyContext;
import org.bx.idgenerator.exception.BizException;
import org.bx.idgenerator.exception.ClockBackRuntimeException;
import org.bx.idgenerator.lifecycle.AbstractGeneratorLifecycle;
import org.bx.idgenerator.properties.SnowFlakeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.bx.idgenerator.Constants.*;

/**
 * ETCD 节点持有类
 *
 * @author Ming.Li
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月06日 17时00分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public class SnowflakeETCDHolder extends AbstractGeneratorLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnowflakeETCDHolder.class);

    /**
     * 保存自身的key  rootpath/ip:port-000000001
     */
    private String etcdAddressNode = null;
    /**
     * 保存自身的key ip:port
     */
    private String listenAddress;
    private String rootNodeValue = "root";
    /**
     * 生成的机器节点id
     */
    private long workerId;
    /**
     * 本地持久化文件
     */
    private static String PROP_PATH;
    /**
     * 上一次更新时间
     */
    private long lastUpdateTime;
    /**
     * 机器IP
     */
    private String ip;
    /**
     * 机器端口
     */
    private String port;
    /**
     * ETCD 客户端
     */
    private Client client;

    private SnowFlakeIdGenerator snowFlakeIdGenerator;

    private SnowFlakeProperties snowFlakeProperties;

    public String getPropPath() {
        return System.getProperty("java.io.tmpdir") + snowFlakeProperties.getGeneratorName()
                + File.separator + "bxconf" + File.separator + "{port}" + File.separator + "workerId.properties";
    }


    /**
     * @param client ETCD 客户端
     * @param ip     ID服务器IP地址
     * @param port   ID服务器端口
     */
    public SnowflakeETCDHolder(Client client, String ip, String port, SnowFlakeIdGenerator generator, SnowFlakeProperties snowFlakeProperties) {
        this.ip = ip;
        this.port = port;
        this.listenAddress = ip + ":" + port;
        this.client = client;
        snowFlakeIdGenerator = generator;
        this.snowFlakeProperties = snowFlakeProperties;

    }

    @Override
    public void doInit() {
        KV kvClient = client.getKVClient();
        try {
            ByteSequence rootNode = ByteSequence.from(snowFlakeProperties.getPathForever(), CHARSET_UTF_8);
            // 带前缀匹配 /snowflake/bx/forever*
            GetResponse rootResponse = kvClient.get(rootNode, GetOption.newBuilder()
                    .withPrefix(rootNode)
                    .withKeysOnly(true)
                    .build()).get();
            // 不存在根节点,机器第一次启动,创建/snowflake/ip:port-000000000,并上传数据
            if (rootResponse == null || rootResponse.getCount() == 0) {
                String etcdNodeAddress = createNode(client);
                updateLocalWorkerId(workerId);
                etcdAddressNode = etcdNodeAddress;
                // 定时上报本机时间给forever节点
                scheduledReportEndPointsData(client, etcdAddressNode);
            } else {
                Map<String, Long> workerIdMap = new HashMap<>();
                Map<String, String> etcdKeyMap = new HashMap<>();
                // 存在根节点，检查是否存在属于自己的根节点
                List<KeyValue> keyValues = rootResponse.getKvs();
                for (KeyValue keyValue : keyValues) {
                    String key = keyValue.getKey().toString(CHARSET_UTF_8);
                    // 如果key为持久节根点 PATH_FOREVER
                    if (!snowFlakeProperties.getPathForever().equals(key)) {
                        String[] split = key.split("-");
                        workerIdMap.put(split[0], Long.parseLong(split[1]));
                        etcdKeyMap.put(split[0], key);
                    }
                }
                String key = snowFlakeProperties.getPathForever() + "/" + listenAddress;
                Long tempWokerId = workerIdMap.get(key);
                // 存在自己的节点
                if (tempWokerId != null) {
                    etcdAddressNode = etcdKeyMap.get(key);
                    workerId = tempWokerId;
                    // 检查当前时间是否 大于 节点最近的上报时间
                    if (!checkInitTimeStamp(kvClient, etcdAddressNode)) {
                        throw new ClockBackRuntimeException("check init timestamp error,forever node timestamp gt this node time");
                    }
                    String tempNodePath = etcdAddressNode.replace(snowFlakeProperties.getPathForever(), snowFlakeProperties.getPathTemp());
                    // 重新创建临时节点
                    createTempNode(tempNodePath, buildEndPointReportDataJson(), client);
                    // 开启定时上报节点
                    scheduledReportEndPointsData(client, etcdAddressNode);
                    updateLocalWorkerId(workerId);
                    LOGGER.info("there node has been found on  forever node, this endpoint ip-{} port-{} workerId-{} childnode and start success", ip, port, workerId);
                } else {
                    // 创建新节点
                    String newNode = createNode(client);
                    etcdAddressNode = newNode;
                    String[] nodeKey = newNode.split("-");
                    workerId = Integer.parseInt(nodeKey[1]);
                    // 开启定时上报节点
                    scheduledReportEndPointsData(client, etcdAddressNode);
                    updateLocalWorkerId(workerId);
                    LOGGER.info("node can not find node on forever node,that endpoint ip-{} port-{} workid-{},create own node on forever node and start success", ip, port, workerId);
                }
            }
            // 检查当前机器节点与其他服务节点的时间是否同步，存在时间偏差，校验每个服务节点的时间一致
            checkServersClockBack(kvClient);
        } catch (Exception e) {
            LOGGER.error("初始化ETCD节点服务出错", e);
            try {
                // 从本地机器文件系统加载workerid信息
                Properties properties = new Properties();
                String path = getPropPath().replace("{port}", port);
                properties.load(new FileInputStream(new File(path)));
                if (properties != null) {
                    String workerID = properties.getProperty(SNOWFLAKE_LOCAL_WORKERID);
                    if (StringUtils.isNotEmpty(workerID)) {
                        workerId = Integer.valueOf(workerID);
                    } else {
                        throw new BizException("读取当前服务器的workerId配置文件信息缺失");
                    }
                }

                LOGGER.warn("start node failed,use local node properties file workerId-{}", workerId);
            } catch (Exception e1) {
                throw new BizException("读取当前服务器的配置文件出错", e1);
            }
        }
    }


    /**
     * 获取 ETCD的所有临时节点(所有运行中的snowflake节点)的服务 IP：Port，
     * 然后通过RPC请求得到所有节点的系统时间，计算 sum(time)/nodeSize
     * 若 abs( 系统时间 -sum(time)/nodeSize ) < 阈值，认为当前系统时间准确，正常启动服务，
     * 检查ID服务器节点之间的时间差别，如果存在时钟不同步，进行抛出异常
     *
     * @param kvClient
     */
    private void checkServersClockBack(KV kvClient) throws Exception {
        ByteSequence rootNode = ByteSequence.from(snowFlakeProperties.getPathTemp(), CHARSET_UTF_8);
        GetResponse rootResponse = null;
        try {
            // 带前缀匹配 /snowflake/bx/temp*
            rootResponse = kvClient.get(rootNode, GetOption.newBuilder()
                    .withPrefix(rootNode)
                    .build()).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new BizException("检查在线服务器节点的时间同步，查询ETCD服务节点出错", e);
        }

        long count = rootResponse.getCount();
        if (count == 0) {
            return;
        }
        List<KeyValue> keyValues = rootResponse.getKvs();
        Gson gson = new Gson();

        List<String> urls = keyValues.stream().filter(kv -> {
            String key = kv.getKey().toString(CHARSET_UTF_8);
            // 排除临时节点根节点 PATH_TEMP、排除当前节点路径
            return !(snowFlakeProperties.getPathTemp().equals(key) || key.contains(ip + ":" + port));
        }).map(kv -> {
            String value = kv.getValue().toString(CHARSET_UTF_8);
            EndPointReportData endPointReportData = gson.fromJson(value, EndPointReportData.class);
            String url = "http://" + endPointReportData.getIp() + ":" + endPointReportData.getPort() + SERVER_TIME_API;
            return url;
        }).collect(Collectors.toList());

        LOGGER.info("在线节点信息: " + urls);

        if (urls == null || urls.size() == 0) {
            return;
        }

        // 服务器之间的时间误差,并行流处理
        double differTime = urls.parallelStream().map(url -> {
            // 系统当前时间
            long start = System.currentTimeMillis();
            String serverTime = HttpUtil.get(url);
            long endTime = System.currentTimeMillis();
            LOGGER.info("当前处理线程:{},请求的服务器URL:{},发起请求之前服务器系统时间:{},对方服务器的系统时间:{},请求完成后的系统时间:{}",
                    Thread.currentThread().getName(), url, start, serverTime, endTime);
            // 单程请求消耗时间
            long oneWayCost = (endTime - start) / 2;
            if (serverTime.startsWith("<Long>")) {
                serverTime = serverTime.substring(6);
                serverTime = serverTime.substring(0, serverTime.length() - 7);
            }
            return Long.parseLong(serverTime) - oneWayCost - start;
        }).collect(Collectors.toList()).stream().mapToLong(v -> v.longValue()).average().getAsDouble();

        // 时间差绝对值
        Double absSubTime = Math.abs(differTime);
        LOGGER.info("当前服务器系统时间与其他线上ID服务器的系统均值时间差: " + absSubTime + "ms");

        // 如果大于阈值
        if (absSubTime.longValue() > snowFlakeProperties.getBlockBackThreshold()) {
            snowFlakeIdGenerator.getNotifyService().notify(NotifyContext.builder().data(snowFlakeIdGenerator).msg("服务器节点之间存在时钟不同步问题，请检查重试,节点信息:" + gson.toJson(urls)).build());
        }
    }


    /**
     * 检查当前节点时间戳是否 大于 节点最后一次上报时间
     *
     * @param kvClient
     * @param etcdAddressNode
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private boolean checkInitTimeStamp(KV kvClient, String etcdAddressNode) throws ExecutionException, InterruptedException {
        GetResponse response = kvClient.get(ByteSequence.from(etcdAddressNode, CHARSET_UTF_8)).get();
        KeyValue keyValue = response.getKvs().get(0);
        ByteSequence value = keyValue.getValue();
        EndPointReportData endPointReportData = parseEndPointReportData(value.toString(CHARSET_UTF_8));
        // 当前节点时间 大于或等于 最后一次上报时间
        return System.currentTimeMillis() >= endPointReportData.getTimestamp();
    }

    /**
     * 在节点文件系统上缓存一个 workid值, etcd 失效,机器重启时保证能够正常启动
     *
     * @param workerId
     */
    private void updateLocalWorkerId(long workerId) {
        File configFile = new File(getPropPath().replace("{port}", port));
        if (configFile.exists()) {
            try {
                FileUtils.writeStringToFile(configFile, SNOWFLAKE_LOCAL_WORKERID + "=" + workerId, false);
                LOGGER.info("update local file success, cache  workerId is {}", workerId);
            } catch (IOException e) {
                LOGGER.error("update file cache workerId is {}", workerId);
            }
        } else {
            boolean mkdirs = configFile.getParentFile().mkdirs();
            if (mkdirs) {
                try {
                    if (configFile.createNewFile()) {
                        FileUtils.writeStringToFile(configFile, SNOWFLAKE_LOCAL_WORKERID + "=" + workerId, false);
                        LOGGER.info("create local file success,cache workerId is {}", workerId);
                    }
                } catch (IOException e) {
                    LOGGER.error("create file failed,path: {},workerid:{}", getPropPath(), workerId);
                }
                return;
            }
            LOGGER.warn("mkdirs failed,path: {},workerid:{}", getPropPath(), workerId);
        }
    }

    /**
     * 定时上报服务器节点的数据
     * 3秒上报一次
     *
     * @param etcdClient
     * @param etcdAddressNode
     */
    public void scheduledReportEndPointsData(Client etcdClient, String etcdAddressNode) {
        // 每 2s 上报数据
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, (r) -> {
            Thread thread = new Thread(r, "bx-schedule-upload-endpoints-data");
            thread.setDaemon(true);
            return thread;
        });
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                reportEndPointsData(etcdClient.getKVClient(), etcdAddressNode);
            }
        }, 1L, snowFlakeProperties.getSystemReportInterval(), TimeUnit.MILLISECONDS);
    }


    public long getWorkerId() {
        return workerId;
    }

    /**
     * 上报服务节点数据
     *
     * @param kvClient
     * @param etcdAddressNode
     */
    private void reportEndPointsData(KV kvClient, String etcdAddressNode) {
        // 当前时间 小于 上次更新上报时间
        if (System.currentTimeMillis() < lastUpdateTime) {
            return;
        }
        ByteSequence key = ByteSequence.from(etcdAddressNode, Charset.forName("utf-8"));
        ByteSequence value = ByteSequence.from(buildEndPointReportDataJson(), Charset.forName("utf-8"));
        kvClient.put(key, value);

    }

    /**
     * 构建需要上传的数据json串
     *
     * @return
     */
    private String buildEndPointReportDataJson() {
        EndPointReportData endpoint = new EndPointReportData(ip, port, System.currentTimeMillis(), workerId);
        Gson gson = new Gson();
        String json = gson.toJson(endpoint);
        return json;
    }


    /**
     * 解析上传数据
     *
     * @param json
     * @return
     */
    private EndPointReportData parseEndPointReportData(String json) {
        Gson gson = new Gson();
        EndPointReportData endpoint = gson.fromJson(json, EndPointReportData.class);
        return endpoint;
    }

    /**
     * 创建临时ETCD节点
     *
     * @param tempNodePath
     */
    @SneakyThrows
    private void createTempNode(String tempNodePath, String value, Client etcdClient) {
        KV kvClient = client.getKVClient();
        // 创建临时节点，续约，创建Lease客户端
        Lease leaseClient = etcdClient.getLeaseClient();
        // 创建一个租约
        Long leaseId = null;
        try {
            //  SNOWFLAKE_LEASE_TTL_VALUE
            long seconds = TimeUnit.MILLISECONDS.toSeconds(snowFlakeProperties.getLeaseTtl());
            leaseId = leaseClient.grant(seconds).get().getID();
        } catch (InterruptedException | ExecutionException e) {
            throw new BizException("create lease id failed", e);
        }
        // 启用线程去周期性续约，当 worker节点死掉，会自动断开连接
        new ScheduledThreadPoolExecutor(1, (run) -> {
            Thread thread = new Thread(run, "bx-schedule-keepAlive-node");
            thread.setDaemon(true);
            return thread;
            // SNOWFLAKE_KEEPALIVE_INTERVAL_VALUE
        }).scheduleAtFixedRate(new KeepAliveTask(leaseClient, leaseId), 1, snowFlakeProperties.getKeepAliveInterval(), TimeUnit.MILLISECONDS);
        ByteSequence uniqueTempKey = ByteSequence.from(tempNodePath, CHARSET_UTF_8);
        ByteSequence uniqueTempValue = ByteSequence.from(value, CHARSET_UTF_8);
        // 创建节点添加租约，服务器节点挂掉后，因没法续租，该节点路径会失效
        kvClient.put(uniqueTempKey, uniqueTempValue, PutOption.newBuilder().withLeaseId(leaseId).build()).get();
        LOGGER.info("create etcd temp node success, path is {} ", tempNodePath);
    }


    /**
     * 创建持久节点 ,且将节点数据放入value
     *
     * @param etcdClient
     * @return 持久顺序节点路径
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private String createNode(Client etcdClient) throws ExecutionException, InterruptedException {
        KV kvClient = etcdClient.getKVClient();
        ByteSequence key = ByteSequence.from(snowFlakeProperties.getPathForever(), CHARSET_UTF_8);
        ByteSequence value = ByteSequence.from(rootNodeValue, CHARSET_UTF_8);
        // 返回前一个KV值，初始值 version=0
        PutResponse putResponse = kvClient.put(key, value, PutOption.newBuilder().withPrevKV().build()).get();
        KeyValue prevKv = putResponse.getPrevKv();
        long version = prevKv.getVersion();
        this.workerId = version;
        // 创建持久节点
        String uniqueForeverPath = snowFlakeProperties.getPathForever() + "/" + listenAddress + "-" + version;
        ByteSequence uniqueForeverKey = ByteSequence.from(uniqueForeverPath, CHARSET_UTF_8);
        ByteSequence uniqueForeverValue = ByteSequence.from(buildEndPointReportDataJson(), CHARSET_UTF_8);
        kvClient.put(uniqueForeverKey, uniqueForeverValue).get();
        LOGGER.info("create etcd forever node success, path is {} ", uniqueForeverPath);
        // 临时节点路径
        String uniqueTempPath = snowFlakeProperties.getPathTemp() + "/" + listenAddress + "-" + version;
        createTempNode(uniqueTempPath, buildEndPointReportDataJson(), client);
        return uniqueForeverPath;
    }

    @Override
    public String name() {
        return "SnowflakeETCDHolder";
    }


    /**
     * 续租任务
     *
     * @author Ming.Li
     * @version V1.0
     * @contact zeroming@163.com
     * @date: 2020年07月07日 14时55分
     * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
     * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
     */
    private static class KeepAliveTask implements Runnable {

        private Lease leaseClient;
        private long leaseId;

        KeepAliveTask(Lease leaseClient, long leaseId) {
            this.leaseClient = leaseClient;
            this.leaseId = leaseId;
        }

        @Override
        public void run() {
            leaseClient.keepAliveOnce(leaseId);
        }
    }

}
