package com.example.toolbox.UI.My_screen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toolbox.UI.LoginActivity;
import com.example.toolbox.R;
import com.example.toolbox.SQL.TotalUserDatabase;

import java.io.ByteArrayOutputStream;

public class userinfoActivity extends AppCompatActivity {

    private TextView textViewAccount;
    private Button buttonLogout;
    private ImageView imageView_back;
    private ImageView imageViewAvatar;
    private TextView textViewUsername;

    private TotalUserDatabase totalUserDatabase;
    private SharedPreferences sharedPreferences;

    private static final int PICK_IMAGE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        // 初始化视图
        textViewAccount = findViewById(R.id.textViewAccount);
        buttonLogout = findViewById(R.id.buttonLogout);
        imageView_back = findViewById(R.id.imageView_back);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        textViewUsername = findViewById(R.id.textViewUsername);

        // 初始化数据库帮助类
        totalUserDatabase = new TotalUserDatabase(this);
        // 初始化SharedPreferences
        sharedPreferences = getSharedPreferences("login_state", MODE_PRIVATE);

        // 获取当前登录的用户账号
        String loggedInAccount = sharedPreferences.getString("loggedInUser", "");
        textViewAccount.setText("当前账号：" + loggedInAccount);

        // 加载预设头像
        imageViewAvatar.setImageResource(R.mipmap.img_4);

        // 设置点击事件监听器
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 点击编辑用户名
        textViewUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUsernameEditDialog();
            }
        });



        // 点击退出登录
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }






    private void showUsernameEditDialog() {
        if (textViewUsername != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("编辑用户名");

            final EditText input = new EditText(this);
            input.setText(textViewUsername.getText().toString()); // 将当前用户名设置为默认文本
            builder.setView(input);

            builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newUsername = input.getText().toString().trim();
                    if (!newUsername.isEmpty()) {
                        // 获取当前登录的用户账号
                        String loggedInAccount = sharedPreferences.getString("loggedInUser", "");
                        // 更新用户名
                        totalUserDatabase.updateUsername(loggedInAccount, newUsername);
                        // 更新界面上的用户名显示
                        textViewUsername.setText(newUsername);
                        Toast.makeText(userinfoActivity.this, "用户名已更新", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(userinfoActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
            Toast.makeText(this, "用户名视图为空", Toast.LENGTH_SHORT).show();
        }
    }



    // 退出登录
    private void logout() {
        // 清除用户登录状态
        clearUserLoginState();
        // 提示用户已退出登录
        Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show();
        // 跳转到登录界面
        Intent intent = new Intent(userinfoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // 清除用户登录状态
    private void clearUserLoginState() {
        // 清除SharedPreferences中的登录状态
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("loggedInUser"); // 移除登录用户的手机号
        editor.apply();
    }
}
