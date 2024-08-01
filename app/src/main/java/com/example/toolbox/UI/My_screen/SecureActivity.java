package com.example.toolbox.UI.My_screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolbox.Adapter.SecureAdapter;
import com.example.toolbox.Model.SecureListModel;
import com.example.toolbox.R;
import com.example.toolbox.UI.Secure_ui.LogoutActivity;
import com.example.toolbox.UI.Secure_ui.AccountStatusActivity;
import com.example.toolbox.UI.Secure_ui.EnvironmentCheckActivity;
import com.example.toolbox.UI.Secure_ui.Safety_and_AdviceActivity;


import java.util.ArrayList;
import java.util.List;

public class SecureActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSettings;
    private SecureAdapter settingAdapter;
    private List<SecureListModel> settingItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure);
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
        settingAdapter = new SecureAdapter(settingItemList, new SecureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 处理列表项点击事件
                String itemTitle = settingItemList.get(position).getTitle();
                Intent intent;


                switch (position) {
                    case 0:
                        intent = new Intent(SecureActivity.this, EnvironmentCheckActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        intent = new Intent(SecureActivity.this, AccountStatusActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(SecureActivity.this, LogoutActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(SecureActivity.this, Safety_and_AdviceActivity.class);
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
    private List<SecureListModel> createSettingItemList() {
        List<SecureListModel> list = new ArrayList<>();
        list.add(new SecureListModel("手机环境检测", R.drawable.ic_apply));
        list.add(new SecureListModel("账号活动状态", R.drawable.ic_apply));
        list.add(new SecureListModel("账号注销", R.drawable.ic_apply));
        list.add(new SecureListModel("安全与建议", R.drawable.ic_apply));


        // 如果需要，可以添加更多的列表项
        return list;
    }
}
