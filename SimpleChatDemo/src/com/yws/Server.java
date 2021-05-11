package com.yws;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Server {

    private ServerSocket server;

    private ExecutorService threadPool;//线程池

    private BlockingQueue<String> messageQue;//阻塞队列，双缓冲队列，支持2个线程同时存取操作

    private Vector<PrintWriter> allOut;//存放所有客户端输出流的集合


    public Server() {

    }

    /**
     * 发送信息任务
     */
    private class SendMessageToAllClient implements Runnable {

        @Override
        public void run() {
            String str = null;
            while (true) {
                str = messageQue.poll();

                if (str != null) {
                    //消息转发给所有客户端
                    sendMessageToAllClient(str);
                }else {
                    //若队列空了，就休息一会，减少不必要的性能开销
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 添加一个客户端的输出流
     * @param out
     */
    public synchronized void addOut(PrintWriter out) {
        allOut.add(out);
    }

    /**
     *  客户端断开后，移除对应的输出流
     * @param out
     */
    public synchronized void removeOut(PrintWriter out) {
        allOut.remove(out);
    }

    /**
     * 遍历输出流，将消息发送给所有客户端
     * @param message
     */
    public synchronized void sendMessageToAllClient(String message) {
        /**
         * 在迭代集合元素时,不能对集合元素进行增删操作
         * Vector虽然是同步的,但仅仅是对于元素增删操作
         * 可以做到同步,遍历元素使用的迭代器依然不允许
         * 在迭代过程中删除元素.
         * 所以迭代元素的操作要与增删元素的操作互斥!
         */
        for (PrintWriter printWriter : allOut) {
            printWriter.print(message);
        }
    }


    /**
     * 处理客户端信息任务
     */
    private class GetClientInfoHandler implements Runnable {

        private Socket client;

        public GetClientInfoHandler(Socket socket) {
            this.client = socket;
        }

        @Override
        public void run() {

            try {
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                addOut(out);

                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String str = null;
                while (true) {
                    str = br.readLine();
                    if (str == null) {
                        throw new RuntimeException("用户数据异常");
                    }
                    //消息放入阻塞队列，等待转发
                    messageQue.offer(str);
                }



            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }

        }
    }



    /**
     * 启动服务器
     */
    public void start() throws IOException {
        /**
         * 启动转发线程
         */
        Thread sendThread = new Thread(new SendMessageToAllClient());
        sendThread.setDaemon(true);
        sendThread.start();

        /**
         * 处理接收客户端
         */
        while (true) {
            Socket socket = server.accept();
            threadPool.execute(new GetClientInfoHandler(socket));
        }
    }


}
