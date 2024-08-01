package com.example.toolbox.UI.Fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toolbox.Adapter.ThreeListAdapter;
import com.example.toolbox.Model.ThreeListModel;
import com.example.toolbox.R;

import java.util.ArrayList;
import java.util.List;

public class ThreeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ThreeListAdapter adapter;
    private List<ThreeListModel> dataList;
    private TextView noDownloadsTextView;

    // 创建一个新的ThreeFragment实例
    public static ThreeFragment newInstance(String param1, String param2) {
        ThreeFragment fragment = new ThreeFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        // 初始化视图组件
        recyclerView = view.findViewById(R.id.recycler_view_downloads);
        noDownloadsTextView = view.findViewById(R.id.no_downloads_text_view);

        // 设置RecyclerView的布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 初始化数据
        initData();

        return view;
    }

    // 初始化数据列表
    private void initData() {
        dataList = new ArrayList<>();

        // 获取下载管理器实例
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(DownloadManager.STATUS_PENDING); // 设置过滤条件为正在进行的下载任务

        // 查询正在运行的下载任务
        Cursor cursor = downloadManager.query(query);
        if (cursor != null && cursor.moveToFirst()) {
            int uriColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_URI);
            int titleColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE);
            int totalSizeColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);

            do {
                // 获取下载信息
                String uriString = cursor.getString(uriColumnIndex);
                String appName = cursor.getString(titleColumnIndex);
                Drawable appIcon = getActivity().getPackageManager().getDefaultActivityIcon(); // 默认图标

                // 通过Uri获取包名和应用名
                String packageName = null;
                try {
                    Uri uri = Uri.parse(uriString);
                    packageName = getActivity().getPackageManager().getNameForUid(getActivity().getPackageManager().getApplicationInfo(uri.getHost(), 0).uid);
                    appName = getActivity().getPackageManager().getApplicationLabel(getActivity().getPackageManager().getApplicationInfo(packageName, 0)).toString();
                    appIcon = getActivity().getPackageManager().getApplicationIcon(packageName);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 获取文件大小
                long bytesTotal = cursor.getLong(totalSizeColumnIndex);
                String appSize = formatSize(bytesTotal);

                // 将下载信息添加到数据列表中
                dataList.add(new ThreeListModel(appIcon, appName, appSize));
            } while (cursor.moveToNext());

            cursor.close();

            // 更新UI
            recyclerView.setVisibility(View.VISIBLE);
            noDownloadsTextView.setVisibility(View.GONE);
            adapter = new ThreeListAdapter(getActivity(), dataList);
            recyclerView.setAdapter(adapter);
        } else {
            // 如果没有下载任务，则显示提示文本
            noDownloadsTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    // 格式化文件大小
    private String formatSize(long sizeInBytes) {
        if (sizeInBytes <= 0) return "0 B";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(sizeInBytes) / Math.log10(1024));

        return String.format("%.2f %s", sizeInBytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }
}
