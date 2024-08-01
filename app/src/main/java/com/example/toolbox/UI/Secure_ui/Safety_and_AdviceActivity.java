package com.example.toolbox.UI.Secure_ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.Spannable;

import androidx.appcompat.app.AppCompatActivity;

import com.example.toolbox.R;

public class Safety_and_AdviceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_and_advice);

        // 找到返回按钮
        ImageView backButton = findViewById(R.id.imageView_back);

        // 设置返回按钮点击事件监听器
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在点击返回按钮时执行的操作
                finish(); // 关闭当前界面，返回上一个界面
            }
        });
    }
}
