package org.bx.idgenerator.base.getter.snowflake;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.PutOption;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bx.idgenerator.Constants;
import org.bx.idgenerator.base.getter.snowflake.lock.AbstractLock;
import org.bx.idgenerator.base.getter.snowflake.lock.EtcdDistributeLock;
import org.bx.idgenerator.base.segment.ILeafInfoService;
import org.bx.idgenerator.base.segment.entity.LeafInfo;
import org.bx.idgenerator.bean.KeyInfoBean;
import org.bx.idgenerator.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 15:55
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Slf4j
@Service("etcdLeafInfoService")
public class ETCDLeafInfoService implements ILeafInfoService {

    @Autowired
    private Client client;


    @Override
    public LeafInfo getLeafInfo(String tag) {
        if (StringUtils.isBlank(tag)) {
            throw new BizException("tag不能为空");
        }
        log.info("getLeafInfo 入参 tag:{}", tag);
        LeafInfo leafInfo = new LeafInfo();

        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.from(Constants.getETCDKeyBySystemId(tag), Charset.forName("utf-8"));
        AbstractLock lock = new EtcdDistributeLock(client, Constants.getLockKeyByKey(tag), 20, TimeUnit.SECONDS);
        try {
            lock.lock();

            GetResponse getResponse = kvClient.get(key).get();
            long count = getResponse.getCount();
            if (count == 0) {
                throw new BizException("不存在此tag:{" + tag + "}的数据");
            }
            List<KeyValue> kvs = getResponse.getKvs();
            KeyValue keyValue = kvs.get(0);
            JSONObject jsonObject = JSONUtil.parseObj(keyValue.getValue().toString(Constants.CHARSET_UTF_8));
            KeyInfoBean remoteObj = jsonObject.toBean(KeyInfoBean.class);
            log.info("getLeafInfo 获得节点对象:{}", remoteObj);
            if (!remoteObj.getStatus()) {
                throw new BizException("此tag:{" + tag + "}的数据不可用");
            }
            Integer step = remoteObj.getStep();
            String bizTag = remoteObj.getBizTag();
            Integer maxId = remoteObj.getMaxId();

            Integer newMaxId = step + maxId;
            //更新最大id
            remoteObj.setMaxId(newMaxId);
            //更新时间
            remoteObj.setUpdateTime(System.currentTimeMillis());
            String remoteDtoS = JSONUtil.toJsonStr(remoteObj);
            ByteSequence value = ByteSequence.from(remoteDtoS, Charset.forName("utf-8"));
            log.info("getLeafInfo 更新实体:{}", remoteObj);
            kvClient.put(key, value, PutOption.newBuilder().withPrevKV().build()).get();

            leafInfo.setTag(bizTag);
            leafInfo.setCurId(maxId);
            leafInfo.setMaxId(newMaxId);
            leafInfo.setDescription(remoteObj.getDescription());
            leafInfo.setUpdateTime(remoteObj.getUpdateTime());
        } catch (Exception e) {
            log.error("填充异常：{}", e);
            throw new BizException("填充异常:{" + tag + "}");
        } finally {
            lock.unlock();
        }
        return leafInfo;
    }
}
