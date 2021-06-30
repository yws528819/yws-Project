package com.yws.lock;

import com.yws.lock.AbstractTemplateLock;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class ZkTemplateLock extends AbstractTemplateLock {


    private static final String CONNECT_STRING = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 5*1000;
    private static final int CONNECTION_TIMEOUT = 3*1000;
    private static final String LOCK_PATH = "/yws";

    private ZkClient client;

    public ZkTemplateLock() {
        client = new ZkClient(CONNECT_STRING, SESSION_TIMEOUT, CONNECTION_TIMEOUT);
        log.info("zk client 连接成功:{}", CONNECT_STRING);
    }

    @Override
    protected void waitLock() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("监测到节点被删除");
                countDownLatch.countDown();
            }
        };
        client.subscribeDataChanges(LOCK_PATH, listener);

        //阻塞自己
        if (client.exists(LOCK_PATH)) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //取消watcher注册
        client.unsubscribeDataChanges(LOCK_PATH, listener);
    }

    @Override
    protected boolean tryLock() {
        try{
            client.createEphemeral(LOCK_PATH);
            System.out.println("线程" + Thread.currentThread().getName() + "获取到🔒");
        }catch (Exception e) {
            log.error("创建失败");
            return false;
        }
        return true;
    }

    @Override
    protected void releaseLock() {
        client.delete(LOCK_PATH);
    }
}
