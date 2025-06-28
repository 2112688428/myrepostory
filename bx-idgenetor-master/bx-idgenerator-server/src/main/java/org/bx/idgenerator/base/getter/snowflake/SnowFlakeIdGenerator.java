package org.bx.idgenerator.base.getter.snowflake;

import com.google.common.base.Preconditions;
import io.etcd.jetcd.Client;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bx.idgenerator.Constants;
import org.bx.idgenerator.base.getter.snowflake.bean.NotifyContext;
import org.bx.idgenerator.base.getter.snowflake.notify.INotifyService;
import org.bx.idgenerator.core.IDGenerator;
import org.bx.idgenerator.exception.BizException;
import org.bx.idgenerator.lifecycle.AbstractGeneratorLifecycle;
import org.bx.idgenerator.properties.SnowFlakeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

/**
 * 基于雪花算法的ID生成器
 * 64bit固定长度，具体分段如下:
 * +--------+-----------------------------------------------+------------+---------------+
 * | 1bit   |               41bit                           |  10bit     |     12bit     |
 * +--------+-----------------------------------------------+------------+---------------+
 * |   0    |00000000 00000000 00000000 00000000 00000000 0 |00000 00000 |00000 00000 00 |
 * +--------+-----------------------------------------------+------------+---------------+
 * | no used|              TimeStamp                        | WorkerId   |  SequenceId   |
 * +------+---+---------------------------------------------+------------+---------------+
 *
 * @author Ming.Li
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月06日 10时52分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Data
@Slf4j
@Component
public class SnowFlakeIdGenerator extends AbstractGeneratorLifecycle implements IDGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnowFlakeIdGenerator.class);

    @Autowired
    private SnowFlakeProperties snowFlakeProperties;
    @Autowired
    private Client client;

    /**
     * 起始时间戳
     */
    private long twepoch;
    /**
     * 机器节点占10位
     */
    private final long workerIdBits = 10L;
    /**
     * 最大能够分配的workerid =1023
     */
    private final long maxWorkerId = ~(-1L << workerIdBits);
    /**
     * 12 bit 序列号
     */
    private final long sequenceBits = 12L;
    /**
     * 机器节点id 偏移
     */
    private final long workerIdShift = sequenceBits;
    /**
     * 时间戳的偏移
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits;
    /**
     * 序列号掩码
     */
    private final long sequenceMask = ~(-1L << sequenceBits);
    /**
     * 机器节点ID
     */
    private long workerId;
    /**
     * 并发控制，毫秒内序列(0~4095)
     */
    private volatile long sequence = 0L;
    /**
     * 上一次生成ID的时间戳
     */
    private long lastTimeStamp = -1L;
    /**
     * sequence随机种子
     */
    private static final Random RANDOM = new Random();

    @Autowired
    private INotifyService notifyService;


    @PostConstruct
    private void initEtcdHolder() {
        // 起始时间戳,默认值 2008-08-08 00:00:00
        this.twepoch = Constants.SNOWFLAKE_DEFAULT_TWEPOCH;
        String workerIp = snowFlakeProperties.getWorkerIp();
        String netInterfaceName = snowFlakeProperties.getNetInterfaceName();
        Preconditions.checkArgument(StringUtils.isNotBlank(netInterfaceName), "init netInterfaceName must not null");
        // 机器的IP地址，可不传，会自动获取当前服务的机器IP地址
        if (StringUtils.isBlank(workerIp)) {
            workerIp = getIp(netInterfaceName);
        }
        String workerPort = snowFlakeProperties.getWorkerPort();
        Preconditions.checkArgument(StringUtils.isNotBlank(workerIp), "init worker ip must not null");
        Preconditions.checkArgument(StringUtils.isNotBlank(workerPort), "init worker port must not null");
        Preconditions.checkArgument(client != null, "init etcd client must not null");
        // 初始化 etcd客户端,注册 worker节点
        SnowflakeETCDHolder holder = new SnowflakeETCDHolder(client, workerIp, workerPort, this, snowFlakeProperties);
        holder.init();
        workerId = holder.getWorkerId();
        LOGGER.info("init success,use etcd workerId is {}", workerId);
        Preconditions.checkArgument(workerId >= 0 && workerId <= maxWorkerId, "workerId must gte 0 and lte 1023");
    }

    @Override
    public synchronized long genId(String bizTag) {
        // CAS 代替 synchronized
        // 如果当前时间戳 小于 上一次更新时间，说明发生了 时钟回拨
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimeStamp) {
            long offset = timestamp - lastTimeStamp;
            // 默认 50ms
            // SNOWFLAKE_BLOCKBACKT_HRESHOLD_VALUE
            if (offset <= snowFlakeProperties.getBlockBackThreshold()) {
                try {
                    wait(offset << 1);
                    timestamp = System.currentTimeMillis();
                    if (timestamp < lastTimeStamp) {
                        throw new BizException("time clock back too long error");
                    }
                } catch (InterruptedException e) {
                    throw new BizException("wait interrupted");
                }
            } else {
                String msg = "服务器节点发生时钟回拨，WorkerId: " + workerId;
                try {
                    notifyService.notify(NotifyContext.builder().data(this).msg(msg).build());
                } catch (Exception e) {
                    throw new BizException(msg);
                }

            }
        }
        // 当前毫秒内进行序列号的递增
        if (timestamp == lastTimeStamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 说明1毫秒内的序列号已经使用完
            if (sequence == 0) {
                // 取随机数，降低并发冲突
                sequence = RANDOM.nextInt(100);
                // 阻塞到下一个毫秒,获得新的时间戳
                tilNextMillis(lastTimeStamp);
            }
        } else {
            // 时间戳改变，毫秒内序列重置
            sequence = RANDOM.nextInt(100);
        }
        // 上次生成ID的时间截
        lastTimeStamp = timestamp;
        LOGGER.info("generate id success,timestamp: {}, workerId: {}, sequence: {}", timestamp, workerId, sequence);
        return ((timestamp - twepoch) << timestampLeftShift)
                | (workerId << workerIdShift)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimeStamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimeStamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimeStamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    private String getIp(String interfaceName) {
        String ip;
        try {
            List<String> ipList = getHostAddress(interfaceName);
            log.info("获取的网卡IP地址,网卡名:{},IPS:{}",interfaceName,ipList);
            ip = (!ipList.isEmpty()) ? ipList.get(0) : "";
        } catch (Exception ex) {
            ip = "";
            log.warn("获取机器网卡ID失败", ex);
        }
        return ip;
    }


    /**
     * 获取已激活网卡的IP地址
     *
     * @param interfaceName 可指定网卡名称,null则获取全部
     * @return List<String>
     */
    private List<String> getHostAddress(String interfaceName) throws SocketException {
        List<String> ipList = new ArrayList<>(5);
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            Enumeration<InetAddress> allAddress = networkInterface.getInetAddresses();
            String networkInterfaceName = networkInterface.getName();
            while (allAddress.hasMoreElements()) {
                InetAddress address = allAddress.nextElement();
                log.info("网卡名称: {},网卡IP:{}", networkInterfaceName, address.getHostAddress());
                if (address.isLoopbackAddress()) {
                    // skip the loopback addr
                    continue;
                }

                if (address instanceof Inet6Address) {
                    // skip the IPv6 addr
                    continue;
                }
                String hostAddress = address.getHostAddress();
                if(interfaceName.equalsIgnoreCase(networkInterfaceName)){
                    ipList.add(hostAddress);
                }
            }

        }
        return ipList;
    }

    @Override
    public String name() {
        return "SnowFlakeIdGenerator";
    }
}
