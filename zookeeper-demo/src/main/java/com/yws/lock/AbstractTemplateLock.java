package com.yws.lock;

public abstract class AbstractTemplateLock implements Lock{
    @Override
    public void getLock() throws Exception {
        if (tryLock()) {
            System.out.println(Thread.currentThread().getName() + "获取🔒成功");
        }else {
            //等待
            waitLock();//事件监听，如果节点被删除则可以重新获取
            //重新获取
            getLock();
        }
    }

    @Override
    public void unLock() throws Exception {
        releaseLock();
    }


    protected abstract void waitLock();

    protected abstract boolean tryLock();

    protected abstract void releaseLock();
}
