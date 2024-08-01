package com.example.toolbox.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toolbox.Model.PermissionsModel;
import com.example.toolbox.R;

import java.util.List;

public class PermissionsAdapter extends RecyclerView.Adapter<PermissionsAdapter.PermissionViewHolder> {

    private List<PermissionsModel> permissionList;

    public PermissionsAdapter(List<PermissionsModel> permissionList) {
        this.permissionList = permissionList;
    }

    @NonNull
    @Override
    public PermissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 创建ViewHolder并绑定到布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_permissions, parent, false);
        return new PermissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PermissionViewHolder holder, int position) {
        // 将数据绑定到ViewHolder
        PermissionsModel permission = permissionList.get(position);
        holder.bind(permission);
    }

    @Override
    public int getItemCount() {
        return permissionList.size();
    }

    public static class PermissionViewHolder extends RecyclerView.ViewHolder {

        private TextView textPermissionName;
        private Switch switchPermission;

        public PermissionViewHolder(@NonNull View itemView) {
            super(itemView);
            // 初始化视图组件
            textPermissionName = itemView.findViewById(R.id.text_permission_name);
            switchPermission = itemView.findViewById(R.id.switch_permission);
        }

        public void bind(final PermissionsModel permission) {
            // 将数据绑定到视图组件
            textPermissionName.setText(permission.getPermissionName());
            switchPermission.setChecked(permission.isEnabled());

            // 设置开关按钮的监听器
            switchPermission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    permission.setEnabled(isChecked);
                }
            });
        }
    }
}
