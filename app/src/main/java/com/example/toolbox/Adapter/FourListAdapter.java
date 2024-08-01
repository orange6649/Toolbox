package com.example.toolbox.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toolbox.Model.FourListModel;
import com.example.toolbox.R;

import java.util.List;

public class FourListAdapter extends RecyclerView.Adapter<FourListAdapter.SettingViewHolder> {

    private List<FourListModel> settingItemList; // 存储设置项的列表数据
    private OnItemClickListener listener; // 点击事件监听器接口

    // 定义点击事件监听器接口
    public interface OnItemClickListener {
        void onItemClick(int position); // 点击事件处理方法，传递点击的位置
    }

    // 构造函数，接受设置项列表数据和点击事件监听器
    public FourListAdapter(List<FourListModel> settingItemList, OnItemClickListener listener) {
        this.settingItemList = settingItemList;
        this.listener = listener;
    }

    // 创建ViewHolder，加载item_setting布局文件
    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_four, parent, false);
        return new SettingViewHolder(view);
    }

    // 绑定ViewHolder，设置数据到View
    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        FourListModel settingItem = settingItemList.get(position);
        holder.iconImageView.setImageResource(settingItem.getIcon()); // 设置图标
        holder.titleTextView.setText(settingItem.getTitle()); // 设置标题
    }

    // 返回设置项的数量
    @Override
    public int getItemCount() {
        return settingItemList.size();
    }

    // ViewHolder类，用于缓存item_setting布局中的控件
    class SettingViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView; // 图标控件
        TextView titleTextView; // 标题控件

        // 构造函数，初始化控件并设置点击事件监听器
        public SettingViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.icon); // 获取图标控件
            titleTextView = itemView.findViewById(R.id.title); // 获取标题控件

            // 设置点击事件监听器
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // 获取点击位置
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position); // 调用点击事件处理方法
                    }
                }
            });
        }
    }
}
