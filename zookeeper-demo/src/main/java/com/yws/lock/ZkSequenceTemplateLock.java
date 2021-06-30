package com.yws.lock;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ZkSequenceTemplateLock extends AbstractTemplateLock{
    private static final String CONNECT_STRING = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 5*1000;
    private static final int CONNECTION_TIMEOUT = 3*1000;
    private static final String LOCK_PATH = "/yws";


    private ZkClient client;
    private String currentPath;
    private String beforePath;

    public ZkSequenceTemplateLock(){
        client = new ZkClient(CONNECT_STRING);
        if (!client.exists(LOCK_PATH)) {
            client.createPersistent(LOCK_PATH);
        }
        log.info("zk client连接成功：{}", CONNECT_STRING);
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
        //给排在前面的节点增加数据删除的watcher，本质是启动另一个线程去监听上一个节点
        client.subscribeDataChanges(beforePath, listener);

        if (client.exists(beforePath)) {
            System.out.println("阻塞" + currentPath);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //取消watcher注册
        client.unsubscribeDataChanges(beforePath, listener);
    }

    @Override
    protected boolean tryLock() {
        if (currentPath == null) {
            //创建一个临时顺序节点
            currentPath = client.createEphemeralSequential(LOCK_PATH + "/", "lock-data");
            System.out.println("current:" + currentPath);
        }

        //获得所有的子节点并排序。临时节点名称为自增长的字符串
        List<String> childrens = client.getChildren(LOCK_PATH);
        //排序List,按自然顺序排序
        Collections.sort(childrens);

        if (currentPath.equals(childrens.get(0))) {
            return true;
        } else {
            //如果当前节点不是排第一，则获取前面一个节点信息，赋值给beforePath
            int curIndex = childrens.indexOf(currentPath.substring(LOCK_PATH.length() + 1));

            beforePath = LOCK_PATH + "/" + childrens.get(curIndex - 1);
        }
        System.out.println("beforePath：" + beforePath);
        return false;
    }

    @Override
    protected void releaseLock() {
        System.out.println("delete:" + currentPath);
        client.delete(currentPath);
    }
}
