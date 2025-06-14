package me.skycofthel.cloudlibraryapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import me.skycofthel.cloudlibraryapp.ui.home.HomeFragment;
import me.skycofthel.cloudlibraryapp.ui.overview.OverviewFragment;
import me.skycofthel.cloudlibraryapp.ui.workbench.WorkbenchFragment;

public class MainActivityForNormal extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;

    private MyFragmentAdapter myFragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_normal);



        bottomNavigationView=findViewById(R.id.nav_view);

        initPage();
        initLister();




    }

    private void initLister() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            //监听页面变化
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                        setTitle(R.string.title_home);
                        break;

                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.navigation_overview);
                        setTitle(R.string.title_overview);
                }
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            //监听按钮点击
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        viewPager2.setCurrentItem(0);
                        return true;
                    case R.id.navigation_overview:
                        viewPager2.setCurrentItem(1);
                        return true;
                }
                return false;
            }
        });
    }

    private void initPage() {
        viewPager2=findViewById(R.id.main_page);
        List<Fragment> list=new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new OverviewFragment());

        myFragmentAdapter=new MyFragmentAdapter(getSupportFragmentManager(),getLifecycle(),list);

        viewPager2.setAdapter(myFragmentAdapter);
    }

}