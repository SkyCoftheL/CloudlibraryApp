package me.skycofthel.cloudlibraryapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import me.skycofthel.cloudlibraryapp.R;
import me.skycofthel.cloudlibraryapp.constant.Constants;
import me.skycofthel.cloudlibraryapp.domain.SelectNewBooks;
import me.skycofthel.cloudlibraryapp.okhttp.NetworkManager;
import me.skycofthel.cloudlibraryapp.okhttp.OkhttpUtil;
import me.skycofthel.cloudlibraryapp.ui.home.adapter.BannerAdapter;
import me.skycofthel.cloudlibraryapp.ui.home.adapter.NewArrivesAdapter;
import me.skycofthel.cloudlibraryapp.ui.login.LoginActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeFragment extends Fragment {


    private View root;

    // 轮播图相关
    private ViewPager2 viewPager2;
    private BannerAdapter bannerAdapter;
    private Handler handler;
    private Runnable runnable;
    private final int[] imageList = {
            R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3
    };

    //    推荐列表相关
    private ListView listView;
    private List<SelectNewBooks.DataDTO.RowsDTO> dataList;
    private static final String TAG = "HomeFragment";
    private Button bookLoaning;
    private Button currentBorrowing;
    private Button historicalBorrowing;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        if (root == null) {
            root = inflater.inflate(R.layout.fragment_home, container, false);
        }


        initView();
        initBanner();
        initList();
        initLister();
        startAutoScroll();


        return root;
    }

    private void initLister() {
        bookLoaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),BookLoaningActivity.class));
            }
        });

        currentBorrowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),CurrentBorrowingActivity.class));
            }
        });

        historicalBorrowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),HistoricalBorrowingActivity.class));
            }
        });
    }

    private void initList() {

        dataList = new ArrayList<>();

        NetworkManager networkManager = NetworkManager.getInstance(getContext());


        // 登录后访问需要认证的资源
        networkManager.sendGetRequest(Constants.CLOUD_LIBRARY_URL + Constants.CLOUD_LIBRARY_BOOK_URL + Constants.SELECT_NEW_BOOKS_API, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),R.string.check_connection,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Authenticated request successful");

                    String json = response.body().string();
                    if (json != null) {
                        Gson gson = new Gson();
                        SelectNewBooks selectNewBooks = gson.fromJson(json, SelectNewBooks.class);

                        SelectNewBooks.DataDTO data = selectNewBooks.getData();
                        List<SelectNewBooks.DataDTO.RowsDTO> rows = data.getRows();


                        dataList.addAll(rows);

                        Log.d(TAG, "onResponse: select new books json is " + json);

                        NewArrivesAdapter adapter = new NewArrivesAdapter(getContext(), R.layout.item_list, dataList);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setAdapter(adapter);
                            }
                        });

                    }

                } else {
                    Log.d(TAG, "Authenticated request failed: " + response.code());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), R.string.invalid_login, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                            getActivity().finish();
                        }
                    });
                }
            }
        });
//        使用okhttp获取数据，进行封装
//        OkhttpUtil okhttpUtil=OkhttpUtil.getInstance();
//
//        Call task = okhttpUtil.getRequestOk(Constants.CLOUD_LIBRARY_URL, Constants.CLOUD_LIBRARY_BOOK_URL + Constants.SELECT_NEW_BOOKS_API);
//        Log.d(TAG, "initList: url is "+task.request().url());
//
//        task.enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                e.printStackTrace();
//                Log.d(TAG, "onFailure: fail to do");
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                Log.d(TAG, "onResponse: response code is "+response.code());
//
//                if (response.code()==HttpURLConnection.HTTP_UNAUTHORIZED) {
//
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getContext(),R.string.invalid_login,Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getContext(), LoginActivity.class));
//                            getActivity().finish();
//                        }
//                    });
//
//                }
//
//                if (response.code()== HttpURLConnection.HTTP_OK) {
//                    String json = response.body().string();
//                    if (json != null) {
//                        Gson gson=new Gson();
//                        SelectNewBooks selectNewBooks=gson.fromJson(json,SelectNewBooks.class);
//
//                        SelectNewBooks.DataDTO data=selectNewBooks.getData();
//                        List<SelectNewBooks.DataDTO.RowsDTO> rows = data.getRows();
//
//
//                        dataList.addAll(rows);
//
//                        Log.d(TAG, "onResponse: select new books json is "+json);
//
//                        NewArrivesAdapter adapter = new NewArrivesAdapter(getContext(), R.layout.item_list, dataList);
//                        listView.setAdapter(adapter);
//                    }
//                }
//            }
//        });


    }

    private void startAutoScroll() {
        runnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = viewPager2.getCurrentItem() + 1;
                viewPager2.setCurrentItem(nextItem, true);
                handler.postDelayed(this, 3000); // 3秒切换一次
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    private void initBanner() {
        bannerAdapter = new BannerAdapter(imageList);
        viewPager2.setAdapter(bannerAdapter);

        // 设置初始位置为中间，实现左右无限滑动
        viewPager2.setCurrentItem(Integer.MAX_VALUE / 2);

        // 绑定指示器


        // 监听页面滑动，停止自动轮播


    }

    private void stopAutoScroll() {
        handler.removeCallbacks(runnable);
    }

    private void initView() {
        viewPager2 = root.findViewById(R.id.banner2);

        handler = new Handler(Looper.getMainLooper());

        listView = root.findViewById(R.id.listView);

        bookLoaning = root.findViewById(R.id.action_book_lending);
        currentBorrowing = root.findViewById(R.id.action_current_borrowing);
        historicalBorrowing = root.findViewById(R.id.action_historical_borrowing);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopAutoScroll();

    }
}