package com.example.toolbox.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toolbox.Model.MyappListModel;
import com.example.toolbox.R;

import java.util.List;

public class MyappListAdapter extends RecyclerView.Adapter<MyappListAdapter.ViewHolder> {

    private Context context;
    private List<MyappListModel> dataList;

    public MyappListAdapter(Context context, List<MyappListModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_myapp, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyappListModel model = dataList.get(position);
        holder.imageViewAppIcon.setImageDrawable(model.getAppIcon());
        holder.textViewAppName.setText(model.getAppName());

        // 设置按钮点击事件监听器
        holder.buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp(model.getPackageName());
            }
        });

        // 设置双击应用图标打开应用
        holder.imageViewAppIcon.setOnClickListener(new View.OnClickListener() {
            long lastClickTime = 0;

            @Override
            public void onClick(View v) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < 500) { // 如果两次点击间隔小于500ms，则认为是双击事件
                    openApp(model.getPackageName());
                }
                lastClickTime = clickTime;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAppIcon;
        TextView textViewAppName;
        Button buttonOpen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAppIcon = itemView.findViewById(R.id.imageView_app_icon);
            textViewAppName = itemView.findViewById(R.id.textView_app_name);
            buttonOpen = itemView.findViewById(R.id.button_open);
        }
    }

    // 打开应用
    private void openApp(String packageName) {
        try {
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (launchIntent != null) {
                context.startActivity(launchIntent);
            } else {
                Toast.makeText(context, "无法打开应用", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "无法打开应用", Toast.LENGTH_SHORT).show();
        }
    }
}
