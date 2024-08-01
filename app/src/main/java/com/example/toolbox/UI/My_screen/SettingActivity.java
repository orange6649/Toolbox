package com.example.toolbox.UI.My_screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolbox.Adapter.SettingAdapter;
import com.example.toolbox.Model.SettingListModel;
import com.example.toolbox.UI.Setting_ui.AboutActivity;
import com.example.toolbox.UI.Setting_ui.ClearCacheActivity;
import com.example.toolbox.UI.Setting_ui.Dark_ModelActivity;
import com.example.toolbox.UI.Setting_ui.PermissionsActivity;
import com.example.toolbox.UI.Setting_ui.UpdateActivity;
import com.example.toolbox.R;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSettings;
    private SettingAdapter settingAdapter;
    private List<SettingListModel> settingItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //返回按钮
        ImageView backButton = findViewById(R.id.imageView_back);

        // 初始化 RecyclerView
        recyclerViewSettings = findViewById(R.id.recycler_view_settings);
        recyclerViewSettings.setLayoutManager(new LinearLayoutManager(this));

        // 创建设置项列表
        settingItemList = createSettingItemList();

        // 设置返回按钮点击事件监听器
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 在此处添加返回按钮点击后的操作
                finish(); // 结束当前活动，返回上一个活动或主屏幕
            }
        });

        // 创建并设置适配器
        settingAdapter = new SettingAdapter(settingItemList, new SettingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 处理列表项点击事件
                String itemTitle = settingItemList.get(position).getTitle();
                Intent intent;

                // 根据点击位置决定打开不同的活动
                switch (position) {
                    case 0:
                        intent = new Intent(SettingActivity.this, Dark_ModelActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(SettingActivity.this, PermissionsActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(SettingActivity.this, ClearCacheActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(SettingActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(SettingActivity.this, UpdateActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        // 设置 RecyclerView 的适配器
        recyclerViewSettings.setAdapter(settingAdapter);
    }

    // 创建设置项列表的方法
    private List<SettingListModel> createSettingItemList() {
        List<SettingListModel> list = new ArrayList<>();
        list.add(new SettingListModel("深色模式", R.drawable.ic_apply));
        list.add(new SettingListModel("系统权限管理", R.drawable.ic_apply));
        list.add(new SettingListModel("清理缓存", R.drawable.ic_apply));
        list.add(new SettingListModel("关于与帮助", R.drawable.ic_apply));
        list.add(new SettingListModel("检查更新", R.drawable.ic_apply));

        // 如果需要，可以添加更多的列表项
        return list;
    }
}
