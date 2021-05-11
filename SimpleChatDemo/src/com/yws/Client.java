package com.yws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;//用于连接服务器的socket

    public Client() throws IOException {
        Config config = new Config("com/yws/server_config.properties");
        try {
            socket = new Socket(config.getStringValue("ip"),
                    config.getIntValue("port"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("连接服务器失败");
        }
        System.out.println("连接服务器成功");
    }


    public void start() {
        //1.创建一个线程，接收服务器发过来的信息
        new Thread(new GetServerInfoHandler()).start();

        //2.给服务器发送消息
        Scanner scanner = new Scanner(System.in);
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String str = null;
            while (true) {
                str = scanner.nextLine();
                out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public class GetServerInfoHandler implements Runnable {

        @Override
        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String str = null;
                while (true) {
                    str = br.readLine();
                    System.out.println(str);
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("与服务器连接异常");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start();
    }
}
