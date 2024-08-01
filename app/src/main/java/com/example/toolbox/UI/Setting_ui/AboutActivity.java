package com.example.toolbox.UI.Setting_ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.toolbox.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abuot);

        // 设置布局并查找返回按钮
        ImageView backButton = findViewById(R.id.imageView_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // 获取版本号文本视图
        TextView versionTextView = findViewById(R.id.versionTextView);

        // 获取并设置版本号
        String versionName = getVersionName();
        versionTextView.setText("当前版本: " + versionName);
    }

    // 获取应用程序的版本号
    private String getVersionName() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "未知版本";
        }
    }
}
