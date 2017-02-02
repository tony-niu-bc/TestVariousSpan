package com.wzhnsc.testvariousspan;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;

public class ExcludedEmoticonEditText extends EditText {
	// 光标位置
	private int mOldCursorPos;
	// 输入表情前的文本
	private String mOldText;
	// 是否使用输入表情之前的文本
	private boolean mIsUseOldText;

	public ExcludedEmoticonEditText(Context context) {
        super(context);

        init();
	}

	public ExcludedEmoticonEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
	}

    public ExcludedEmoticonEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        // EditView 中有这个 com.android.internal.R.attr.editTextStyle 样式方使可以输入
        super(context, attrs, defStyleAttr);

        init();
    }

	private void init(){
		addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                if (!mIsUseOldText) {
                    mOldCursorPos = getSelectionEnd();
                    mOldText      = s.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    // 手机自带输入法会出现数组越界情况，所以 catch 后正常输入
                    if (!mIsUseOldText) {
                        // 表情符号的字符长度最小为2
                        if (count >= 2) {
                            CharSequence emoticon = s.subSequence(mOldCursorPos, mOldCursorPos + count);

                            if (containEmoticon(emoticon.toString())) {
                                mIsUseOldText = true;

                                // 是表情符号就将文本还原为输入表情符号之前的内容
                                setText(mOldText);

                                // 光标定于最后
                                setSelection(getText().length());
                            }
                        }
                    }
                    else {
                        mIsUseOldText = false;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
	}

    // 判断是否为表情符号
    public static boolean containEmoticon(String source) {
        for (int i = 0; i < source.length(); i++) {
            char    codePoint = source.charAt(i);
            // Emoji Unicode Tables
            // http://apps.timwhitlock.info/emoji/tables/unicode
            boolean isContain = (codePoint == 0x0)      ||
                                (codePoint == 0x9)      ||
                                (codePoint == 0xA)      ||
                                (codePoint == 0xD)      ||
                                ((codePoint >= 0x20) &&
                                 (codePoint <= 0xD7FF)) ||
                                ((codePoint >= 0xE000) &&
                                 (codePoint <= 0xFFFD)) ||
                                ((codePoint >= 0x10000) &&
                                 (codePoint <= 0x10FFFF));

            // 如果不能匹配，则该字符是表情符号
            if (!isContain) {
                return true;
            }
        }

        return false;
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();

        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    public void hideSoftKeyboard(){
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			setInputType(InputType.TYPE_NULL);
		}
		else {
			try {
				((Activity)getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

				Class<EditText> cls = EditText.class;
				Method setSoftInputShownOnFocus;
				setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
				setSoftInputShownOnFocus.setAccessible(true);
				setSoftInputShownOnFocus.invoke(this, false);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		//不显示光标
		setCursorVisible(false);
	}
}
