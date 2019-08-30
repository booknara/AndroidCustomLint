package com.booknara.android.apps.lint.viewmodel;

import androidx.lifecycle.ViewModel;

public class HelloWorldViewModel extends ViewModel {
    private String mGreeting = "Hello World";

    public String getGreeting() {
        return mGreeting;
    }
}
