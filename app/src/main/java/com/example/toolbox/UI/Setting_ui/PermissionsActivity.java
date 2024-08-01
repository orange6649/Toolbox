package com.example.toolbox.UI.Setting_ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toolbox.Adapter.PermissionsAdapter;
import com.example.toolbox.Model.PermissionsModel;
import com.example.toolbox.R;
import android.Manifest;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class PermissionsActivity extends AppCompatActivity {

    private static final int FINE_LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int COARSE_LOCATION_PERMISSION_REQUEST_CODE = 2;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        // 返回按钮
        ImageView backButton = findViewById(R.id.imageView_back);

        // 初始化RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_permissions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 获取SharedPreferences实例
        sharedPreferences = getSharedPreferences("permissions", MODE_PRIVATE);

        // 准备权限数据
        List<PermissionsModel> permissionList = preparePermissionData();

        // 创建并设置适配器
        PermissionsAdapter adapter = new PermissionsAdapter(permissionList);
        recyclerView.setAdapter(adapter);

        // 设置返回按钮点击事件监听器
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish(); // 结束当前活动，返回上一个活动或主屏幕
            }
        });

        // 恢复权限状态
        restorePermissionStates(permissionList);

        // 请求位置权限
        requestLocationPermissions();
    }

    // 准备权限数据的方法
    private List<PermissionsModel> preparePermissionData() {
        List<PermissionsModel> permissionList = new ArrayList<>();
        permissionList.add(new PermissionsModel("访问存储权限", sharedPreferences.getBoolean("storage_permission", false), "存储权限"));
        permissionList.add(new PermissionsModel("访问相机权限", sharedPreferences.getBoolean("camera_permission", false), "相机权限"));
        permissionList.add(new PermissionsModel("位置权限", sharedPreferences.getBoolean("location_permission", false), "位置权限"));
        permissionList.add(new PermissionsModel("悬浮窗", sharedPreferences.getBoolean("overlay_permission", false), "悬浮窗权限"));
        // 添加更多权限...
        return permissionList;
    }


    // 恢复权限状态
    private void restorePermissionStates(List<PermissionsModel> permissionList) {
        for (PermissionsModel permission : permissionList) {
            switch (permission.getName()) {
                case "访问存储权限":
                    permission.setEnabled(sharedPreferences.getBoolean("storage_permission", false));
                    break;
                case "访问相机权限":
                    permission.setEnabled(sharedPreferences.getBoolean("camera_permission", false));
                    break;
                case "位置权限":
                    permission.setEnabled(sharedPreferences.getBoolean("location_permission", false));
                    break;
                case "悬浮窗":
                    permission.setEnabled(sharedPreferences.getBoolean("overlay_permission", false));
                    break;
                // 添加更多权限...
            }
        }
    }



    // 请求位置权限的方法
    private void requestLocationPermissions() {
        // 检查是否已授予ACCESS_FINE_LOCATION权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 如果未授予，则请求ACCESS_FINE_LOCATION权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_LOCATION_PERMISSION_REQUEST_CODE);
        }

        // 检查是否已授予ACCESS_COARSE_LOCATION权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 如果未授予，则请求ACCESS_COARSE_LOCATION权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    COARSE_LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // 处理权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                // ACCESS_FINE_LOCATION权限已授予，相应地处理
                sharedPreferences.edit().putBoolean("location_permission", true).apply();
            } else {
                // ACCESS_FINE_LOCATION权限已拒绝，相应地处理
                sharedPreferences.edit().putBoolean("location_permission", false).apply();
            }
        } else if (requestCode == COARSE_LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // ACCESS_COARSE_LOCATION权限已授予，相应地处理
                sharedPreferences.edit().putBoolean("location_permission", true).apply();
            } else {
                // ACCESS_COARSE_LOCATION权限已拒绝，相应地处理
                sharedPreferences.edit().putBoolean("location_permission", false).apply();
            }
        }
    }
}
