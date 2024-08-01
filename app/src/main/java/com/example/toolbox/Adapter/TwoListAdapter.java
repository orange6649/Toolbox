package com.example.toolbox.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toolbox.Model.TwoListModel;
import com.example.toolbox.R;



import java.util.List;

public class TwoListAdapter extends RecyclerView.Adapter<TwoListAdapter.AppViewHolder> {

    private Context context;
    private List<TwoListModel> appList;

    public TwoListAdapter(Context context, List<TwoListModel> appList) {
        this.context = context;
        this.appList = appList;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 创建ViewHolder并绑定对应的布局文件
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_two, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        // 将数据绑定到ViewHolder中的视图
        TwoListModel app = appList.get(position);
        holder.appName.setText(app.getName());
        holder.appPackage.setText(app.getPackageName());
        holder.appIcon.setImageDrawable(app.getIcon()); // 设置应用图标
        //String serialNumberText = context.getString(R.string.serial_number_format, position + 1);//序号+1
        //holder.serialNumberTextView.setText(serialNumberText);//序号后的点

        // 检查应用是否已安装
        boolean isInstalled = isAppInstalled(app.getPackageName());

        // 根据安装状态设置按钮文本和文本颜色
        if (isInstalled) {
            holder.installButton.setText("已安装");
            // 设置按钮文本颜色为浅红色

        } else {
            holder.installButton.setText("安装");
        }



        // 设置安装按钮点击事件监听器
        holder.installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取应用包名
                String packageName = app.getPackageName();
                // 创建跳转到应用商店的意图
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + packageName));
                // 检查是否有应用能够处理这个意图
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    // 如果有应用能够处理，则启动应用商店
                    context.startActivity(intent);
                } else {
                    // 如果没有应用能够处理，则显示提示信息
                    Toast.makeText(context, "未找到应用商店", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 检查应用是否已安装
    private boolean isAppInstalled(String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }



    @Override
    public int getItemCount() {
        // 返回列表项数目
        return appList.size();
    }

    // 更新列表数据的方法
    public void updateList(List<TwoListModel> searchResultList) {
        appList.clear(); // 清除原始列表
        appList.addAll(searchResultList); // 将搜索结果添加到列表中
        notifyDataSetChanged(); // 通知RecyclerView更新显示
    }

    // ViewHolder类，用于缓存item布局中的视图
    public static class AppViewHolder extends RecyclerView.ViewHolder {
        TextView appName;
        TextView appPackage;
        ImageView appIcon;
        TextView serialNumberTextView; // 序号TextView
        TextView installButton; // 安装按钮TextView

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            // 初始化ViewHolder中的视图
            appName = itemView.findViewById(R.id.app_name);
            appPackage = itemView.findViewById(R.id.app_package);
            appIcon = itemView.findViewById(R.id.app_icon);
            //serialNumberTextView = itemView.findViewById(R.id.serial_number);
            installButton = itemView.findViewById(R.id.install_button);
        }
    }
}
