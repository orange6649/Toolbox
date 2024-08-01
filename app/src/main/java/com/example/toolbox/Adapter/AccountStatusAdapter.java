package com.example.toolbox.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.toolbox.Model.AccountStatusModel;
import com.example.toolbox.R;
import java.util.List;

public class AccountStatusAdapter extends RecyclerView.Adapter<AccountStatusAdapter.ViewHolder> {
    private List<AccountStatusModel> loginHistoryList;

    public AccountStatusAdapter(List<AccountStatusModel> loginHistoryList) {
        this.loginHistoryList = loginHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AccountStatusModel loginInfo = loginHistoryList.get(position);
        holder.bind(loginInfo);
    }

    @Override
    public int getItemCount() {
        return loginHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDateTime;
        private TextView textViewPhoneModel;
        private TextView textViewLoginArea;
        private TextView textViewLoginStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
            textViewPhoneModel = itemView.findViewById(R.id.textViewPhoneModel);
            textViewLoginArea = itemView.findViewById(R.id.textViewLoginArea);
            textViewLoginStatus = itemView.findViewById(R.id.textViewLoginStatus);
        }

        public void bind(AccountStatusModel loginInfo) {
            textViewDateTime.setText(loginInfo.getDateTime());
            textViewPhoneModel.setText(loginInfo.getPhoneModel());
            textViewLoginArea.setText(loginInfo.getLoginArea());
            textViewLoginStatus.setText(loginInfo.getLoginStatus());
        }
    }

    // 添加登录信息到适配器中
    public void addLoginInfo(AccountStatusModel loginInfo) {
        loginHistoryList.add(loginInfo);
        notifyDataSetChanged();
    }

    // 清空适配器中的数据
    public void clearLoginInfo() {
        loginHistoryList.clear();
        notifyDataSetChanged();
    }


}
