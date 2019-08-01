package com.zlin.buglytest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class NewActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newactivity);
    }
}
