package me.skycofthel.cloudlibraryapp.ui.home;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import me.skycofthel.cloudlibraryapp.R;
import me.skycofthel.cloudlibraryapp.constant.Constants;
import me.skycofthel.cloudlibraryapp.domain.LoginBean;
import me.skycofthel.cloudlibraryapp.domain.SelectNewBooks;
import me.skycofthel.cloudlibraryapp.okhttp.NetworkManager;
import me.skycofthel.cloudlibraryapp.ui.home.adapter.NewArrivesAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class BookLoaningActivity extends AppCompatActivity {

    private SearchView searchBooks;
    private static final String TAG="BookLoaningActivity";
    private ListView searchBooksList;
    private List<SelectNewBooks.DataDTO.RowsDTO> dataList;
    private NetworkManager networkManager;
    private NewArrivesAdapter adapter;
    private SharedPreferences login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_loaning);
        setTitle(R.string.action_book_lending);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        initViews();
        initLister();
        initData();
        searchAll();

        toLoanBooks();

        login = getSharedPreferences("login", MODE_PRIVATE);

    }

    private void toLoanBooks() {
        searchBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: position is "+position);
                // 获取点击位置的数据
//                Object item = parent.getItemAtPosition(position);
//                // 处理点击事件
                FormBody formBody=new FormBody.Builder()
                        .add("id", String.valueOf(adapter.getBooksId(position)))
                        .add("email",login.getString("userEmail", ""))
                        .add("returnTime", getFutureTime())
                        .build();

                Log.d(TAG, "onItemClick: userEmail is "+login.getString("userEmail",""));


                AlertDialog.Builder builder = new AlertDialog.Builder(BookLoaningActivity.this);
                builder.setTitle(R.string.action_to_borrow_book)
                        .setMessage(adapter.getBookName(position))
                        .setPositiveButton(R.string.action_alert_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                networkManager.sendPostRequest(Constants.CLOUD_LIBRARY_URL + Constants.CLOUD_LIBRARY_BOOK_URL + Constants.ACTION_BORROW_BOOK_API, formBody, new Callback() {
                                    @Override
                                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),R.string.check_connection,Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                        String json = response.body().string();
                                        if (json != null) {
                                            Gson gson=new Gson();
                                            LoginBean loginBean=gson.fromJson(json,LoginBean.class);
                                            String msg = loginBean.getMsg();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                    }
                                });
                                dialog.dismiss(); // 关闭对话框
                            }
                        })
                        .setNegativeButton(R.string.action_alert_cancel, null); // 取消按钮，点击自动关闭对话框

                // 创建并显示对话框
                builder.create().show();
            }
        });
    }

    private String getFutureTime(){
        // 获取当前日期
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();


            // 计算未来30天的日期
            LocalDate futureDate = currentDate.plusDays(30);

            // 定义日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // 格式化并输出日期
            String formattedCurrentDate = currentDate.format(formatter);
            String formattedFutureDate = futureDate.format(formatter);

            System.out.println("当前日期: " + formattedCurrentDate);
            System.out.println("未来30天日期: " + formattedFutureDate);

            return formattedFutureDate;
        }
        return null;



    }

    private void searchAll() {
        dataList.clear();

        networkManager=NetworkManager.getInstance(this);

        networkManager.sendGetRequest(Constants.CLOUD_LIBRARY_URL + Constants.CLOUD_LIBRARY_BOOK_URL + Constants.ACTION_SEARCH_API, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),R.string.check_connection,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    if (json != null) {
                        Gson gson=new Gson();
                        SelectNewBooks selectNewBooks=gson.fromJson(json,SelectNewBooks.class);

                        SelectNewBooks.DataDTO data = selectNewBooks.getData();

                        List<SelectNewBooks.DataDTO.RowsDTO> rows = data.getRows();

                        dataList.addAll(rows);

                        adapter = new NewArrivesAdapter(getApplicationContext(),R.layout.item_list,dataList);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchBooksList.setAdapter(adapter);
                            }
                        });

                    }
                }
            }
        });


    }

    private void initData() {
        dataList = new ArrayList<>();
    }

    private void initViews() {
        searchBooks = findViewById(R.id.action_search_books);
        searchBooksList = findViewById(R.id.listView);

    }

    private void initLister() {

        // 设置搜索文本变化监听
        searchBooks.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 用户点击键盘搜索按钮时触发
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 搜索文本发生变化时触发
                // 可以在这里做实时搜索提示等功能，比如根据输入内容过滤数据
                return false;
            }
        });

        searchBooks.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
//                关闭时触发
                searchAll();
                return false;
            }
        });

    }

    private void performSearch(String query) {
        dataList.clear();
        networkManager = NetworkManager.getInstance(this);

        FormBody formBody=new FormBody.Builder()
                .add("name", query)
                .build();
        networkManager.sendPostRequest(Constants.CLOUD_LIBRARY_URL + Constants.CLOUD_LIBRARY_BOOK_URL + Constants.ACTION_SEARCH_API, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),R.string.check_connection,Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    if (json != null) {
                        Gson gson=new Gson();
                        SelectNewBooks selectNewBooks=gson.fromJson(json,SelectNewBooks.class);

                        SelectNewBooks.DataDTO data = selectNewBooks.getData();

                        List<SelectNewBooks.DataDTO.RowsDTO> rows = data.getRows();

                        dataList.addAll(rows);

                        NewArrivesAdapter adapter=new NewArrivesAdapter(getApplicationContext(),R.layout.item_list,dataList);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchBooksList.setAdapter(adapter);
                                Toast.makeText(getApplicationContext(),selectNewBooks.getMsg(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 点击返回按钮，finish 当前 Activity 回到上一个页面
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}