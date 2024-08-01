package com.example.toolbox.UI.Secure_ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.toolbox.R;
import androidx.appcompat.app.AppCompatActivity;

public class LogoutActivity extends AppCompatActivity {

    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        //返回按钮
        ImageView backButton = findViewById(R.id.imageView_back);

        // 设置返回按钮点击事件监听器
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 在此处添加返回按钮点击后的操作
                finish(); // 结束当前活动，返回上一个活动或主屏幕
            }
        });
        // 初始化视图
        initView();

        // 设置确认注销按钮点击事件
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutAccount();
            }
        });
    }

    // 初始化视图
    private void initView() {
        buttonLogout = findViewById(R.id.buttonLogout);
    }

    // 处理注销账号逻辑
    private void logoutAccount() {
        // 执行注销账号的逻辑，这里只是简单的示例，可以根据实际需求添加逻辑

        // 显示注销成功信息
        Toast.makeText(this, "账号已注销", Toast.LENGTH_SHORT).show();
        // 这里可以添加其他操作，例如清除用户信息、跳转到登录页面等
    }
}
