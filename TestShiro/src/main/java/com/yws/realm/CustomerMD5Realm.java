package com.yws.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * 使用自定义Realm加入MD5 + salt + hash
 */
public class CustomerMD5Realm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //获取身份信息
        String principal = (String) token.getPrincipal();

        //根据用户名查询数据库
        //...

        if ("yws".equals(principal)) {
            //参数1：存在数据库用户名 参数2：数据库存的密码 md5+salt之后的密码 参数3：注册时的随机盐 参数4：realm的名字
            return new SimpleAuthenticationInfo(principal,
                    "2d66e7af20ed07b8efbdf22c4d7cc043",
                    ByteSource.Util.bytes("xo*7ps"),
                    this.getName());
        }


        return null;
    }
}
