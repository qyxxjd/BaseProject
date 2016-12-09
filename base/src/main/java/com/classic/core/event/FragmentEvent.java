package com.classic.core.event;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 应用名称: RxBasicProject
 * 包 名 称: com.classic.core.event
 *
 * 文件描述: Fragment事件类型
 * 创 建 人: 续写经典
 * 创建时间: 2016/11/16 10:00
 */
@IntDef({FragmentEvent.ATTACH, FragmentEvent.CREATE, FragmentEvent.CREATE_VIEW,
         FragmentEvent.VIEW_CREATE, FragmentEvent.START, FragmentEvent.RESUME, FragmentEvent.PAUSE,
         FragmentEvent.STOP, FragmentEvent.DESTROY_VIEW, FragmentEvent.DESTROY,
         FragmentEvent.DETACH})
@Retention(RetentionPolicy.CLASS)
public @interface FragmentEvent {

    int ATTACH       = 0x00;
    int CREATE       = 0x01;
    int CREATE_VIEW  = 0x02;
    int VIEW_CREATE  = 0x03;
    int START        = 0x04;
    int RESUME       = 0x05;
    int PAUSE        = 0x06;
    int STOP         = 0x07;
    int DESTROY_VIEW = 0x08;
    int DESTROY      = 0x09;
    int DETACH       = 0x0A;
}
