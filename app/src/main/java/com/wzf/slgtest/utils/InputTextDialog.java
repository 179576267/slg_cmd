package com.wzf.slgtest.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


import com.wzf.slgtest.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * @author YOUNG
 * @date 2017-05-02
 */

public abstract class InputTextDialog extends Dialog {

    private final Context context;
    @Bind(R.id.et_input_text)
    EditText etInputText;
    @Bind(R.id.tv_confirm_input)
    TextView tvConfirmInput;
    private String hint;

    public InputTextDialog(Context context, String s) {
        super(context, R.style.InputTextTheme);
        this.context = context;
        this.hint = s;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setContentView(R.layout.dialog_text_input);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.CENTER;
        attributes.width = ScreenUtils.getScreenWidth(context);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        etInputText.setHint(hint);
    }

    @OnClick(R.id.tv_confirm_input)
    public void onClick() {
        if (etInputText.getText().toString().length() > 0) {
            sendText(etInputText.getText().toString().replace(" ", ""));
            dismiss();
        }
    }

    abstract public void sendText(String text);

    @Override
    public void onBackPressed() {
        dismiss();
    }
}
