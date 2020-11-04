package com.yjfei.antibot.stream.engine;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public abstract class Workflow implements InitializingBean, DisposableBean,Lifecycle{

    /**
     * 初始化
     */
    protected abstract void init();

    /**
     * 强制初始化必须调用init方法
     */
    @Override
    public final void afterPropertiesSet() {
        init();
    }

    /**
     * 强制销毁前必须调用stop方法
     */
    @Override
    public final void destroy() {
        stop();
    }
}
