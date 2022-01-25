package com.lg_project.modelclass;

import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;

public class KeyboardHelper {

    public interface KeyboardListener {

        void onShowKeyboard();

        void onHideKeyboard();
    }

    public static ViewTreeObserver.OnGlobalLayoutListener setKeyboardListener(
            @NonNull View rootChildView, @NonNull KeyboardListener listener) {
        return () -> {
            int bottomPadding = rootChildView.getPaddingBottom();
            if (bottomPadding > 0) {
                listener.onShowKeyboard();
            } else {
                listener.onHideKeyboard();
            }
        };
    }
}