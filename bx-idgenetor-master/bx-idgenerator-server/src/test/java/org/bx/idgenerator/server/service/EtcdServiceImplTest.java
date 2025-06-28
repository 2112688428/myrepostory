package org.bx.idgenerator.server.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.etcd.jetcd.ByteSequence;
import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.bean.KeyInfoBean;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 23:16
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Slf4j
public class EtcdServiceImplTest {

    private ExecutorService executor = Executors.newFixedThreadPool(1);
    public static final String key1 = "123/oms";
    public static final String key2 = "123/oms";


    @Test
    public void test_thread() throws InterruptedException {
        for(int i=0;i<4;i++){
            executor.submit(() -> {
                log.info("thread:{}, value: {}",Thread.currentThread().getId());
                throw new RuntimeException("异常");
            });
        }

        executor.awaitTermination(1000, TimeUnit.HOURS);

    }






    @Test
    public void test_json() {
        KeyInfoBean dto = new KeyInfoBean();
        dto.setBizTag("1234");
        dto.setStatus(true);
        dto.setMaxId(1222);
        dto.setStep(12);
        String voS = JSONUtil.toJsonStr(dto);
        ByteSequence value = ByteSequence.from(voS, Charset.forName("utf-8"));

        String valueS = value.toString(Charset.forName("utf-8"));

        JSONObject jsonObject = JSONUtil.parseObj(valueS);
        KeyInfoBean dto1 = jsonObject.toBean(KeyInfoBean.class);
        System.out.println(dto1);

    }

    //@Test
    public void testMultiThread1() throws Exception {
        /*ETCDLeafInfoService leafInfoService = new ETCDLeafInfoService(null);
        Field client = leafInfoService.getClass().getDeclaredField("client");
        client.setAccessible(true);
        client.set(leafInfoService, EtcdClientFactory.getClient());

        SegmentIDGenerator segmentIdGenerator = new SegmentIDGenerator(
                leafInfoService,
                new FillSegmentUpdater(),
                new DefaultFetchPolicy());


        ConcurrentHashSet<Long> set1 = new ConcurrentHashSet<>();
        ConcurrentHashSet<Long> set2 = new ConcurrentHashSet<>();
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                for (int j = 0; j < 100; j++) {
                    Long value1 = segmentIdGenerator.genId(key1);
                    Long value2 = segmentIdGenerator.genId(key2);
                    if (set1.contains(value1)) {
                        System.out.println("set1冲突了");
                        executor.shutdownNow();
                    }
                    if (set2.contains(value2)) {
                        System.out.println("set2冲突了");
                        executor.shutdownNow();
                    }
                    set1.add(value1);
                    set2.add(value2);
                    System.out.println(value1);
                    System.out.println(value2);
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);*/

    }


}