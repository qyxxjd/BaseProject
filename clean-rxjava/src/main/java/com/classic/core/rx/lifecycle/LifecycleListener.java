package com.classic.core.rx.lifecycle;

import com.classic.core.event.ActivityEvent;
import com.classic.core.event.FragmentEvent;

/**
 * 生命周期监听
 *
 * @author classic
 * @version v1.0, 2018/4/22 下午4:48
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
