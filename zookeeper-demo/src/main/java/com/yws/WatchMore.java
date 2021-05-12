package com.yws;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WatchMore {
    private ZooKeeper zk;

    private static final String CONNECT_STRING = "127.0.0.1";
    private static final int SESSION_TIMEOUT = 5*1000;
    private static final String PATH = "/yws";

    /**
     * 启动返回zookeeper
     * @return
     * @throws IOException
     */
    public ZooKeeper start() throws IOException {
        return zk = new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }

    /**
     * 停掉服务
     * @throws InterruptedException
     */
    public void stop() throws InterruptedException {
        if (zk != null) {
            zk.close();
        }
    }

    /**
     * 创建zNode
     * @param path
     * @param data
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void createZNode(String path, String data) throws KeeperException, InterruptedException {
        zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 获取Znode值
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String getZNode(String path) throws KeeperException, InterruptedException {
        byte[] data = zk.getData(path, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                try {
                    triggerValue(path);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, new Stat());

        return new String(data);
    }

    /**
     * 观察回调触发返回值
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String triggerValue(String path) throws KeeperException, InterruptedException {
        byte[] data = zk.getData(path, event -> {
            try {
                triggerValue(path);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }, new Stat());
        String retValue = new String(data);
        System.out.println("*******triggleValue:" + retValue);
        return retValue;
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        WatchMore watchMore = new WatchMore();
        watchMore.setZk(watchMore.start());

        if (watchMore.getZk().exists(PATH, false) == null) {
            watchMore.createZNode(PATH, "AAAAAA");

            System.out.println("==========>成功设值：" + watchMore.getZNode(PATH));

            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        }else {
            System.out.println("i have znode");
        }
    }



    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }
}
