package com.classic.core.interfaces;

/**
 * 应用名称: RxBasicProject
 * 包 名 称: com.classic.core.interfaces
 *
 * 文件描述: 规范注册的接口协议
 * 创 建 人: 续写经典
 * 创建时间: 2015/6/21 10:00
 */
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
