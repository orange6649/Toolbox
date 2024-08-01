package com.example.toolbox.UI.Setting_ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.toolbox.R;

import java.io.File;

public class ClearCacheActivity extends Activity {

    private TextView tvCacheSize;
    private Button btnClearCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_cache);

        tvCacheSize = findViewById(R.id.tv_cache_size);
        btnClearCache = findViewById(R.id.btn_clear_cache);

        btnClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCache();
            }
        });

        // 设置布局并查找返回按钮
        ImageView backButton = findViewById(R.id.imageView_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // 更新当前缓存大小
        updateCacheSize();
    }

    // 更新当前缓存大小显示
    private void updateCacheSize() {
        long cacheSize = getDirSize(getCacheDir());
        tvCacheSize.setText("当前缓存大小：" + formatSize(cacheSize));
    }

    // 计算目录大小
    private long getDirSize(File dir) {
        long size = 0;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                size += file.length();
            }
        }
        return size;
    }

    // 清理缓存
    private void clearCache() {
        File cacheDir = getCacheDir();
        if (cacheDir != null && cacheDir.isDirectory()) {
            deleteDir(cacheDir);
            updateCacheSize(); // 清理后更新缓存大小的显示
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir != null && dir.delete();
    }

    // 格式化文件大小
    private String formatSize(long size) {
        String suffix = "B";
        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }
        return size + suffix;
    }
}
