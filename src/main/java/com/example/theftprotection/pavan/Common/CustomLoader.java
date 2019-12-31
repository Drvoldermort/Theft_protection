package com.example.pavan.theftprotection.Common;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.example.pavan.theftprotection.R;
public class CustomLoader extends Dialog {
    public CustomLoader(@NonNull Context context) {
        super(context);
    }

    public CustomLoader(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.custom_loader);
    }

    public CustomLoader(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
