package com.classic.core.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * 应用名称: RxBasicProject
 * 包 名 称: com.classic.core.utils
 *
 * 文件描述: 文本输入框工具类
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/4 17:26
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"}) public final class EditTextUtil {

    private EditTextUtil() { }

    /**
     * 限制内容长度
     */
    public static void lengthFilter(@NonNull final EditText editText, final int length) {
        InputFilter[] filters = new InputFilter[1];

        filters[0] = new InputFilter.LengthFilter(length) {
            @Override public CharSequence filter(
                    CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (dest.toString().length() >= length) {
                    return "";
                }
                return source;
            }
        };
        editText.setFilters(filters);
    }

    /**
     * 限制中文字符的长度
     */
    public static void lengthChineseFilter(@NonNull final EditText editText, final int length) {
        InputFilter[] filters = new InputFilter[1];

        filters[0] = new InputFilter.LengthFilter(length) {
            public CharSequence filter(
                    CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // 可以检查中文字符
                boolean isChinese = StringUtil.checkNameChese(source.toString());
                if (!isChinese || dest.toString().length() >= length) {
                    return "";
                }
                return source;
            }
        };
        editText.setFilters(filters);
    }

    /**
     * 设置密码是否可见
     */
    public static void passwordVisibleToggle(@NonNull EditText editText) {
        if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.setInputType(
                    InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    /**
     * 设置文本，并将光标移动到文本末尾
     */
    public static void setText(@NonNull EditText editText, @NonNull String text) {
        editText.setText(text);
        editText.setSelection(text.length());
    }

    /**
     * 监听文本框内容，动态改变指定view状态，
     * 文本框无内容则隐藏指定view,有内容则显示指定view;
     * 点击view自动清理文本框内容。
     */
    public static void autoClear(@NonNull final EditText editText, @NonNull final View clearView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearView.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
            }

            @Override public void afterTextChanged(Editable s) { }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override public void onFocusChange(View v, boolean hasFocus) {
                clearView.setVisibility(
                        !TextUtils.isEmpty(editText.getText().toString()) && hasFocus ? View.VISIBLE
                                                                                      : View.GONE);
            }
        });
        clearView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    /**
     * 屏蔽复制、粘贴功能
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB) public static void pasteUnable(
            @NonNull EditText editText) {
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override public void onDestroyActionMode(ActionMode mode) {

            }
        });
        editText.setLongClickable(false);
    }
}
