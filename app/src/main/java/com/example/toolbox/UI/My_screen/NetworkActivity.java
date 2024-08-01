package com.example.toolbox.UI.My_screen;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import java.io.BufferedReader;
import java.io.InputStreamReader;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



import com.example.toolbox.R;

public class NetworkActivity extends AppCompatActivity {

    // UI元素
    private TextView textSpeedResult;
    private TextView textSpeedAaa;
    private TextView textSpeedPing;

    // 网速测速所需的URL
    private static final String TEST_URL = "https://example.com/";

    // 测速任务是否正在进行的标志
    private boolean isTesting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        textSpeedResult = findViewById(R.id.text_speed_result);
        textSpeedAaa = findViewById(R.id.text_speed_aaa);
        textSpeedPing = findViewById(R.id.text_speed_ping);
        //返回按钮
        ImageView backButton = findViewById(R.id.imageView_back);

        // 设置返回按钮点击事件监听器
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 在此处添加返回按钮点击后的操作
                finish(); // 结束当前活动，返回上一个活动或主屏幕
            }
        });

        Button buttonSpeedTest = findViewById(R.id.button_speed_test);
        buttonSpeedTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTesting) {
                    isTesting = true;
                    new SpeedTestTask().execute(TEST_URL);
                } else {
                    Toast.makeText(NetworkActivity.this, "测试正在进行中，请稍候", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonRefreshNetworkStatus = findViewById(R.id.button_aaa);
        buttonRefreshNetworkStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 刷新连接网络状态
                checkNetworkStatus();
            }
        });

        // 初始化界面时显示默认的网络状态信息
        checkNetworkStatus();

        // 设置按钮执行Ping测试
        Button buttonPingTest = findViewById(R.id.button_ping_test);
        buttonPingTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 执行Ping测试
                performPingTest();
            }
        });
    }



    // 异步任务类，用于执行网速测速任务
    private class SpeedTestTask extends AsyncTask<String, Double, Void> {

        @Override
        protected Void doInBackground(String... urls) {
            final OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urls[0])
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                // 获取响应数据大小
                final long contentLength = response.body().contentLength();
                // 获取响应时间
                final long elapsedTimeMillis = response.receivedResponseAtMillis() - response.sentRequestAtMillis();

                // 如果响应时间为0，则将速度设置为0
                if (elapsedTimeMillis == 0) {
                    publishProgress(0.0);
                    return null;
                }

                // 计算下载速度，单位为字节每毫秒（bytes/ms）
                double speed = (double) contentLength / elapsedTimeMillis;

                // 将速度转换为兆字节每秒（MB/s）
                double speedInMBps = speed * -1000000000 / (100 * 1024);

                // 发布更新，更新UI显示
                publishProgress(speedInMBps);
            } catch (IOException e) {
                e.printStackTrace();
                // 发布更新，更新UI显示
                publishProgress(0.0);
            }
            return null;
        }



        @Override
        protected void onProgressUpdate(Double... values) {
            double speed = values[0];
            // 格式化下载速度，保留两位小数
            String formattedSpeed = String.format("%.2f", speed);
            // 更新网速显示
            textSpeedResult.setText("当前网速：" + formattedSpeed + " MB/s");
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            // 测速任务完成，将标志设置为false
            isTesting = false;
            // 显示测试完成提示消息
            Toast.makeText(NetworkActivity.this, "网速测试完成", Toast.LENGTH_SHORT).show();
        }
    }







    // 判断是否连接网络并显示网络状态
    private void checkNetworkStatus() {
        // 获取ConnectivityManager实例
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // 获取当前活动网络信息
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            // 设备已连接到网络
            String networkType = activeNetwork.getTypeName(); // 获取网络类型（例如：WIFI或移动数据）
            boolean isConnected = activeNetwork.isConnected(); // 获取连接状态

            // 显示网络模式和连接状态
            String connectionStatus = "网络模式：" + networkType + "\n连接状态：" + (isConnected ? "已连接" : "未连接");
            textSpeedAaa.setText(connectionStatus); // 更新网络连接状态的文本视图
            Toast.makeText(NetworkActivity.this, "已连接网络", Toast.LENGTH_SHORT).show();
        } else {
            // 设备未连接到网络
            textSpeedAaa.setText("未连接到网络"); // 更新网络连接状态的文本视图
            Toast.makeText(NetworkActivity.this, "未连接网络", Toast.LENGTH_SHORT).show();
        }
    }




    // 在 NetworkActivity 类中添加一个方法来执行Ping测试
    private void performPingTest() {
        // 要ping的目标地址
        final String ipAddress = "google.com";

        // 创建一个新线程来执行ping测试
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建一个ProcessBuilder对象，设置命令参数
                    ProcessBuilder processBuilder = new ProcessBuilder("/system/bin/ping", "-c", "4", ipAddress);
                    // 重定向错误流以便读取错误信息
                    processBuilder.redirectErrorStream(true);
                    // 启动进程
                    Process process = processBuilder.start();

                    // 读取进程的输出流
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    // 读取输出并保存到StringBuilder中
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    // 等待进程完成
                    process.waitFor();
                    // 关闭输入流
                    reader.close();

                    // 解析ping测试结果并转换为中文
                    final String pingResult = parsePingResult(stringBuilder.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textSpeedPing.setText("Ping测试结果：\n" + pingResult);
                        }
                    });
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    // 显示错误消息
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NetworkActivity.this, "执行Ping测试时出错", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    // 解析Ping测试结果并转换为中文
    private String parsePingResult(String pingResult) {
        // 将小写域名转换为首字母大写的形式
        pingResult = pingResult.replaceAll("ping测试([a-z]+\\.[a-z]+\\.[a-z]+)", "Ping测试$1".toLowerCase())
                .replaceAll("bytes of data", "数据字节")
                .replaceAll("ping统计信息", "Ping统计信息")
                .replaceAll("packets transmitted", "数据包已发送")
                .replaceAll("packets received", "数据包已接收")
                .replaceAll("packet loss", "丢包率")
                .replaceAll("time", "时间");
        return pingResult;
    }


}