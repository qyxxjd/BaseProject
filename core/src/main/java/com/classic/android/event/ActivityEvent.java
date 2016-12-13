package com.classic.android.event;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.event
 *
 * 文件描述: Activity事件类型
 * 创 建 人: 续写经典
 * 创建时间: 2016/11/16 10:00
 */
@IntDef({ActivityEvent.CREATE, ActivityEvent.START, ActivityEvent.RESTART, ActivityEvent.RESUME,
         ActivityEvent.PAUSE, ActivityEvent.STOP, ActivityEvent.DESTROY})
@Retention(RetentionPolicy.CLASS)
public @interface ActivityEvent {

    int CREATE  = 0x00;
    int START   = 0x01;
    int RESTART = 0x02;
    int RESUME  = 0x03;
    int PAUSE   = 0x04;
    int STOP    = 0x05;
    int DESTROY = 0x06;
}
