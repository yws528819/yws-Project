package com.yws;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * 一、缓冲区(Buffer):在]ava NIO中负责数据的存取。缓冲区就是数组。用于存储不同数据类型的数据
 * 根据数据类型不同( boolean除外)，提供了相应类型的缓冲区：
 * ByteBuffer
 * CharBuffer
 * ShortBuffer
 * IntBuffer
 * Long Buffer
 * FloatBuffer
 * DoubleBuffer
 * 上述冲区的管理方式几乎一致，通过a11ocate（）获取缓冲区
 *
 *
 * 二、缓冲区存取数据的两个核心方法：
 * put（）:存入数据到缓冲区中
 * get（）:获取缓冲区中的数据
 *
 * 三、缓冲区中的四个核心属性
 * capacity:容量，表示冲区中最大存備数据的容量。一旦声明不能改变。
 * 1imit:界限，表示缓冲区中可以操作数据的大小。(1imit后数据不能进行读写)
 * position:位置，表示缓冲区中正在操作数据的位置。
 * mark:标记，表示记录当前 position的位置。可以通过 reset（）恢复到mark的位置
 * 
 * 0 <=mark<= position<=1imit<= capacity
 */
public class TestBuffer {
    @Test
    public void test01() {
        //1.分配一个指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("-----allocate------");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        //2.利用put()存人数据到数据缓冲区中
        String str = "abcd";
        byteBuffer.put(str.getBytes(), 0, str.length());
        System.out.println("-----put------");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        //3.切换读取数据模式
        byteBuffer.flip();
        System.out.println("-----flip------");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

        //4.利用get()读取缓冲区数据
        byte[] dst = new byte[str.length()];
        byteBuffer.get(dst);
        System.out.println(new String(dst, 0, dst.length));
        System.out.println("-----get------");
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());

    }
}
