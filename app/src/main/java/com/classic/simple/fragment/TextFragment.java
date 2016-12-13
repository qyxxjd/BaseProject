package com.classic.simple.fragment;

import com.classic.android.base.BaseFragment;
import com.classic.android.utils.ToastUtil;
import com.classic.simple.R;

public class TextFragment extends BaseFragment {

    @Override public int getLayoutResId() {
        return R.layout.fragment_text;
    }

    @Override public void onFragmentShow() {
        super.onFragmentShow();
        ToastUtil.showToast(getActivity(), "TextFragment --> onFragmentShow()");
    }

    @Override public void onFragmentHide() {
        super.onFragmentHide();
        ToastUtil.showToast(getActivity(), "TextFragment --> onFragmentHide()");
    }
}
