package com.example.toolbox.UI.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toolbox.Adapter.TwoListAdapter;
import com.example.toolbox.Model.TwoListModel;
import com.example.toolbox.R;
import com.example.toolbox.util.PackageNames;

import android.content.pm.ApplicationInfo;


import java.util.ArrayList;
import java.util.List;

public class TwoFragment extends Fragment {

    private RecyclerView recyclerView;
    private TwoListAdapter adapter;
    private List<TwoListModel> appList;
    private List<TwoListModel> originalAppList; // 用于保存原始应用列表
    private List<TwoListModel> searchResultList; // 用于保存搜索结果列表

    // 创建并返回一个 TwoFragment 实例
    public static TwoFragment newInstance(String param1, String param2) {
        TwoFragment twoFragment = new TwoFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        twoFragment.setArguments(args);
        return twoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        originalAppList = new ArrayList<>();
        appList = new ArrayList<>();
        searchResultList = new ArrayList<>(); // 初始化搜索结果列表
        fetchAllApps(); // 获取应用商店中的应用列表

    }


    // 在 onCreateView 方法中添加监听安装按钮点击事件的代码
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 将布局文件 fragment_two.xml 填充为视图
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        // 初始化 RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        setupRecyclerView(); // 设置RecyclerView
        // 获取搜索框
        EditText searchEditText = view.findViewById(R.id.search_edit_text);
        // 设置搜索框监听器，当按下回车键时执行搜索操作
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String searchText = ((EditText) v).getText().toString().trim();
                    performSearch(searchText); // 执行搜索操作
                    return true;
                }
                return false;
            }
        });

        // 获取进入应用商店按钮
        ImageButton appStoreButton = view.findViewById(R.id.button_select_all);
        // 设置点击事件监听器，点击按钮进入应用商店
        appStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取搜索框内容
                String searchText = searchEditText.getText().toString().trim();
                if (searchText.isEmpty()) {
                    // 如果搜索框内容为空，直接打开应用商店首页
                    openAppStoreHomePage();
                } else {
                    // 如果搜索框内容不为空，执行应用商店搜索
                    searchInAppStore(searchText);
                }
            }
        });

        // 获取刷新按钮
        ImageButton refreshButton = view.findViewById(R.id.button_refresh);
        // 设置点击事件监听器，点击按钮刷新应用列表
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 延时1秒后执行刷新操作
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 点击刷新按钮重新获取原始数据
                        appList.clear();
                        appList.addAll(originalAppList);
                        adapter.notifyDataSetChanged(); // 通知 RecyclerView 数据已更改

                    }
                }, 100); // 延时0.1秒
            }
        });

        return view; // 返回视图
    }




    // 执行搜索操作
    private void performSearch(String searchText) {
        searchResultList.clear();
        for (TwoListModel app : originalAppList) {
            if (app.getAppName().toLowerCase().contains(searchText.toLowerCase())) {
                searchResultList.add(app);
            }
        }
        adapter.updateList(searchResultList); // 更新列表显示搜索结果
    }

    // 打开应用商店首页
    private void openAppStoreHomePage() {
        // 创建启动应用商店的意图
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 设置 URI 为应用商店的详情页面
        intent.setData(Uri.parse("market://details?id=" + getContext().getPackageName()));
        // 检查是否有应用商店应用能够处理这个意图
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent); // 打开应用商店
        } else {
            Toast.makeText(getContext(), "未找到应用商店", Toast.LENGTH_SHORT).show();
        }
    }

    // 在应用商店搜索应用
    private void searchInAppStore(String searchText) {
        // 创建启动应用商店的意图
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 设置 URI 为应用商店的搜索页面，并传入搜索关键词
        intent.setData(Uri.parse("market://search?q=" + searchText));
        // 检查是否有应用商店应用能够处理这个意图
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent); // 打开应用商店搜索页面
        } else {
            Toast.makeText(getContext(), "未找到应用商店", Toast.LENGTH_SHORT).show();
        }
    }

    // 获取手机上所有已安装和未安装的应用列表
    private void fetchAllApps() {
        PackageManager packageManager = getContext().getPackageManager();
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(0); // 获取所有已安装的应用
        List<String> desiredPackageNames = new ArrayList<>(PackageNames.getDesiredPackageNames()); // 获取您想要添加的应用包名列表并转换为集合

        for (ApplicationInfo appInfo : installedApps) {
            String packageName = appInfo.packageName;
            // 检查是否为您想要添加的应用包名列表中的应用
            if (desiredPackageNames.contains(packageName)) {
                String appName = packageManager.getApplicationLabel(appInfo).toString();
                Drawable appIcon = packageManager.getApplicationIcon(appInfo);
                TwoListModel appModel = new TwoListModel(appName, packageName, appIcon);
                appList.add(appModel);
                originalAppList.add(appModel); // 保存原始应用列表数据
                desiredPackageNames.remove(packageName); // 从列表中移除已经找到的应用包名，减少后续循环次数
            }
        }

        // 对于未安装的应用，尝试获取其应用信息，如果获取到了，则添加到列表中
        for (String packageName : desiredPackageNames) {
            try {
                ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
                String appName = packageManager.getApplicationLabel(appInfo).toString();
                Drawable appIcon = packageManager.getApplicationIcon(appInfo);
                TwoListModel appModel = new TwoListModel(appName, packageName, appIcon);
                appList.add(appModel);
                originalAppList.add(appModel); // 保存原始应用列表数据
            } catch (PackageManager.NameNotFoundException ignored) {
                // 如果应用信息无法获取，则忽略该异常
            }
        }
    }


    // 设置 RecyclerView 的布局和适配器
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TwoListAdapter(getContext(), appList);
        recyclerView.setAdapter(adapter);
    }

    // 检查应用是否已安装
    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getContext().getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
