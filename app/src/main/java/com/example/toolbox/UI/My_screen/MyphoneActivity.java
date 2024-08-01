package com.example.toolbox.UI.My_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.toolbox.R;

public class MyphoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myphone);
        //返回按钮
        ImageView backButton = findViewById(R.id.imageView_back);

        // 设置返回按钮点击事件监听器
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 在此处添加返回按钮点击后的操作
                finish(); // 结束当前活动，返回上一个活动或主屏幕
            }
        });
    }
}