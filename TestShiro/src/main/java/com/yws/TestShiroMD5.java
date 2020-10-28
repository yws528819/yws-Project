package com.yws;

import org.apache.shiro.crypto.hash.Md5Hash;

public class TestShiroMD5 {
    public static void main(String[] args) {
        //使用MD5
        Md5Hash md5Hash = new Md5Hash("123");
        System.out.println(md5Hash.toHex());

        //使用MD5 + salt处理，默认把salt加在密码前面，再进行加密，即密码为xo*7ps123
        Md5Hash md5Hash1 = new Md5Hash("123", "xo*7ps");
        System.out.println(md5Hash1.toHex());

        //使用MD5 + salt + hash散列
        Md5Hash md5Hash2 = new Md5Hash("123", "xo*7ps", 1024);
        System.out.println(md5Hash2.toHex());

    }
}
