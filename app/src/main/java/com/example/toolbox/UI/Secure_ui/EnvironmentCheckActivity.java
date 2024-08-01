package com.example.toolbox.UI.Secure_ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.Spannable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.example.toolbox.R;

public class EnvironmentCheckActivity extends AppCompatActivity {

    private TextView textViewNetworkStatus;
    private TextView textViewBatteryStatus;
    private TextView textViewCurrentTime;
    private TextView textViewDeviceModel;
    private TextView textViewStorageStatus;
    private TextView textViewCpuUsage;
    private TextView textViewMemoryUsage;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment_check);

        textViewNetworkStatus = findViewById(R.id.textViewNetworkStatus);
        textViewBatteryStatus = findViewById(R.id.textViewBatteryStatus);
        textViewCurrentTime = findViewById(R.id.textViewCurrentTime);
        textViewDeviceModel = findViewById(R.id.textViewDeviceModel);
        textViewStorageStatus = findViewById(R.id.textViewStorageStatus);
        textViewCpuUsage = findViewById(R.id.textViewCpuUsage);
        textViewMemoryUsage = findViewById(R.id.textViewMemoryUsage);

        // 找到返回按钮
        ImageView backButton = findViewById(R.id.imageView_back);

        // 设置返回按钮点击事件监听器
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在点击返回按钮时执行的操作
                finish(); // 关闭当前界面，返回上一个界面
            }
        });

        // 注册网络状态变化的广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkStatusReceiver, intentFilter);

        // 获取当前电池状态
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        updateBatteryStatus(batteryIntent);

        // 显示当前时间
        showCurrentTime();

        // 显示设备型号
        showDeviceModel();

        // 显示存储空间状态
        showStorageStatus();

        // 启动定时器，每隔一秒更新当前时间、CPU 使用率和内存占用情况
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCurrentTime();
                        showCpuUsage();
                        showMemoryUsage();
                        // 更新电池状态
                        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                        updateBatteryStatus(batteryIntent);
                    }
                });
            }
        }, 0, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解除注册网络状态变化的广播接收器
        unregisterReceiver(networkStatusReceiver);
        // 取消定时器
        timer.cancel();
    }

    // 广播接收器：接收网络状态变化的广播
    private BroadcastReceiver networkStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                // 当网络状态变化时更新界面上的网络状态
                updateNetworkStatus();
            }
        }
    };

    // 更新界面上的网络状态信息
    private void updateNetworkStatus() {
        // 获取当前网络连接状态
        boolean isConnected = checkNetworkStatus(); // 假设这是一个用于检查网络连接状态的方法

        // 更新界面上的网络状态信息
        String networkStatusText = "";
        int textColor = Color.BLACK; // 默认黑色

        if (isConnected) {
            // 已连接网络
            networkStatusText = "当前网络状态：已连接";
            textColor = Color.GREEN;
        } else {
            // 未连接网络
            networkStatusText = "当前网络状态：未连接";
            textColor = Color.RED;
        }

        // 设置网络状态文字颜色
        textViewNetworkStatus.setText(networkStatusText);
        textViewNetworkStatus.setTextColor(textColor);
    }

    // 检查网络连接状态的示例方法
    private boolean checkNetworkStatus() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // 更新界面上的电池状态信息
    private void updateBatteryStatus(Intent batteryIntent) {
        // 获取当前电池状态
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // 获取当前电量
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPercentage = level / (float) scale;

        // 更新界面上的电池状态信息
        String statusText = isCharging ? "正在充电" : "放电中";
        String batteryStatusText = "当前电池状态：" +
                statusText +
                "，电池电量：" +
                String.format(Locale.getDefault(), "%.2f", batteryPercentage * 100) +
                "%";

        // 设置文字颜色
        int textColor = isCharging ? Color.RED : Color.GRAY;

        textViewBatteryStatus.setText(batteryStatusText);
        // 设置标题文字颜色
        textViewBatteryStatus.setTextColor(textColor);
    }

    // 显示当前时间
    private void showCurrentTime() {
        // 获取当前时间
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdfTime = new SimpleDateFormat("-HH:mm:ss", Locale.getDefault());
        String currentDate = sdfDate.format(new Date());
        String currentTime = sdfTime.format(new Date());

        // 更新界面上的当前时间信息
        String currentTimeText = "当前日期：" + currentDate + currentTime;

        // 设置标题文字颜色为黑色
        textViewCurrentTime.setTextColor(Color.BLACK);

        // 设置内容文字颜色为蓝色
        SpannableStringBuilder spannableCurrentTime = new SpannableStringBuilder(currentTimeText);
        spannableCurrentTime.setSpan(new ForegroundColorSpan(Color.RED), 5, 5 + currentDate.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableCurrentTime.setSpan(new ForegroundColorSpan(Color.BLUE), 6 + currentDate.length(), currentTimeText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 显示更新后的时间
        textViewCurrentTime.setText(spannableCurrentTime);
    }

    // 显示设备型号（修改为显示手机品牌和手机型号）
    private void showDeviceModel() {
        // 获取手机品牌和手机型号
        String deviceBrand = Build.BRAND;
        String deviceModel = Build.MODEL;

        // 更新界面上的设备信息，并设置不同的文字颜色
        String title = "设备信息：";
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(title + deviceBrand + " " + deviceModel);

        // 设置标题文字颜色为红色
        ForegroundColorSpan titleColorSpan = new ForegroundColorSpan(Color.RED);
        spannableStringBuilder.setSpan(titleColorSpan, 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置内容文字颜色为蓝色
        ForegroundColorSpan contentColorSpan = new ForegroundColorSpan(Color.BLUE);
        spannableStringBuilder.setSpan(contentColorSpan, title.length(), spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewDeviceModel.setText(spannableStringBuilder);
    }

    // 显示存储空间状态
    private void showStorageStatus() {
        // 获取存储空间状态
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        long availableBlocks = stat.getAvailableBlocksLong();

        // 计算总容量、已使用空间和可用空间
        long totalSpace = totalBlocks * blockSize;
        long usedSpace = (totalBlocks - availableBlocks) * blockSize;
        long availableSpace = availableBlocks * blockSize;

        // 格式化显示
        String totalSpaceStr = Formatter.formatFileSize(this, totalSpace);
        String usedSpaceStr = Formatter.formatFileSize(this, usedSpace);
        String availableSpaceStr = Formatter.formatFileSize(this, availableSpace);

        // 更新界面上的存储空间状态信息
        String storageStatusText = "存储空间状态：" +
                "\n\n\t\t\t\t\t总容量：" + totalSpaceStr +
                "\n\t\t\t\t\t已使用：" + usedSpaceStr +
                "\n\t\t\t\t\t\t\t可用：" + availableSpaceStr;

        // 设置标题文字颜色为黑色
        textViewStorageStatus.setTextColor(Color.BLACK);

        // 设置内容文字颜色为绿色
        SpannableStringBuilder spannableStorageStatus = new SpannableStringBuilder(storageStatusText);
        spannableStorageStatus.setSpan(new ForegroundColorSpan(Color.WHITE), 7, 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStorageStatus.setSpan(new ForegroundColorSpan(Color.RED), 26, 45, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStorageStatus.setSpan(new ForegroundColorSpan(Color.GREEN), 45, storageStatusText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 显示更新后的存储空间状态
        textViewStorageStatus.setText(spannableStorageStatus);

        // 设置背景色
        textViewStorageStatus.setBackgroundColor(Color.GRAY);
    }

    // 显示CPU使用率
    private void showCpuUsage() {
        // 获取CPU使用率，这里仅作示例，实际情况需要根据系统API获取
        double cpuUsage = Math.random() * 100; // 示例：随机生成一个0到100的值作为CPU使用率

        // 更新界面上的CPU使用率信息
        String cpuUsageText = "CPU 使用率：" + String.format(Locale.getDefault(), "%.2f", cpuUsage) + "%";
        textViewCpuUsage.setText(cpuUsageText);
        // 设置背景色为红色
        textViewCpuUsage.setBackgroundColor(Color.RED);
        // 设置标题文字颜色为白色
        textViewCpuUsage.setTextColor(Color.WHITE);
    }

    // 显示内存占用情况
    private void showMemoryUsage() {
        // 获取内存占用情况，这里仅作示例，实际情况需要根据系统API获取
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;

        // 计算使用率
        double memoryUsage = (double) usedMemory / totalMemory * 100;

        // 更新界面上的内存占用情况信息
        String memoryUsageText = "内存占用情况：" +
                "\n\n总内存：" + Formatter.formatFileSize(this, totalMemory) +
                "\t\t   已用：" + Formatter.formatFileSize(this, usedMemory) +
                "\n\t\t空闲：" + Formatter.formatFileSize(this, freeMemory) +
                "\t\t\t\t使用率：" + String.format(Locale.getDefault(), "%.2f", memoryUsage) + "%";

        // 设置不同内容文字颜色
        SpannableStringBuilder spannableMemoryUsage = new SpannableStringBuilder(memoryUsageText);
        spannableMemoryUsage.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 总内存颜色为蓝色
        spannableMemoryUsage.setSpan(new ForegroundColorSpan(Color.GREEN), 7, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 已用颜色为绿色
        spannableMemoryUsage.setSpan(new ForegroundColorSpan(Color.RED), 25, 39, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 空闲颜色为红色
        spannableMemoryUsage.setSpan(new ForegroundColorSpan(Color.GRAY), 40, memoryUsageText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 使用率颜色为黄色

        // 显示更新后的内存占用情况
        textViewMemoryUsage.setText(spannableMemoryUsage);
        // 设置背景色为黄色
        textViewMemoryUsage.setBackgroundColor(Color.YELLOW);
    }
}
