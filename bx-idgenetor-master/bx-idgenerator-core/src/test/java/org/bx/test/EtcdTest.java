package org.bx.test;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月07日 00时22分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public class EtcdTest {
//
//    @Test
//    public void test() throws ExecutionException, InterruptedException {
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        Lease leaseClient = client.getLeaseClient();
//        Maintenance maintenanceClient = client.getMaintenanceClient();
//        ByteSequence key1 = ByteSequence.from("/BitLee/1", Charset.forName("utf-8"));
//        ByteSequence value = ByteSequence.from("100", Charset.forName("utf-8"));
//        PutResponse putResponse = kvClient.put(key1, value, PutOption.DEFAULT).get();
//        // put
//        System.out.println(putResponse.getHeader().getRevision());
//
//        ByteSequence key2 = ByteSequence.from("/BitLee/2", Charset.forName("utf-8"));
//        value = ByteSequence.from("101", Charset.forName("utf-8"));
//        putResponse = kvClient.put(key2, value, PutOption.DEFAULT).get();
//
//
//        value = ByteSequence.from("103", Charset.forName("utf-8"));
//        putResponse = kvClient.put(key2, value, PutOption.DEFAULT).get();
//
//        ByteSequence prefix = ByteSequence.from("/BitLee/", Charset.forName("utf-8"));
//        // getId
//        CompletableFuture<GetResponse> admin = kvClient.get(prefix, GetOption.newBuilder().withPrefix(prefix).build());
//        GetResponse getResponse = admin.get();
//        System.out.println(getResponse.getKvs().get(1).getVersion());
//    }
//
//
//    @Test
//    public void test2() throws ExecutionException, InterruptedException {
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        Lease leaseClient = client.getLeaseClient();
//        Maintenance maintenanceClient = client.getMaintenanceClient();
//        ByteSequence key1 = ByteSequence.from("/BitLee", Charset.forName("utf-8"));
//        ByteSequence value = ByteSequence.from("100", Charset.forName("utf-8"));
//        PutResponse putResponse = kvClient.put(key1, value).get();
//
//        ByteSequence key2 = ByteSequence.from("/BitLee/1-", Charset.forName("utf-8"));
//        putResponse = kvClient.put(key2, value, PutOption.DEFAULT).get();
//
//        System.out.println(putResponse.getHeader().getRevision());
//        CompletableFuture<GetResponse> admin = kvClient.get(key2, GetOption.newBuilder().build());
//        GetResponse getResponse = admin.get();
//        System.out.println(getResponse.getKvs().size());
//        System.out.println(getResponse.getKvs().get(0).getModRevision());
//        System.out.println(getResponse.getKvs().get(0).getCreateRevision());
//        System.out.println(getResponse.getKvs().get(0).getVersion());
//    }
//
//
//    @Test
//    public void testLock() throws ExecutionException, InterruptedException {
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        Lease leaseClient = client.getLeaseClient();
//        Maintenance maintenanceClient = client.getMaintenanceClient();
//        ByteSequence key1 = ByteSequence.from("/Guohua", Charset.forName("utf-8"));
//        ByteSequence value = ByteSequence.from("100", Charset.forName("utf-8"));
////        PutResponse putResponse = kvClient.put(key1, value, PutOption.newBuilder().withPrevKV().build()).getId();
////        long revision = putResponse.getHeader().getRevision();
////        System.out.println(revision);
//
//        key1 = ByteSequence.from("/meiguo", Charset.forName("utf-8"));
//        value = ByteSequence.from("100", Charset.forName("utf-8"));
//        PutResponse putResponse = kvClient.put(key1, value, PutOption.newBuilder().withPrevKV().build()).get();
//        CompletableFuture<GetResponse> admin = kvClient.get(key1, GetOption.DEFAULT);
//        GetResponse getResponse = admin.get();
//        long revision = putResponse.getHeader().getRevision();
//        System.out.println(revision);
//        System.out.println("前一版本：" + putResponse.getPrevKv().getVersion());
//        System.out.println(getResponse.getKvs().get(0).getVersion());
//
//
////        key1 = ByteSequence.from("/meiguo/1", Charset.forName("utf-8"));
////        value = ByteSequence.from("100", Charset.forName("utf-8"));
////        putResponse = kvClient.put(key1, value, PutOption.newBuilder().withPrevKV().build()).getId();
////
//////        key1 = ByteSequence.from("/meiguo/2", Charset.forName("utf-8"));
//////        value = ByteSequence.from("100", Charset.forName("utf-8"));
//////        putResponse = kvClient.put(key1, value, PutOption.newBuilder().withPrevKV().build()).getId();
//////
////        key1 = ByteSequence.from("/meiguo", Charset.forName("utf-8"));
////        value = ByteSequence.from("100", Charset.forName("utf-8"));
////        putResponse = kvClient.put(key1, value, PutOption.newBuilder().withPrevKV().build()).getId();
////
////        kvClient.delete(key1, DeleteOption.newBuilder().build());
//
//        key1 = ByteSequence.from("/meiguo", Charset.forName("utf-8"));
//        value = ByteSequence.from("100", Charset.forName("utf-8"));
//        putResponse = kvClient.put(key1, value, PutOption.newBuilder().withPrevKV().build()).get();
//        System.out.println("前一版本：" + putResponse.getPrevKv().getVersion());
//
//
//    }
//
//
//    @Test
//    public void testMultiPut() throws ExecutionException, InterruptedException {
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        CountDownLatch  countDownLatch = new CountDownLatch(500);
//        ExecutorService threadPool = Executors.newFixedThreadPool(500);
//        for (int i = 0; i < 500; i++) {
//            threadPool.submit(new Runnable() {
//                @Override
//                public void run() {
//                    KV kvClient = client.getKVClient();
//
//                    ByteSequence key1 = ByteSequence.from("/niubi", Charset.forName("utf-8"));
//                    ByteSequence value = ByteSequence.from("100", Charset.forName("utf-8"));
//
//                    PutResponse putResponse = null;
//                    try {
//                        putResponse = kvClient.put(key1, value, PutOption.newBuilder().withPrevKV().build()).get();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                    CompletableFuture<GetResponse> admin = kvClient.get(key1, GetOption.DEFAULT);
//                    GetResponse getResponse = null;
//                    try {
//                        getResponse = admin.get();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("前一版本：" + putResponse.getPrevKv().getVersion());
//                    countDownLatch.countDown();
//                }
//            });
//        }
//        countDownLatch.await();
//        threadPool.awaitTermination(10, TimeUnit.DAYS);
//        threadPool.shutdown();
//
//    }
//
//
//    @Test
//    public void testWatch() throws ExecutionException, InterruptedException {
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        ByteSequence key1 = ByteSequence.from("/007", Charset.forName("utf-8"));
//        ByteSequence value = ByteSequence.from("100", Charset.forName("utf-8"));
//
//        Watch watchClient = client.getWatchClient();
//        watchClient.watch(key1, WatchOption.newBuilder().withPrevKV(true).build(), new Watch.Listener() {
//            @Override
//            public void onNext(WatchResponse response) {
//                System.out.println(response.getHeader());
//                List<WatchEvent> events = response.getEvents();
//                for (WatchEvent watchEvent : events) {
//                    KeyValue keyValue = watchEvent.getKeyValue();
//                    System.out.println("监听事件：" + keyValue.getVersion());
//                }
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//
//            @Override
//            public void onCompleted() {
//
//            }
//        });
//
//        PutResponse putResponse = kvClient.put(key1, value, PutOption.newBuilder().withPrevKV().build()).get();
//
//        Thread.sleep(5000);
//    }
//
//    @Test
//    public void test001() throws Exception {
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        ByteSequence key1 = ByteSequence.from("/wgh1", Charset.forName("utf-8"));
//        ByteSequence value0 = ByteSequence.from("0", Charset.forName("utf-8"));
//
//        //ByteSequence value = ByteSequence.from("3", Charset.forName("utf-8"));
//        GetResponse getZeroResponse = kvClient.get(key1, GetOption.newBuilder().withLimit(100).build()).get();
//        DeleteResponse deleteResponse = kvClient.delete(key1).get();
//        PutResponse putResponse1 = kvClient.put(key1, value0, PutOption.newBuilder().withPrevKV().build()).get();
//        ByteSequence value1 = ByteSequence.from("10", Charset.forName("utf-8"));
//        PutResponse putResponse2 = kvClient.put(key1, value1, PutOption.newBuilder().withPrevKV().build()).get();
//
//        System.out.println("zeroGet:" + getZeroResponse + ",getKvs: " + getZeroResponse.getKvs() + ",getCount: " + getZeroResponse.getCount());
//        System.out.println("zeroDelete:" + deleteResponse + ",deleted: " + deleteResponse.getDeleted() + " getPrevKvs : " + deleteResponse.getPrevKvs());
//        System.out.println("zeroPut:" + putResponse1 + ",hasPrevKv: " + putResponse1.hasPrevKv());
//        System.out.println("zeroPut2:" + putResponse2 + ",hasPrevKv: " + putResponse2.hasPrevKv());
//
//        GetResponse get2 = kvClient.get(key1, GetOption.newBuilder().withRevision(putResponse1.getHeader().getRevision()).build()).get();
//        System.out.println("zeroGet:" + get2 + ",getKvs: " + get2.getKvs() + ",getCount: " + get2.getCount());
//
//
//    }
//
//    @Test
//    public void test_trx() throws Exception {
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        ByteSequence key = ByteSequence.from("/sentinon", Charset.forName("utf-8"));
//        ByteSequence key1 = ByteSequence.from("/test_trx07", Charset.forName("utf-8"));
//        ByteSequence value0 = ByteSequence.from("0", Charset.forName("utf-8"));
//        ByteSequence value1 = ByteSequence.from("10", Charset.forName("utf-8"));
//
//        //ByteSequence value = ByteSequence.from("3", Charset.forName("utf-8"));
//        GetResponse getZeroResponse = kvClient.get(key1, GetOption.newBuilder().withLimit(100).build()).get();
//        DeleteResponse deleteResponse = kvClient.delete(key1).get();
//        PutResponse putResponse1 = kvClient.put(key1, value0, PutOption.newBuilder().withPrevKV().build()).get();
//
//        System.out.println("zeroGet:" + getZeroResponse + ",getKvs: " + getZeroResponse.getKvs() + ",getCount: " + getZeroResponse.getCount());
//        System.out.println("zeroDelete:" + deleteResponse + ",deleted: " + deleteResponse.getDeleted() + " getPrevKvs : " + deleteResponse.getPrevKvs());
//        System.out.println("zeroPut:" + putResponse1 + ",hasPrevKv: " + putResponse1.hasPrevKv());
//
//
//        Thread t1 = new Thread(() -> {
//            try {
//                ByteSequence value = ByteSequence.from("test1", Charset.forName("utf-8"));
//                Txn txn = kvClient.txn();
//                TxnResponse txnResponse = txn.If(new Cmp(key1, Cmp.Op.NOT_EQUAL, CmpTarget.value(value1)))
//                        .Then(Op.put(key1, value, PutOption.DEFAULT), Op.get(key1, GetOption.DEFAULT)).commit().get();
//                System.out.println(Thread.currentThread().getName() + " isSucceeded:" + txnResponse.isSucceeded());
//                System.out.println(Thread.currentThread().getName() + "  getPutResponses:" + txnResponse.getPutResponses());
//                System.out.println(Thread.currentThread().getName() + "  getGetResponses:" + txnResponse.getGetResponses());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//
//        }, "test1");
//        Thread t2 = new Thread(() -> {
//            try {
//                ByteSequence value = ByteSequence.from("test2", Charset.forName("utf-8"));
//
//                Txn txn = kvClient.txn();
//                TxnResponse txnResponse = txn.If(new Cmp(key1, Cmp.Op.NOT_EQUAL, CmpTarget.value(value1)))
//                        .Then(Op.put(key1, value, PutOption.DEFAULT), Op.get(key1, GetOption.DEFAULT)).commit().get();
//                System.out.println(Thread.currentThread().getName() + "  isSucceeded:" + txnResponse.isSucceeded());
//                System.out.println(Thread.currentThread().getName() + "  getPutResponses:" + txnResponse.getPutResponses());
//                System.out.println(Thread.currentThread().getName() + "  getGetResponses:" + txnResponse.getGetResponses());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//
//        }, "test2");
//        Thread t3 = new Thread(() -> {
//            try {
//                ByteSequence value = ByteSequence.from("test3", Charset.forName("utf-8"));
//                Txn txn = kvClient.txn();
//                TxnResponse txnResponse = txn.If(new Cmp(key1, Cmp.Op.NOT_EQUAL, CmpTarget.value(value1)))
//                        .Then(Op.put(key1, value, PutOption.DEFAULT), Op.get(key1, GetOption.DEFAULT)).commit().get();
//                System.out.println(Thread.currentThread().getName() + "  isSucceeded:" + txnResponse.isSucceeded());
//                System.out.println(Thread.currentThread().getName() + "  getPutResponses:" + txnResponse.getPutResponses());
//                System.out.println(Thread.currentThread().getName() + "  getGetResponses:" + txnResponse.getGetResponses());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }, "test3");
//        Thread t4 = new Thread(() -> {
//            try {
//                ByteSequence value = ByteSequence.from("test4", Charset.forName("utf-8"));
//                Txn txn = kvClient.txn();
//                TxnResponse txnResponse = txn.If(new Cmp(key1, Cmp.Op.NOT_EQUAL, CmpTarget.value(value1)))
//                        .Then(Op.put(key1, value, PutOption.DEFAULT), Op.get(key1, GetOption.DEFAULT))
//                        .commit().get();
//                System.out.println(Thread.currentThread().getName() + "  isSucceeded:" + txnResponse.isSucceeded());
//                System.out.println(Thread.currentThread().getName() + "  getPutResponses:" + txnResponse.getPutResponses());
//                System.out.println(Thread.currentThread().getName() + "  getGetResponses:" + txnResponse.getGetResponses());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//
//        }, "test4");
//        Thread t5 = new Thread(() -> {
//            try {
//                ByteSequence value = ByteSequence.from("test5", Charset.forName("utf-8"));
//                Txn txn = kvClient.txn();
//                TxnResponse txnResponse = txn.If(new Cmp(key1, Cmp.Op.NOT_EQUAL, CmpTarget.value(value1)))
//                        .Then(Op.put(key1, value, PutOption.DEFAULT), Op.get(key1, GetOption.DEFAULT))
//                        .commit().get();
//                System.out.println(Thread.currentThread().getName() + "  isSucceeded:" + txnResponse.isSucceeded());
//                System.out.println(Thread.currentThread().getName() + "  getPutResponses:" + txnResponse.getPutResponses());
//                System.out.println(Thread.currentThread().getName() + "  getGetResponses:" + txnResponse.getGetResponses());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//
//        }, "test5");
//
//
//        /*t1.start();
//        t2.start();
//        t3.start();
//        t4.start();
//        t5.start();
//
//        t1.join();
//        t2.join();
//        t3.join();
//        t4.join();
//        t5.join();
//*/
//        PutResponse putResponse = null;
//        GetResponse getResponse = null;
//        GetResponse getResponse1 = null;
//
//        /*try {
//            Txn txn = kvClient.txn();
//            TxnResponse txnResponse = txn.If(null)
//                    .Then(Op.put(key1, value, PutOption.DEFAULT), Op.get(key1, GetOption.DEFAULT))
//                    .Else(null).commit().get();
//
//            //client.getLockClient();
//            getResponse = kvClient.get(key1).get();
//            putResponse = kvClient.put(key1, value, PutOption.newBuilder().build()).get();
//            getResponse1 = kvClient.get(key1).get();
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        System.out.println("getResponse:"+getResponse);
//        System.out.println(putResponse);
//        System.out.println("getResponse1:"+getResponse1);*/
//
//      /*  Txn txn = kvClient.txn();
//        TxnResponse txnResponse = txn.If(new Cmp(key1, Cmp.Op.NOT_EQUAL,CmpTarget.value(value)))
//                .Then(Op.put(key1, value, PutOption.DEFAULT), Op.get(key1, GetOption.DEFAULT))
//                .Else(Op.put(key1, value, PutOption.DEFAULT), Op.get(key1, GetOption.DEFAULT)).commit().get();
//        System.out.println("success");
//        System.out.println(txnResponse.isSucceeded());
//        System.out.println("getPutResponses");
//        System.out.println(txnResponse.getPutResponses());
//        System.out.println("getGetResponses");
//        System.out.println(txnResponse.getGetResponses());*/
//
//
//    }
//
//    @Test
//    public void test_lock() throws Exception {
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        String lockKey = "/etcd/lock";
//
//
//        String keyS = "/tag/wgh";
//        ByteSequence key = ByteSequence.from("/tag/wgh001", Charset.forName("utf-8"));
//        //初始化
//        ByteSequence value0 = ByteSequence.from("0", Charset.forName("utf-8"));
//        PutResponse initResponse = kvClient.put(key, value0, PutOption.DEFAULT).get();
//        System.out.println("初始化: " + initResponse);
//
//        Thread t1 = new Thread(() -> {
//            AbstractLock lock = new EtcdDistributeLock(client, lockKey + keyS, 20, TimeUnit.SECONDS);
//            try {
//                lock.lock();
//                CompletableFuture<GetResponse> admin1 = kvClient.get(key, GetOption.DEFAULT);
//                GetResponse getResponse1 = admin1.get();
//                System.out.println("get响应:" + getResponse1);
//                List<KeyValue> kvs = getResponse1.getKvs();
//                ByteSequence getV = kvs.get(0).getValue();
//                Long added = new Long(getV.toString(Charset.forName("utf-8")));
//                added = added + 100;
//                ByteSequence value = ByteSequence.from(added.toString(), Charset.forName("utf-8"));
//                PutResponse putResponse = kvClient.put(key, value, PutOption.newBuilder().withPrevKV().build()).get();
//                System.out.println("put响应" + putResponse);
//                CompletableFuture<GetResponse> admin = kvClient.get(key, GetOption.DEFAULT);
//                GetResponse getResponse = admin.get();
//                System.out.println("最后一次get：" + getResponse);
//                Thread.sleep(50000);
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } finally {
//                //放锁
//                lock.unlock();
//            }
//
//        }, "test1");
//        Thread t2 = new Thread(() -> {
//            AbstractLock lock = new EtcdDistributeLock(client, lockKey + keyS, 20, TimeUnit.SECONDS);
//            try {
//                lock.lock();
//                CompletableFuture<GetResponse> admin1 = kvClient.get(key, GetOption.DEFAULT);
//                GetResponse getResponse1 = admin1.get();
//                System.out.println("get响应:" + getResponse1);
//                List<KeyValue> kvs = getResponse1.getKvs();
//                ByteSequence getV = kvs.get(0).getValue();
//                Long added = new Long(getV.toString(Charset.forName("utf-8")));
//                added = added + 100;
//                ByteSequence value = ByteSequence.from(added.toString(), Charset.forName("utf-8"));
//                PutResponse putResponse = kvClient.put(key, value, PutOption.newBuilder().withPrevKV().build()).get();
//                System.out.println("put响应" + putResponse);
//                CompletableFuture<GetResponse> admin = kvClient.get(key, GetOption.DEFAULT);
//                GetResponse getResponse = admin.get();
//                System.out.println("最后一次get：" + getResponse);
//                Thread.sleep(50000);
//
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } finally {
//                //放锁
//                lock.unlock();
//            }
//
//        }, "test2");
//        Thread t3 = new Thread(() -> {
//            AbstractLock lock = new EtcdDistributeLock(client, lockKey + keyS, 20, TimeUnit.SECONDS);
//            try {
//                lock.lock();
//                CompletableFuture<GetResponse> admin1 = kvClient.get(key, GetOption.DEFAULT);
//                GetResponse getResponse1 = admin1.get();
//                System.out.println("get响应:" + getResponse1);
//                List<KeyValue> kvs = getResponse1.getKvs();
//                ByteSequence getV = kvs.get(0).getValue();
//                Long added = new Long(getV.toString(Charset.forName("utf-8")));
//                added = added + 100;
//                ByteSequence value = ByteSequence.from(added.toString(), Charset.forName("utf-8"));
//                PutResponse putResponse = kvClient.put(key, value, PutOption.newBuilder().withPrevKV().build()).get();
//                System.out.println("put响应" + putResponse);
//                CompletableFuture<GetResponse> admin = kvClient.get(key, GetOption.DEFAULT);
//                GetResponse getResponse = admin.get();
//                System.out.println("最后一次get：" + getResponse);
//                Thread.sleep(50000);
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } finally {
//                //放锁
//                lock.unlock();
//            }
//
//        }, "test3");
//
//
//        t1.start();
//        t2.start();
//        t3.start();
//        t1.join();
//        t2.join();
//        t3.join();
//
//    }
//
//    @Test
//    public void test_trx01() throws Exception {
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        ByteSequence key1 = ByteSequence.from("/suijide", Charset.forName("utf-8"));
//        ByteSequence value1 = ByteSequence.from("10", Charset.forName("utf-8"));
//        ByteSequence value = ByteSequence.from("test5", Charset.forName("utf-8"));
//        Txn txn = kvClient.txn();
//        TxnResponse txnResponse = txn.If(new Cmp(key1, Cmp.Op.LESS, CmpTarget.value(value1)))
//                .Then(Op.put(key1, value, PutOption.DEFAULT), Op.get(key1, GetOption.DEFAULT))
//                .commit().get();
//        System.out.println(Thread.currentThread().getName() + "  isSucceeded:" + txnResponse.isSucceeded());
//        System.out.println(Thread.currentThread().getName() + "  getPutResponses:" + txnResponse.getPutResponses());
//        System.out.println(Thread.currentThread().getName() + "  getGetResponses:" + txnResponse.getGetResponses());
//    }
//
//    @Test
//    public void test002() throws Exception {
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        ByteSequence key1 = ByteSequence.from("/test002", Charset.forName("utf-8"));
//        ByteSequence value1 = ByteSequence.from("10", Charset.forName("utf-8"));
//        CompletableFuture<GetResponse> getResponseCompletableFuture = kvClient.get(key1);
//        GetResponse getResponse = getResponseCompletableFuture.get();
//        System.out.println(getResponse);
//
//        getResponse = kvClient.get(key1).get();
//        System.out.println(getResponse);
//
//        PutResponse putResponse = kvClient.put(key1, value1, PutOption.newBuilder().withPrevKV().build()).get();
//        System.out.println(putResponse);
//
//        getResponse = kvClient.get(key1).get();
//        System.out.println(getResponse);
//
//
//        Txn txn = kvClient.txn();
//        ByteSequence value2 = ByteSequence.from("12", Charset.forName("utf-8"));
//        TxnResponse txnResponse = txn.If(new Cmp(key1, Cmp.Op.EQUAL, CmpTarget.modRevision(putResponse.getHeader().getRevision())))
//                .Then(Op.put(key1, value2, PutOption.newBuilder().withPrevKV().build())).commit().get();
//        System.out.println(Thread.currentThread().getName() + "  isSucceeded:" + txnResponse.isSucceeded());
//        System.out.println(Thread.currentThread().getName() + "  getPutResponses:" + txnResponse.getPutResponses());
//        System.out.println(Thread.currentThread().getName() + "  getGetResponses:" + txnResponse.getGetResponses());
//
//        getResponse = kvClient.get(key1).get();
//        System.out.println(getResponse);
//
//    }
//
//    @Test
//    public void test_pre() throws Exception {
//
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        String source = "/test_pre";
//        ByteSequence key1 = ByteSequence.from(source + "/", Charset.forName("utf-8"));
//
//        for (int i = 0; i < 14; i++) {
//            ByteSequence key = ByteSequence.from(source + "/" + i, Charset.forName("utf-8"));
//            ByteSequence value = ByteSequence.from(i + "," + i, Charset.forName("utf-8"));
//            kvClient.put(key, value);
//        }
//        CompletableFuture<GetResponse> getResponseCompletableFuture = kvClient.get(key1, GetOption.newBuilder().withPrefix(key1).withSortField(GetOption.SortTarget.CREATE).build());
//        GetResponse getResponse = getResponseCompletableFuture.get();
//        System.out.println(getResponse);
//    }
//
//    @Test
//    public void test0010()throws Exception{
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        KeyInfoBean dto = new KeyInfoBean();
//        dto.setStatus(true);
//        dto.setBizTag("oms");
//        dto.setGroupId(1234);
//        dto.setDescription("我是测试");
//        dto.setMaxId(0);
//        dto.setUpdateTime(System.currentTimeMillis());
//
//        String keyS = Constants.getKeyByGroupIdAndTag(dto);
//        ByteSequence key1 = ByteSequence.from(Constants.getETCDKeyByGroupId(keyS), Charset.forName("utf-8"));
//        String s = JSONUtil.toJsonStr(dto);
//        ByteSequence value1 = ByteSequence.from(s, Charset.forName("utf-8"));
//        PutResponse putResponse = kvClient.put(key1, value1).get();
//        System.out.println(putResponse);
//
//    }
//
//    @Test
//    public void test_delte() throws Exception{
//        Client client = EtcdClientFactory.getClient();
//        KV kvClient = client.getKVClient();
//        ByteSequence key1 = ByteSequence.from("/id/", Charset.forName("utf-8"));
//        /*DeleteResponse deleteResponse = kvClient.delete(key1, DeleteOption.newBuilder().withPrefix(key1).build()).get();
//        System.out.println(deleteResponse);*/
//
//        GetResponse getResponse = kvClient.get(key1, GetOption.newBuilder().withPrefix(key1).build()).get();
//        System.out.println(getResponse);
//
//
//    }
}
