package com.example.toolbox.UI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import android.content.pm.PackageManager;

import com.example.toolbox.MainActivity;
import com.example.toolbox.R;
import com.example.toolbox.SQL.TotalUserDatabase;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhone;
    private EditText etVerificationCode; // 添加验证码输入框
    private Button btnGetVerificationCode;
    private Button btnLogin;
    private TotalUserDatabase dbHelper; // 修改这里的类型
    private SharedPreferences sharedPreferences; // 用于存储登录状态的SharedPreferences

    private String verificationCode; // 存储生成的验证码
    private long verificationCodeTimestamp; // 存储生成验证码的时间戳

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化数据库帮助类
        dbHelper = new TotalUserDatabase(this); // 修改这里的实例化
        // 初始化SharedPreferences
        sharedPreferences = getSharedPreferences("login_state", Context.MODE_PRIVATE);

        // 检查登录状态
        if (isUserLoggedIn()) {
            // 如果用户已经登录，直接跳转到主界面
            redirectToMainActivity();
            return; // 直接返回，不再执行下面的代码
        }

        // 初始化控件
        etPhone = findViewById(R.id.et_phone);
        etVerificationCode = findViewById(R.id.et_verification_code); // 初始化验证码输入框
        btnGetVerificationCode = findViewById(R.id.btn_get_verification_code);
        btnLogin = findViewById(R.id.btn_login);

        // 获取验证码按钮点击事件
        btnGetVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = etPhone.getText().toString().trim();
                if (isValidPhoneNumber(phoneNumber)) {
                    // 生成随机的四位验证码
                    generateRandomVerificationCode();
                    // 显示验证码对话框
                    showVerificationCodeDialog();
                } else {
                    if (TextUtils.isEmpty(phoneNumber)) {
                        Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    } else  {
                        Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 登录按钮点击事件
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 登录逻辑
                String phoneNumber = etPhone.getText().toString().trim();
                String userVerificationCode = etVerificationCode.getText().toString().trim(); // 获取用户输入的验证码
                if (isValidVerificationCode(userVerificationCode)) { // 检查验证码是否正确
                    saveUserLoginState(phoneNumber); // 保存用户登录状态
                    saveUserPhoneNumberToDatabase(phoneNumber); // 保存用户手机号到数据库
                    requestLocationPermission(); // 请求获取定位权限
                } else {
                    Toast.makeText(LoginActivity.this, "验证码错误或已过期", Toast.LENGTH_SHORT).show(); // 提示验证码错误
                }
            }
        });
    }

    // 检查手机号是否合法
    private boolean isValidPhoneNumber(String phoneNumber) {
        return !TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == 11;
    }

    // 生成随机验证码
    private void generateRandomVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000; // 生成1000到9999之间的随机数
        verificationCode = String.valueOf(code);
        verificationCodeTimestamp = SystemClock.elapsedRealtime(); // 记录生成验证码时的时间戳
    }

    // 显示验证码对话框，包含手机号和验证码信息
    private void showVerificationCodeDialog() {
        String phoneNumber = etPhone.getText().toString().trim();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("验证码")
                .setMessage("手机号：" + phoneNumber + "\n验证码是：" + verificationCode)
                .setPositiveButton("确定", null)
                .show();
    }

    // 检查验证码是否正确且在有效时间范围内（60秒内）
    private boolean isValidVerificationCode(String userVerificationCode) {
        if (verificationCode == null || TextUtils.isEmpty(userVerificationCode)) {
            return false;
        }
        // 检查验证码是否相等且生成时间在60秒内
        return userVerificationCode.equals(verificationCode) && (SystemClock.elapsedRealtime() - verificationCodeTimestamp) <= 60000;
    }

    // 保存用户登录状态到SharedPreferences
    private void saveUserLoginState(String phoneNumber) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true); // 保存登录状态为已登录
        editor.putString("loggedInUser", phoneNumber); // 保存登录用户的手机号
        editor.apply();
    }

    // 检查用户是否已登录
    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false); // 检查SharedPreferences中是否存在登录状态
    }

    // 跳转到主界面
    private void redirectToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // 保存用户手机号到数据库
    private void saveUserPhoneNumberToDatabase(String phoneNumber) {
        // 在这里调用数据库操作方法，将手机号保存到数据库中
        dbHelper.insertUser(phoneNumber);
    }

    // 请求获取定位权限
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
    }

    // 处理权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授予了定位权限，跳转到主界面
                redirectToMainActivity();
            } else {
                // 用户拒绝了定位权限，显示提示信息
                showPermissionDeniedDialog();
            }
        }
    }

    // 显示权限被拒绝的对话框
    private void showPermissionDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限被拒绝")
                .setMessage("您已拒绝获取定位权限，可能会影响应用的部分功能，请在设置中开启权限后重试。")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击确定按钮，关闭对话框
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
