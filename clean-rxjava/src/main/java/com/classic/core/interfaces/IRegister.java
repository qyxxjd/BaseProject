package com.classic.core.interfaces;

/**
 * 规范注册的接口协议
 *
 * @author classic
 * @version v1.0, 2018/4/22 下午4:48
 */
@SuppressWarnings("unused")
public interface IRegister {

    /**
     * 注册广播、服务
     */
    void register();

    /**
     * 注销广播、服务
     */
    void unRegister();
}
