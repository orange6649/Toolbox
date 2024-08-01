package com.example.toolbox;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.toolbox.UI.Fragment.FourFragment;
import com.example.toolbox.UI.Fragment.OneFragment;
import com.example.toolbox.UI.Fragment.ThreeFragment;
import com.example.toolbox.UI.Fragment.TwoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.toolbox.Adapter.MainActivityAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> fragmentList = new ArrayList<>();
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.main_vp);
        BottomNavigationView bottomNavigationView = findViewById(R.id.main_bnv);

        initData();

        MainActivityAdapter adapter = new MainActivityAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);

        // 设置页面切换动画为立方体旋转效果
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                final float normalizedPosition = Math.abs(Math.abs(position) - 1);
                page.setTranslationX(page.getWidth() * -position);

                // 设置页面缩放和旋转
                page.setTranslationX(-page.getWidth() * position);
                page.setCameraDistance(12000);
                if (position < 0.5 && position > -0.5) {
                    page.setVisibility(View.VISIBLE);
                } else {
                    page.setVisibility(View.INVISIBLE);
                }

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.setAlpha(0);
                } else if (position <= 0) { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    page.setAlpha(1);
                    page.setRotationY(180 * (1 - Math.abs(position) + 1));
                } else if (position <= 1) { // (0,1]
                    // Fade the page out.
                    page.setAlpha(1 - position);

                    // Counteract the default slide transition
                    page.setRotationY(-180 * (1 - Math.abs(position) + 1));
                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.setAlpha(0);
                }
            }
        });

        // 使用 RxJava 提高页面切换的速度
        disposables.add(Observable.just(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    // 页面更改监听
                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

                        @Override
                        public void onPageSelected(int position) {
                            switch (position) {
                                case 0:
                                    bottomNavigationView.setSelectedItemId(R.id.navigation_one);
                                    break;
                                case 1:
                                    bottomNavigationView.setSelectedItemId(R.id.navigation_two);
                                    break;
                                case 2:
                                    bottomNavigationView.setSelectedItemId(R.id.navigation_three);
                                    break;
                                case 3:
                                    bottomNavigationView.setSelectedItemId(R.id.navigation_four);
                                    break;
                            }
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {}
                    });

                    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            // 切换页面
                            if (item.getItemId() == R.id.navigation_one) {
                                viewPager.setCurrentItem(0, true); // 使用平滑滚动
                                item.setChecked(true); // 设置选中状态
                            } else if (item.getItemId() == R.id.navigation_two) {
                                viewPager.setCurrentItem(1, true); // 使用平滑滚动
                                item.setChecked(true); // 设置选中状态
                            } else if (item.getItemId() == R.id.navigation_three) {
                                viewPager.setCurrentItem(2, true); // 使用平滑滚动
                                item.setChecked(true); // 设置选中状态
                            } else if (item.getItemId() == R.id.navigation_four) {
                                viewPager.setCurrentItem(3, true); // 使用平滑滚动
                                item.setChecked(true); // 设置选中状态
                            }
                            return true;
                        }
                    });
                }));
    }

    private void initData() {
        OneFragment oneFragment = OneFragment.newInstance("1", "");
        fragmentList.add(oneFragment);
        TwoFragment twoFragment = TwoFragment.newInstance("2", "");
        fragmentList.add(twoFragment);
        ThreeFragment threeFragment = ThreeFragment.newInstance("3", "");
        fragmentList.add(threeFragment);
        FourFragment fourFragment = FourFragment.newInstance("4", "");
        fragmentList.add(fourFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理 RxJava 订阅
        disposables.clear();
    }
}
