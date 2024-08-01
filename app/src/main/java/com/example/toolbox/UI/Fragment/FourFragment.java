package com.example.toolbox.UI.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toolbox.R;
import com.example.toolbox.Adapter.FourListAdapter;
import com.example.toolbox.Model.FourListModel;
import com.example.toolbox.UI.My_screen.MyappActivity;
import com.example.toolbox.UI.My_screen.MyphoneActivity;
import com.example.toolbox.UI.My_screen.NetworkActivity;
import com.example.toolbox.UI.My_screen.SecureActivity;
import com.example.toolbox.UI.My_screen.SettingActivity;
import com.example.toolbox.UI.My_screen.userinfoActivity;

import java.util.ArrayList;
import java.util.List;

public class FourFragment extends Fragment {

    private RecyclerView recyclerViewSettings;
    private FourListAdapter settingAdapter;
    private List<FourListModel> settingItemList;
    private ImageButton userinfoButton;

    public static FourFragment newInstance(String param1, String param2) {
        FourFragment fourFragment = new FourFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fourFragment.setArguments(args);
        return fourFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 为该片段充气布局
        View view = inflater.inflate(R.layout.fragment_four, container, false);

        // 初始化用户信息按钮
        userinfoButton = view.findViewById(R.id.userinfo);

        // 设置用户信息按钮的点击事件监听器
        userinfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), userinfoActivity.class);
                startActivity(intent);
            }
        });

        // 在布局中找到RecyclerView
        recyclerViewSettings = view.findViewById(R.id.recycler_view_settings);
        recyclerViewSettings.setLayoutManager(new LinearLayoutManager(getContext()));

        // 创建RecyclerView的项目列表
        settingItemList = createSettingItemList();

        // 创建RecyclerView的适配器
        settingAdapter = new FourListAdapter(settingItemList, new FourListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 获取点击项的标题
                String itemTitle = settingItemList.get(position).getTitle();

                // 初始化一个意图以启动相应的活动
                Intent intent;
                switch (position) {
                    case 0: // 点击了“我的应用”
                        intent = new Intent(getContext(), MyappActivity.class);
                        startActivity(intent);
                        break;
                    case 1: // 点击了“我的手机”
                        intent = new Intent(getContext(), MyphoneActivity.class);
                        startActivity(intent);
                        break;
                    case 2: // 点击了“网速测试”
                        intent = new Intent(getContext(), NetworkActivity.class);
                        startActivity(intent);
                        break;
                    case 3: // 点击了“安全中心”
                        intent = new Intent(getContext(), SecureActivity.class);
                        startActivity(intent);
                        break;
                    case 4: // 点击了“设置”
                        intent = new Intent(getContext(), SettingActivity.class);
                        startActivity(intent);
                        break;
                    // 添加更多情况来处理更多列表项
                    default:

                        break;
                }
            }
        });
        // 将适配器设置到RecyclerView
        recyclerViewSettings.setAdapter(settingAdapter);
        // 返回视图
        return view;
    }

    private List<FourListModel> createSettingItemList() {
        List<FourListModel> list = new ArrayList<>();
        list.add(new FourListModel(R.drawable.ic_apply, "我的应用"));
        list.add(new FourListModel(R.drawable.ic_mpc, "我的手机"));
        list.add(new FourListModel(R.drawable.ic_net, "网速测试"));
        list.add(new FourListModel(R.drawable.ic_secure, "安全中心"));
        list.add(new FourListModel(R.drawable.ic_setting, "设置"));
        // 添加更多列表选项
        return list;
    }
}
