package org.bx.idgenerator.manager.service.impl;

import io.etcd.jetcd.Client;
import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.bean.*;
import org.bx.idgenerator.manager.service.IEtcdNodeOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Slf4j
@Service
public class EtcdNodeOperateServiceImpl implements IEtcdNodeOperateService {
    @Autowired
    private Client client;

    @Override
    public ResultWrapper<String> add(EtcdKeyInfoBean dto) {
//        log.info("EtcdServiceImpl.add 入参:{}", dto);
//        if (dto.getGroupId() == null || StringUtils.isBlank(dto.getBizTag())) {
//            throw new BizException("新增节点的时候groupId和bizTag不能为null");
//        }
//        KV kv = client.getKVClient();
//        String keyS = Constants.getKeyByGroupIdAndTag(dto);
//        ByteSequence key = ByteSequence.from(Constants.getETCDKeyByGroupId(keyS), Charset.forName("utf-8"));
//        String voS = JSONUtil.toJsonStr(dto);
//        ByteSequence value = ByteSequence.from(voS, Charset.forName("utf-8"));
//        AbstractLock lock = new EtcdDistributeLock(
//                client,
//                Constants.getLockKeyByKey(keyS),
//                20, TimeUnit.SECONDS);
//        try {
//            lock.lock();
//            //查询key是否存在
//            GetResponse getResponse = kv.get(key).get();
//            long count = getResponse.getCount();
//            if (count != 0) {
//                throw new BizException(keyS + "已经存在");
//            }
//            //设置状态  幂等
//            dto.setStatus(true);
//            dto.setMaxId(0);
//            dto.setUpdateTime(System.currentTimeMillis());
//            log.info("EtcdServiceImpl#add 更新实体:{}", dto);
//            kv.put(key, value, PutOption.newBuilder().withPrevKV().build()).get();
//            return ResultWrapper.getSuccessResultWrapper("新增成功");
//        } catch (Exception e) {
//            log.error("新增异常:{}", e);
//            throw new BizException("新增节点出错", e);
//        } finally {
//            lock.unlock();
//        }
        return null;
    }


    @Override
    public ResultWrapper<String> update(EtcdKeyInfoBean dto) {
//        log.info("EtcdServiceImpl.update 入参:{}", dto);
//        if (dto.getGroupId() == null || StringUtils.isBlank(dto.getBizTag())) {
//            throw new BizException("更新节点的时候groupId和bizTag不能为null");
//        }
//        KV kv = client.getKVClient();
//
//        String description = dto.getDescription();
//        String keyS = Constants.getKeyByGroupIdAndTag(dto);
//        ByteSequence key = ByteSequence.from(Constants.getETCDKeyByGroupId(keyS), Charset.forName("utf-8"));
//        AbstractLock lock = new EtcdDistributeLock(
//                client,
//                Constants.getLockKeyByKey(keyS),
//                20, TimeUnit.SECONDS);
//        try {
//            lock.lock();
//            CompletableFuture<GetResponse> getResponseCompletableFuture = kv.get(key);
//            GetResponse getResponse = getResponseCompletableFuture.get();
//            long count = getResponse.getCount();
//            if (count == 0) {
//                throw new EtcdBizRuntimeException("更新异常,此键{" + keyS + "}不存在");
//            }
//            KeyValue keyValue = getResponse.getKvs().get(0);
//            JSONObject jsonObject = JSONUtil.parseObj(keyValue.getValue().toString(Constants.CHARSET_UTF_8));
//            KeyInfoBean remoteObj = jsonObject.toBean(KeyInfoBean.class);
//            remoteObj.setDescription(description);
//            String updateObj = JSONUtil.toJsonStr(remoteObj);
//            ByteSequence updateValue = ByteSequence.from(updateObj, Charset.forName("utf-8"));
//            log.info("EtcdServiceImpl.update 更新实体:{}", remoteObj);
//
//            kv.put(key, updateValue).get();
//            return BaseReturnCode.SUCCESS.toData("更新成功");
//        } catch (Exception e) {
//            log.error("更新异常:{}", e);
//            throw new EtcdBizRuntimeException(e);
//        } finally {
//            lock.unlock();
//        }
        return null;
    }

