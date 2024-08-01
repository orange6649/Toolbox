package com.example.toolbox.UI.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.text.InputType;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.toolbox.Adapter.OneListAdapter;
import com.example.toolbox.Model.OneListModel;
import com.example.toolbox.R;
import android.util.Log;
import android.widget.Toast;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import android.view.inputmethod.EditorInfo;


public class OneFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout; // 下拉刷新布局
    private RecyclerView recyclerView; // RecyclerView显示应用列表
    private EditText searchEditText; // 搜索框
    private OneListAdapter adapter; // RecyclerView适配器
    private List<OneListModel> appList; // 应用列表
    private List<OneListModel> searchResultList; // 搜索结果列表

    // 创建并返回一个 OneFragment 实例
    public static OneFragment newInstance(String param1, String param2) {
        OneFragment oneFragment = new OneFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        oneFragment.setArguments(args);
        return oneFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchResultList = new ArrayList<>(); // 初始化搜索结果列表
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加载布局文件
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        searchEditText = view.findViewById(R.id.search_edit_text); // 找到搜索框

        // 初始化下拉刷新布局
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        // 初始化RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);

        // 设置下拉刷新时的颜色
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark);

        // 设置下拉刷新监听器
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        // 初始化应用列表
        appList = new ArrayList<>();
        // 获取并展示APK文件列表
        fetchApkFiles();
        // 设置RecyclerView
        setupRecyclerView();
        // 在 onCreateView 方法内添加 TextWatcher
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                filter(keyword);
            }
        });


        searchEditText.setInputType(InputType.TYPE_CLASS_TEXT); // 设置输入类型为文本
        searchEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH); // 设置软键盘上的回车按钮为搜索按钮

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filter(searchEditText.getText().toString());
                return true;
            }
            return false;
        });

        return view;
    }



    // 根据关键词过滤应用列表
    private void filter(String keyword) {
        searchResultList.clear(); // 清空搜索结果列表
        if (keyword.isEmpty()) {
            searchResultList.addAll(appList); // 如果关键词为空，则显示所有应用
        } else {
            keyword = keyword.toLowerCase(); // 将关键词转换为小写以进行不区分大小写的搜索
            for (OneListModel app : appList) {
                String appName = app.getName().toLowerCase();
                String packageName = app.getPackageName().toLowerCase();
                if (appName.contains(keyword) || packageName.contains(keyword)) {
                    searchResultList.add(app); // 将匹配的应用添加到搜索结果列表中
                }
            }
        }
        adapter.updateList(searchResultList); // 更新 RecyclerView 显示搜索结果
    }





    // 获取设备上的APK文件列表，过滤掉系统应用和特定品牌的自带应用
    private void fetchApkFiles() {
        appList.clear(); // 清空列表
        PackageManager packageManager = requireContext().getPackageManager();

        // 获取已安装的应用程序列表
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        // 获取设备制造商信息
        String manufacturer = Build.MANUFACTURER.toLowerCase();

        for (ApplicationInfo appInfo : installedApps) {
            // 检查应用程序是否为用户应用程序（非系统应用程序）
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                // 根据设备制造商信息来过滤自带应用程序
                if (!appInfo.packageName.startsWith("com." + manufacturer + ".") &&
                        !appInfo.packageName.startsWith("com.android.")) {
                    String appName = packageManager.getApplicationLabel(appInfo).toString();
                    String packageName = appInfo.packageName;
                    Drawable appIcon = packageManager.getApplicationIcon(appInfo);
                    String apkFilePath = appInfo.sourceDir;
                    appList.add(new OneListModel(appName, packageName, appIcon, apkFilePath));
                }
            }
        }
    }




    // 设置RecyclerView和适配器
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OneListAdapter(getContext(), appList);
        // 设置RecyclerView条目点击事件监听器
        adapter.setOnItemClickListener(position -> openAppInfo(appList.get(position).getPackageName()));
        recyclerView.setAdapter(adapter);
    }


    // 刷新数据
    private void refreshData() {
        fetchApkFiles(); // 重新获取APK文件列表
        adapter.notifyDataSetChanged(); // 通知适配器数据已更新

        // 设置刷新动画颜色
        int[] colors = {
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark
        };
        swipeRefreshLayout.setColorSchemeResources(colors);

        // 延迟1秒后停止刷新动画
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false); // 停止刷新动画
                // 获取应用列表的大小
                int appCount = appList.size();
                // 显示提示信息
                Toast.makeText(getContext(), "刷新成功，应用个数为：" + appCount, Toast.LENGTH_SHORT).show();
            }
        }, 1000);
    }



    // 打开应用程序信息页面
    private void openAppInfo(String packageName) {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        } catch (Exception e) {
            Log.e("OpenAppInfo", "无法打开应用信息 " + e.getMessage());
            Toast.makeText(getContext(), "无法打开应用信息", Toast.LENGTH_SHORT).show();
        }
    }


}
