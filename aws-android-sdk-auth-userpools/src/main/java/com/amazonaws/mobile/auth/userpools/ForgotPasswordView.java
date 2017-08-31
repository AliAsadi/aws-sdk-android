/*
  * Copyright 2013-2017 Amazon.com, Inc. or its affiliates.
  * All Rights Reserved.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package com.amazonaws.mobile.auth.userpools;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.amazonaws.mobile.config.AWSConfiguration;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.signin.SignInManager;
import com.amazonaws.mobile.auth.core.signin.ui.DisplayUtils;
import com.amazonaws.mobile.auth.core.signin.ui.SplitBackgroundDrawable;

import com.amazonaws.mobile.auth.userpools.R;

import static com.amazonaws.mobile.auth.userpools.UserPoolFormConstants.FORM_BUTTON_COLOR;
import static com.amazonaws.mobile.auth.userpools.UserPoolFormConstants.FORM_BUTTON_CORNER_RADIUS;
import static com.amazonaws.mobile.auth.userpools.UserPoolFormConstants.FORM_SIDE_MARGIN_RATIO;
import static com.amazonaws.mobile.auth.userpools.UserPoolFormConstants.MAX_FORM_WIDTH_IN_PIXELS;

/**
 * This view present the ForgotPassword screen for the user to reset the
 * password.
 */
public class ForgotPasswordView extends LinearLayout {
    private FormView forgotPassForm;
    private EditText verificationCodeEditText;
    private EditText passwordEditText;

    private SplitBackgroundDrawable splitBackgroundDrawable;

    /**
     * Constructs the ForgotPassword View.
     * @param context The activity context.
     */
    public ForgotPasswordView(final Context context) {
        this(context, null);
    }

    /**
     * Constructs the ForgotPassword View.
     * @param context The activity context.
     * @param attrs The Attribute Set for the view from which the resources can be accessed.
     */
    public ForgotPasswordView(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructs the ForgotPassword View.
     * @param context The activity context.
     * @param attrs The Attribute Set for the view from which the resources can be accessed.
     * @param defStyleAttr The resource identifier for the default style attribute.
     */
    public ForgotPasswordView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);

        final int backgroundColor;
        if (isInEditMode()) {
            backgroundColor = Color.DKGRAY;
        } else {
            final TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ForgotPasswordView);
            backgroundColor = styledAttributes.getInt(R.styleable.ForgotPasswordView_forgotPasswordViewBackgroundColor, 
                                                      Color.DKGRAY);
            styledAttributes.recycle();
        }

        splitBackgroundDrawable = new SplitBackgroundDrawable(0, getBackgroundColor(context, backgroundColor));
    }

    private int getBackgroundColor(final Context context, final int defaultBackgroundColor) {
        Intent intent = ((Activity) context).getIntent();
        return (int) (intent.getIntExtra(CognitoUserPoolsSignInProvider.AttributeKeys.BACKGROUND_COLOR,
                                             defaultBackgroundColor));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        forgotPassForm = (FormView) findViewById(R.id.forgot_password_form);

        verificationCodeEditText = forgotPassForm.addFormField(getContext(),
            InputType.TYPE_CLASS_NUMBER,
            getContext().getString(R.string.sign_up_confirm_code));

        passwordEditText = forgotPassForm.addFormField(getContext(),
            InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD,
            getContext().getString(R.string.sign_in_password));

        setupConfirmButtonColor();
    }

    private void setupConfirmButtonColor() {
        final Button confirmButton = (Button) findViewById(R.id.forgot_password_button);
        confirmButton.setBackgroundDrawable(DisplayUtils.getRoundedRectangleBackground(
            FORM_BUTTON_CORNER_RADIUS, FORM_BUTTON_COLOR));
        LayoutParams signUpButtonLayoutParams = (LayoutParams) confirmButton.getLayoutParams();
        signUpButtonLayoutParams.setMargins(
            forgotPassForm.getFormShadowMargin(),
            signUpButtonLayoutParams.topMargin,
            forgotPassForm.getFormShadowMargin(),
            signUpButtonLayoutParams.bottomMargin);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int maxWidth = Math.min((int)(parentWidth * FORM_SIDE_MARGIN_RATIO), MAX_FORM_WIDTH_IN_PIXELS);
        super.onMeasure(MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST), heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setupSplitBackground();
    }

    private void setupSplitBackground() {
        splitBackgroundDrawable.setSplitPointDistanceFromTop(forgotPassForm.getTop()
            + (forgotPassForm.getMeasuredHeight()/2));
        ((ViewGroup) getParent()).setBackgroundDrawable(splitBackgroundDrawable);
    }

    public String getVerificationCode() {
        return verificationCodeEditText.getText().toString();
    }

    public String getPassword() {
        return passwordEditText.getText().toString();
    }
}
