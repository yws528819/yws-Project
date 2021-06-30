package com.yws.lock;

/**
 * 参考 https://zhuanlan.zhihu.com/p/147181364
 * https://zhuanlan.zhihu.com/p/348743201
 */
public interface Lock {
    /**
     * 获取🔒
     * @throws Exception
     */
    void getLock() throws Exception;

    /**
     * 释放🔒
     * @throws Exception
     */
    void unLock() throws Exception;
}
