package com.example.toolbox.UI.My_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Context;
import android.app.ActivityOptions;


import com.example.toolbox.Adapter.MyappListAdapter;
import com.example.toolbox.Model.MyappListModel;
import com.example.toolbox.R;

import java.util.ArrayList;
import java.util.List;

public class MyappActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyappListAdapter adapter;
    private List<MyappListModel> dataList;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myapp);

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recycler_view_grid);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        // 初始化数据列表
        dataList = new ArrayList<>();
        loadInstalledApps(); // 加载已安装的应用
        adapter = new MyappListAdapter(this, dataList);
        recyclerView.setAdapter(adapter);

        // 设置返回按钮点击事件监听器
        ImageView backButton = findViewById(R.id.imageView_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一个活动或主屏幕
            }
        });
    }

    private void loadInstalledApps() {
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(0);

        for (ApplicationInfo appInfo : installedApps) {
            // 过滤系统应用
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                Drawable appIcon = appInfo.loadIcon(packageManager);
                String appName = appInfo.loadLabel(packageManager).toString();
                String packageName = appInfo.packageName; // 获取应用程序的包名
                MyappListModel model = new MyappListModel(appIcon, appName, packageName);
                dataList.add(model);
            }
        }
    }

    // 在 MyappListAdapter.java 中的 openApp 方法中添加判断是否是游戏的逻辑
    public void openApp(String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent launchIntent = packageManager.getLaunchIntentForPackage(packageName);
            if (launchIntent != null) {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                Bundle bundle = appInfo.metaData;
                if (bundle != null && bundle.containsKey("com.android.app.category.GAME")) {
                    // 如果是游戏，加速进入
                    Intent gameIntent = new Intent(launchIntent);
                    gameIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(context, "该应用为游戏", Toast.LENGTH_SHORT).show();

                    // 设置游戏启动时的动画效果，加快200%
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(context, R.anim.enter_animation, R.anim.exit_animation);
                    context.startActivity(gameIntent, options.toBundle());
                } else {
                    // 如果不是游戏，正常进入
                    context.startActivity(launchIntent);
                }
            } else {
                Toast.makeText(context, "无法打开应用", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "无法打开应用", Toast.LENGTH_SHORT).show();
        }
    }

}
