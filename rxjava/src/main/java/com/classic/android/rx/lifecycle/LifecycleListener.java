package com.classic.android.rx.lifecycle;

import com.classic.android.event.ActivityEvent;
import com.classic.android.event.FragmentEvent;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.rx.lifecycle
 *
 * 文件描述: 生命周期监听
 * 创 建 人: 续写经典
 * 创建时间: 2016/12/8 17:03
 */
public interface LifecycleListener {

    /**
     * 生命周期改变回调方法
     *
     * @param event 事件类型
     * @see ActivityEvent
     * @see FragmentEvent
     */
    void onLifecycleChange(int event);
}