    @Override
    public ResultWrapper<String> delete(EtcdKeyInfoBean dto) {
//        log.info("EtcdServiceImpl.delete 入参:{}", dto);
//        if (dto.getGroupId() == null || StringUtils.isBlank(dto.getBizTag())) {
//            throw new BizException("删除节点的时候groupId和bizTag不能为null");
//        }
//        KV kv = client.getKVClient();
//        String keyS = Constants.getKeyByGroupIdAndTag(dto);
//        ByteSequence key = ByteSequence.from(Constants.getETCDKeyByGroupId(keyS), Charset.forName("utf-8"));
//        AbstractLock lock = new EtcdDistributeLock(client, Constants.getLockKeyByKey(keyS), 20, TimeUnit.SECONDS);
//        try {
//            lock.lock();
//            GetResponse getResponse = kv.get(key).get();
//            long count = getResponse.getCount();
//            if (count == 0) {
//                throw new EtcdBizRuntimeException("删除异常,此键{" + keyS + "}不存在");
//            }
//            KeyValue keyValue = getResponse.getKvs().get(0);
//            JSONObject jsonObject = JSONUtil.parseObj(keyValue.getValue().toString(Constants.CHARSET_UTF_8));
//            KeyInfoBean remoteObj = jsonObject.toBean(KeyInfoBean.class);
//            remoteObj.setStatus(false);
//            remoteObj.setUpdateTime(System.currentTimeMillis());
//
//            String updateObj = JSONUtil.toJsonStr(remoteObj);
//            ByteSequence updateValue = ByteSequence.from(updateObj, Charset.forName("utf-8"));
//            log.info("禁用节点:{}", remoteObj);
//            kv.put(key, updateValue).get();
//            return BaseReturnCode.SUCCESS.toData("删除成功");
//        } catch (Exception e) {
//            log.error("删除异常:{}", e);
//            throw new EtcdBizRuntimeException(e);
//        } finally {
//            lock.unlock();
//        }
        return null;
    }

    @Override
    public ResultWrapper<List<KeyInfoBean>> select(EtcdKeyInfoBean dto) {
//        log.info("EtcdServiceImpl.select 入参:{}", dto);
//        if (dto.getGroupId() == null) {
//            throw new BizException("查询节点的时候groupId不能为null");
//        }
//        KV kv = client.getKVClient();
//        String keyS = Constants.getKeyByGroupId(dto);
//        ByteSequence key = ByteSequence.from(Constants.getETCDKeyByGroupId(keyS), Charset.forName("utf-8"));
//        List<KeyInfoBean> result;
//        try {
//            GetResponse getResponse = kv.get(key, GetOption.newBuilder().withPrefix(key).build()).get();
//            List<KeyValue> kvs = getResponse.getKvs();
//
//            result = kvs.stream()
//                    .map(kvObj -> {
//                        JSONObject jsonObject = JSONUtil.parseObj(kvObj.getValue().toString(Constants.CHARSET_UTF_8));
//                        KeyInfoBean remoteObj = jsonObject.toBean(KeyInfoBean.class);
//                        return remoteObj;
//                    })
//                    .filter(KeyInfoBean::getStatus).collect(Collectors.toList());
//            log.info("EtcdServiceImpl.select 查询的结果:{}", result);
//
//            return BaseReturnCode.SUCCESS.toData(result);
//        } catch (Exception e) {
//            log.error("出现异常：{}", e);
//            throw new EtcdBizRuntimeException(e);
//        }
        return null;
    }

    @Override
    public ResultWrapper<List<SnowFlakeEndPointDTO>> selectSnowFlakeEndPoint(NodeTypeEnum nodeTypeEnum) {
//        String rootPath = "";
//        if (nodeTypeEnum == NodeTypeEnum.FOREVER) {
//            rootPath = Constants.PATH_FOREVER;
//        }
//        KV kv = client.getKVClient();
//        ByteSequence key = ByteSequence.from(rootPath, Constants.CHARSET_UTF_8);
//        List<SnowFlakeEndPointDTO> result = new ArrayList<>();
//        try {
//            GetResponse getResponse = kv.get(key, GetOption.newBuilder().withPrefix(key).build()).get();
//            List<KeyValue> kvs = getResponse.getKvs();
//            Gson gson = new Gson();
//            for (KeyValue keyValue : kvs) {
//                if (keyValue.getKey().toString(Constants.CHARSET_UTF_8).equals(rootPath)) {
//                    continue;
//                }
//                SnowFlakeEndPointDTO endPoint = gson.fromJson(keyValue.getValue().toString(Constants.CHARSET_UTF_8), SnowFlakeEndPointDTO.class);
//                result.add(endPoint);
//            }
//            log.info("查询SnowFlake持久节点信息结果:{}", result);
//            return BaseReturnCode.SUCCESS.toData(result);
//        } catch (Exception e) {
//            log.error("出现异常：{}", e);
//            throw new EtcdBizRuntimeException(e);
//        }
        return null;
    }


}
