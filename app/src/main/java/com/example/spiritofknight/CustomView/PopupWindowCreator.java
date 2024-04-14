package com.example.spiritofknight.CustomView;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.spiritofknight.R;


public class PopupWindowCreator{
    private static PopupWindow popupWindow;
    private Runnable runnable;
    private Handler uiHandler;

    public  PopupWindow createaPopupWindow(Context context, String display_content, Activity activity) {
        // 加载布局文件
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.custom_layout, null);
        View contentView = activity.getWindow().getDecorView().getRootView();
        int gravity = Gravity.CENTER;

// 创建PopupWindow
        if(popupWindow==null){
            popupWindow = new PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            // 设置PopupWindow的一些属性
            popupWindow.setFocusable(true); // 让PopupWindow获得焦点，可以处理事件
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 设置背景，这里设为透明
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE)); // 设置背景，这里设为透明
            popupWindow.setOutsideTouchable(true); // 点击外部区域可以关闭PopupWindow
//            this.autocloseButtonFeature(1);//自动关闭弹出窗口

            if (display_content!=null){
                TextView textView = popupView.findViewById(R.id.text_view);
                textView.setText(display_content);
            }
        }
        popupWindow.showAtLocation(contentView, gravity, 0, 0);
        return popupWindow;
    }

    public  void autocloseButtonFeature(int second) {

        uiHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
                switch (msg.what){
                    case 1: popupWindow.dismiss();//close my popupwindow
                            break;
                }
            }
        };
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                try {
                    Thread.sleep(second*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Log.d("StarPlatuin", "run: ");
//                popupWindow.dismiss();用Handler的handleMessage来替换在子线程中的直接对ui的操作
                uiHandler.sendEmptyMessage(1);
                Log.d("StarPlatuin", "end: ");
            }
        };
        new Thread(runnable).start();
    }

    public void closeWindow(){
        if (popupWindow!=null){
            popupWindow.dismiss();
        }
    }

}
