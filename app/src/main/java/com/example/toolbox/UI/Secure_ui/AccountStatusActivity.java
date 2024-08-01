package com.example.toolbox.UI.Secure_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.example.toolbox.Adapter.AccountStatusAdapter;

import com.example.toolbox.Model.AccountStatusModel;
import com.example.toolbox.R;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import android.util.Log;

import com.example.toolbox.SQL.TotalUserDatabase;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageButton;

import android.content.DialogInterface;
import com.example.toolbox.SQL.TotalUserDatabase;

import java.text.ParseException;


public class AccountStatusActivity extends AppCompatActivity {

    private TextView textViewAccountStatus;
    private RecyclerView recyclerViewLoginHistory;
    private AccountStatusAdapter accountStatusAdapter;

    private TotalUserDatabase databaseHelper;
    private SQLiteDatabase database;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int REQUEST_LOCATION_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_status);

        // 初始化数据库帮助类和数据库
        databaseHelper = new TotalUserDatabase(this);
        database = databaseHelper.getWritableDatabase();

        // 返回按钮
        ImageView backButton = findViewById(R.id.imageView_back);

        // 设置返回按钮点击事件监听器
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 在此处添加返回按钮点击后的操作
                finish(); // 结束当前活动，返回上一个活动或主屏幕
            }
        });

        // 删除按钮的点击事件监听器
        ImageButton deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DeleteButton", "删除按钮被点击了");
                // 在这里执行删除按钮的逻辑
                // 删除账号登录历史记录
                deleteLoginHistory();
            }
        });

        // 初始化视图
        initView();

        // 请求定位权限并获取位置信息
        requestLocationPermission();

        // 设置RecyclerView
        setupRecyclerView();

        // 加载真实数据
        loadRealData();

        // 检查账号登录成功并自动获取位置信息
        checkAccountLoginAndRetrieveLocation();
    }

    // 初始化视图
    private void initView() {
        textViewAccountStatus = findViewById(R.id.textViewAccountStatus);
        recyclerViewLoginHistory = findViewById(R.id.recyclerViewLoginHistory);
    }

    // 请求定位权限并获取位置信息
    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 请求定位权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            } else {
                getLocation();
            }
        }
    }

    // 获取地理位置信息
    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // 获取经纬度
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // 根据经纬度获取地址信息
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        String country = addresses.get(0).getCountryName();
                        String city = addresses.get(0).getLocality();
                        String province = addresses.get(0).getAdminArea();
                        String address = country + "." + province + "." + city;

                        // 更新登录地区信息显示
                        updateLoginArea(address);

                        // 保存登录历史记录到数据库
                        saveLoginHistory(address);

                        // 移除位置更新请求
                        removeLocationUpdates();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }
        };

        // 请求位置更新
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // 请求位置更新，间隔时间为24h，距离变化为100m
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 24 * 60 * 60 * 1000, 100, locationListener);
    }

    // 更新登录地区信息显示
    private void updateLoginArea(String address) {
        String phoneBrand = Build.MANUFACTURER;
        String phoneModel = Build.MODEL;

        // 获取当前日期和时间
        String dateTime = getCurrentDateTime();

        // 添加登录历史数据，包括地区信息
        AccountStatusModel loginInfo = new AccountStatusModel(dateTime, phoneBrand + " " + phoneModel, "登录成功", address);
        accountStatusAdapter.addLoginInfo(loginInfo);
    }

    // 保存登录历史记录到数据库
    private void saveLoginHistory(String address) {
        // 获取当前日期和时间
        String currentDateTime = getCurrentDateTime();

        // 获取手机型号
        String phoneBrand = Build.MANUFACTURER;
        String phoneModel = Build.MODEL;

        // 插入数据到数据库
        long id = databaseHelper.insertLoginHistory(currentDateTime, phoneBrand + " " + phoneModel, "登录成功", address);
        if (id != -1) {
            Toast.makeText(this, "登录历史记录保存成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "登录历史记录保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    // 移除位置更新请求
    private void removeLocationUpdates() {
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    // 获取当前日期和时间
    private String getCurrentDateTime() {
        // 创建日期格式化对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // 获取当前时间并格式化
        return dateFormat.format(new Date());
    }

    // 处理权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "未授权定位权限，无法获取位置信息", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止位置更新
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    // 设置RecyclerView
    private void setupRecyclerView() {
        // 创建适配器
        accountStatusAdapter = new AccountStatusAdapter(new ArrayList<>());

        // 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewLoginHistory.setLayoutManager(layoutManager);

        // 设置适配器
        recyclerViewLoginHistory.setAdapter(accountStatusAdapter);
    }

    // 加载真实数据的方法
    private void loadRealData() {
        // 获取所有登录历史记录
        List<AccountStatusModel> loginHistoryList = getAllLoginHistory();

        // 使用冒泡排序算法对登录历史记录按时间进行排序（从大到小）
        for (int i = 0; i < loginHistoryList.size() - 1; i++) {
            for (int j = 0; j < loginHistoryList.size() - i - 1; j++) {
                // 将时间字符串转换为日期对象进行比较
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                try {
                    Date date1 = format.parse(loginHistoryList.get(j).getDateTime());
                    Date date2 = format.parse(loginHistoryList.get(j + 1).getDateTime());
                    // 如果前一个时间大于后一个时间，则交换它们的位置
                    if (date1 != null && date2 != null && date1.compareTo(date2) > 0) {
                        AccountStatusModel temp = loginHistoryList.get(j);
                        loginHistoryList.set(j, loginHistoryList.get(j + 1));
                        loginHistoryList.set(j + 1, temp);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        // 添加排序后的数据到适配器中
        for (int i = loginHistoryList.size() - 1; i >= 0; i--) {
            accountStatusAdapter.addLoginInfo(loginHistoryList.get(i));
        }
    }

    // 获取所有登录历史记录
    private List<AccountStatusModel> getAllLoginHistory() {
        List<AccountStatusModel> loginHistoryList = new ArrayList<>();
        Cursor cursor = database.query(TotalUserDatabase.TABLE_LOGIN_HISTORY, null, null, null, null, null, TotalUserDatabase.COLUMN_HISTORY_DATE_TIME + " DESC"); // 按时间降序排列
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TotalUserDatabase.COLUMN_HISTORY_ID);
            int dateTimeIndex = cursor.getColumnIndex(TotalUserDatabase.COLUMN_HISTORY_DATE_TIME);
            int phoneModelIndex = cursor.getColumnIndex(TotalUserDatabase.COLUMN_HISTORY_PHONE_MODEL);
            int loginStatusIndex = cursor.getColumnIndex(TotalUserDatabase.COLUMN_HISTORY_LOGIN_STATUS);
            int loginAreaIndex = cursor.getColumnIndex(TotalUserDatabase.COLUMN_HISTORY_LOGIN_AREA);

            do {
                AccountStatusModel loginInfo = new AccountStatusModel();
                loginInfo.setId(cursor.getLong(idIndex));
                loginInfo.setDateTime(cursor.getString(dateTimeIndex));
                loginInfo.setPhoneModel(cursor.getString(phoneModelIndex));
                loginInfo.setLoginStatus(cursor.getString(loginStatusIndex));
                loginInfo.setLoginArea(cursor.getString(loginAreaIndex));
                loginHistoryList.add(loginInfo);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return loginHistoryList;
    }

    // 删除登录历史记录的方法
    private void deleteLoginHistory() {
        // 创建删除确认对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认删除");
        builder.setMessage("您确定要删除登录历史记录吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 清空数据库中的登录历史记录
                databaseHelper.clearDatabase();

                // 清空适配器中的数据
                accountStatusAdapter.clearLoginInfo();

                // 刷新列表
                loadRealData();

                // 检查数据库是否为空
                if (isLoginHistoryEmpty()) {
                    // 如果数据库为空，显示提示消息
                    Toast.makeText(AccountStatusActivity.this, "历史登录记录已清理干净", Toast.LENGTH_SHORT).show();
                }

                // 关闭数据库连接
                database.close(); // 添加这行代码
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户取消删除操作，不做任何处理
            }
        });
        builder.show();
    }

    // 检查登录历史记录是否为空的方法
    private boolean isLoginHistoryEmpty() {
        Cursor cursor = database.query(TotalUserDatabase.TABLE_LOGIN_HISTORY, null, null, null, null, null, null);
        boolean isEmpty = (cursor == null || cursor.getCount() == 0);
        if (cursor != null) {
            cursor.close();
        }
        return isEmpty;
    }

    // 检查账号登录成功并自动获取位置信息
    private void checkAccountLoginAndRetrieveLocation() {
        // 在这里执行检查账号登录成功的逻辑
        // 假设账号登录成功后调用 performWifiLocalization() 方法获取位置信息
        performWifiLocalization();
    }

    // 调用 Wi-Fi 定位方法
    private void callWifiLocalizationMethod() {
        // 在需要执行 Wi-Fi 定位的地方调用此方法
        performWifiLocalization();
    }

    // Wi-Fi 定位方法
    private void performWifiLocalization() {
        // 检查定位权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 请求定位权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        // 获取位置信息
        if (isGPSEnabled()) {
            // 如果 GPS 可用，则使用 GPS 定位
            getLocation();
        } else {
            Toast.makeText(this, "无法获取位置信息", Toast.LENGTH_SHORT).show();
        }
    }

    // 检查 GPS 是否可用
    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
