package com.classic.core.event;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Activity事件类型
 *
 * @author classic
 * @version v1.0, 2018/4/22 下午4:48
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
