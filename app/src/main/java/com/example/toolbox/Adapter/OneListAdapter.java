package com.example.toolbox.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toolbox.Model.OneListModel;
import com.example.toolbox.R;

import java.util.List;

public class OneListAdapter extends RecyclerView.Adapter<OneListAdapter.AppViewHolder> {

    private Context context;
    private List<OneListModel> appList;
    private OnItemClickListener onItemClickListener; // 添加一个 OnItemClickListener 对象

    // 构造函数
    public OneListAdapter(Context context, List<OneListModel> appList) {
        this.context = context;
        this.appList = appList;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 创建 ViewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_one, parent, false);
        return new AppViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        // 绑定数据到 ViewHolder
        OneListModel app = appList.get(position);
        holder.appName.setText(app.getName());
        holder.appPackage.setText(app.getPackageName());
        holder.appIcon.setImageDrawable(app.getIcon()); // 使用 Drawable 对象设置图标

        // 设置序号
        int maxIndex = appList.size(); // 获取最大序号
        //String serialNumberText = getSerialNumberText(position, maxIndex);
        //holder.serialNumberTextView.setText(serialNumberText);
    }

    /*
    private String getSerialNumberText(int position, int maxIndex) {
        int maxDigits = (int) Math.log10(maxIndex) + 1; // 计算最大序号的位数
        String serialNumberText;
        if (maxDigits == 1) {
            // 如果最大序号只有一位，直接显示当前位置加1即可
            serialNumberText = (position + 1) + ".";
        } else {
            // 否则，显示当前位置加1的格式化字符串，保证位数一致，且使用空格替代数字前的0
            serialNumberText = String.format("%0" + maxDigits + "d.", position + 1);
            // 找到数字前的零，用空格替代
            serialNumberText = serialNumberText.replaceAll("0(?=[1-9])", " ");
        }
        return serialNumberText;
    }
*/


    @Override
    public int getItemCount() {
        // 返回列表项数目
        return appList.size();
    }

    // 设置点击监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // ViewHolder 类
    public class AppViewHolder extends RecyclerView.ViewHolder {
        TextView serialNumberTextView;
        ImageView appIcon;
        TextView appName;
        TextView appPackage;

        // 构造函数
        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            // 初始化视图
            //serialNumberTextView = itemView.findViewById(R.id.serial_number);
            appIcon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.app_name);
            appPackage = itemView.findViewById(R.id.app_package);

            // 设置条目点击监听器
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    // 确保位置有效且监听器不为空
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                        // 触发点击事件
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }

    // 更新列表数据
    public void updateList(List<OneListModel> newList) {
        appList.clear();
        appList.addAll(newList);
        notifyDataSetChanged();
    }

    // 定义一个接口，用于监听点击事件
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
