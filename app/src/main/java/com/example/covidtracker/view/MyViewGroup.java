package com.example.covidtracker.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.covidtracker.R;

public class MyViewGroup extends CoordinatorLayout {

    private boolean isEnabled = true;

    public MyViewGroup(@NonNull Context context) {
        super(context);
    }

    public MyViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !isEnabled;
    }

    public void setChildrenEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isEnabled) {
                setForeground(new ColorDrawable(getContext().getColor(R.color.screen_freeze_color)));

            } else {
                setForeground(new ColorDrawable(getContext().getColor(android.R.color.transparent)));
            }
        }
    }

}
