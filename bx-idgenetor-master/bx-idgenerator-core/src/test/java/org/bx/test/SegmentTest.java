package org.bx.test;

public class SegmentTest {
//    private Segment segment;
//    private ExecutorService executor = Executors.newCachedThreadPool();
//
//    @Before
//    public void init() {
//        segment = new Segment();
//        segment.setMinId(0);
//        segment.setMaxId(100);
//        segment.setCurId(0);
//    }
//
//    @Test
//    public void testSingleThread() {
//        for (int i = 0; i < 11; i++) {
//            System.out.println(segment.nextID(10));
//        }
//    }
//
//    @Test
//    public void testMultiThread() throws InterruptedException {
//        for (int i = 0; i < 11; i++) {
//            executor.execute(() -> {
//                System.out.println(segment.nextID(10));
//            });
//        }
//        executor.shutdown();
//        executor.awaitTermination(1, TimeUnit.DAYS);
//
//    }
}
