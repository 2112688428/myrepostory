package org.bx.test;

public class SegmentIDGeneratorTest {
//    private SegmentIDGenerator segmentIdGenerator;
//    private ExecutorService executor = Executors.newCachedThreadPool();
//    public static final String key1 = "test-1";
//    public static final String key2 = "test-2";
//
//    @Before
//    public void init() {
//        segmentIdGenerator = new SegmentIDGenerator(new TestLeafInfoService(), new SegmentUpdater(), new DefaultFetchPolicy());
//    }
//
//    @Test
//    public void testSingleThread() {
//        for (int i = 0; i < 200; i++) {
//            System.out.println(segmentIdGenerator.genId(key1));
//        }
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        for (int i = 0; i < 200; i++) {
//            System.out.println(segmentIdGenerator.genId(key2));
//        }
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        for (int i = 0; i < 200; i++) {
//            System.out.println(segmentIdGenerator.genId(key1));
//        }
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//        for (int i = 0; i < 200; i++) {
//            System.out.println(segmentIdGenerator.genId(key2));
//        }
//    }
//
//    @Test
//    public void testMultiThread() throws InterruptedException {
//
//        ConcurrentHashSet<Long> set1 = new ConcurrentHashSet<>();
//        ConcurrentHashSet<Long> set2 = new ConcurrentHashSet<>();
//        for (int i = 0; i < 10; i++) {
//            executor.execute(() -> {
//                for (int j = 0; j < 100; j++) {
//                    Long value1 = segmentIdGenerator.genId(key1);
//                    Long value2 = segmentIdGenerator.genId(key2);
//                    if(set1.contains(value1)){
//                        System.out.println("set1冲突了");
//                        executor.shutdownNow();
//                    }
//                    if(set2.contains(value2)){
//                        System.out.println("set2冲突了");
//                        executor.shutdownNow();
//                    }
//                    set1.add(value1);
//                    set2.add(value2);
//                    System.out.println(value1);
//                    System.out.println(value2);
//                }
//            });
//        }
//        executor.shutdown();
//        executor.awaitTermination(1, TimeUnit.DAYS);
//
//    }

}
