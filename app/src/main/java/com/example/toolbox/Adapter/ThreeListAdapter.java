package com.example.toolbox.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolbox.Model.ThreeListModel;
import com.example.toolbox.R;
import java.util.List;

public class ThreeListAdapter extends RecyclerView.Adapter<ThreeListAdapter.ThreeViewHolder> {

    private Context context;
    private List<ThreeListModel> dataList;

    public ThreeListAdapter(Context context, List<ThreeListModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ThreeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_three, parent, false);
        return new ThreeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreeViewHolder holder, int position) {
        ThreeListModel model = dataList.get(position);

        // 设置应用名称和大小
        holder.appNameTextView.setText(model.getAppName());
        holder.appSizeTextView.setText(model.getAppSize());

        // 设置应用图标
        Drawable appIcon = model.getAppIcon();
        if (appIcon != null) {
            holder.appIconImageView.setImageDrawable(appIcon);
        } else {
            // 如果没有图标，则设置一个默认图标或者隐藏图标视图
            holder.appIconImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ThreeViewHolder extends RecyclerView.ViewHolder {
        ImageView appIconImageView;
        TextView appNameTextView;
        TextView appSizeTextView;

        public ThreeViewHolder(@NonNull View itemView) {
            super(itemView);
            appIconImageView = itemView.findViewById(R.id.app_icon);
            appNameTextView = itemView.findViewById(R.id.app_name);
            appSizeTextView = itemView.findViewById(R.id.app_size);
        }
    }
}
