package com.eeducationgo.markoupdrivers.util;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;


import com.eeducationgo.markoupdrivers.R;

import java.util.Objects;

public class ProgressBarDialog extends AlertDialog {

    private Context context;

    public static class Builder {

        private Context context;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public ProgressBarDialog build() {
            return new ProgressBarDialog(
                    context);
        }
    }

    public ProgressBarDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout mView = new LinearLayout(context);
        mView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mView.setGravity(Gravity.CENTER);
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        mView.addView(progressBar);
        setContentView(mView);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
