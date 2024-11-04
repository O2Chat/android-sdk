package com.arittek.signalrtestandroid.commons;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

public class KeyboardVisibilityUtils {

    public interface KeyboardVisibilityListener {
        void onKeyboardVisibilityChanged(boolean isVisible);
    }

    public static void setKeyboardVisibilityListener(Activity activity, KeyboardVisibilityListener listener) {
        final View contentView = activity.findViewById(android.R.id.content);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private boolean wasOpened = false;
            
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                boolean isOpen = keypadHeight > screenHeight * 0.15; // 0.15 ratio is a common threshold for keyboard height
                if (isOpen != wasOpened) {
                    wasOpened = isOpen;
                    listener.onKeyboardVisibilityChanged(isOpen);
                }
            }
        });
    }
}
