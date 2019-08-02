package com.zlin.buglytest;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.tencent.bugly.beta.Beta;
import com.zlin.buglytest.jpush.ExampleUtil;
import com.zlin.buglytest.jpush.LocalBroadcastManager;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_check;
    Button btn_down;
    Button btn_together;
    Button btn_restart;

    Button btn_new;
    Button btn_math;

    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_check = (Button)findViewById(R.id.btn_check);
        btn_down = (Button)findViewById(R.id.btn_down);
        btn_together = (Button)findViewById(R.id.btn_together);
        btn_restart = (Button)findViewById(R.id.btn_restart);
        btn_new = (Button)findViewById(R.id.btn_new);
        btn_math = (Button)findViewById(R.id.btn_math);
        btn_check.setOnClickListener(this);
        btn_down.setOnClickListener(this);
        btn_together.setOnClickListener(this);
        btn_restart.setOnClickListener(this);
        btn_new.setOnClickListener(this);
        btn_math.setOnClickListener(this);
        registerMessageReceiver();  // used for receive msg
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_check:
                Beta.checkUpgrade(true,true);
                break;
            case R.id.btn_down:
//                Beta.startDownload();
                Beta.downloadPatch();
                break;
            case R.id.btn_together:
                Beta.applyDownloadedPatch();
                break;
            case R.id.btn_restart:
                final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            case R.id.btn_new:
                Intent intent2 = new Intent(MainActivity.this,NewActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_math:
                showUpdate();
                break;
        }
    }

    private void showUpdate(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示");
        builder.setMessage("我是弹出的新方法");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        //需要把对话框的类型设为TYPE_SYSTEM_ALERT，否则对话框无法在广播接收器里弹出
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init(){
        JPushInterface.init(getApplicationContext());
        JPushInterface.resumePush(getApplicationContext());
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.zlin.buglytest.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    }

    private void setCostomMsg(String msg){
//        if (null != msgText) {
//            msgText.setText(msg);
//            msgText.setVisibility(android.view.View.VISIBLE);
//        }
    }
}
