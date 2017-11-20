package com.classic.simple.fragment;

import com.classic.android.base.BaseFragment;
import com.classic.android.utils.ToastUtil;
import com.classic.simple.R;

public class ImageFragment extends BaseFragment {

    @Override public int getLayoutResId() {
        return R.layout.fragment_image;
    }

    @Override public void onFragmentShow() {
        super.onFragmentShow();
        ToastUtil.showToast(mAppContext, "ImageFragment --> onFragmentShow()");
    }

    @Override public void onFragmentHide() {
        super.onFragmentHide();
        ToastUtil.showToast(mAppContext, "ImageFragment --> onFragmentHide()");
    }
}
