package com.example.toolbox.UI.Setting_ui;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toolbox.R;

public class Dark_ModelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_model);

        // 设置布局并查找返回按钮
        ImageView backButton = findViewById(R.id.imageView_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // 设置开关按钮的监听器
        Switch switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 获取系统的护眼模式管理器
                UiModeManager uiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
                if (uiModeManager != null) {
                    // 根据开关状态设置护眼模式
                    if (isChecked) {
                        // 开启护眼模式
                        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
                    } else {
                        // 关闭护眼模式
                        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
                    }
                }
            }
        });
    }
}
