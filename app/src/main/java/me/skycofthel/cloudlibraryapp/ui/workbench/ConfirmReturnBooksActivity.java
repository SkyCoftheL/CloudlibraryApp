package me.skycofthel.cloudlibraryapp.ui.workbench;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.skycofthel.cloudlibraryapp.R;
import me.skycofthel.cloudlibraryapp.constant.Constants;
import me.skycofthel.cloudlibraryapp.domain.LoginBean;
import me.skycofthel.cloudlibraryapp.domain.SelectNewBooks;
import me.skycofthel.cloudlibraryapp.okhttp.NetworkManager;
import me.skycofthel.cloudlibraryapp.ui.home.CurrentBorrowingActivity;
import me.skycofthel.cloudlibraryapp.ui.home.adapter.CurrentBorrowingAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class ConfirmReturnBooksActivity extends AppCompatActivity {

    private ListView confirmBooksList;
    private List<SelectNewBooks.DataDTO.RowsDTO> dataList;
    private NetworkManager networkManager;
    private CurrentBorrowingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_return_books);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.title_confirm_return_books);

        initView();
        initData();

        toLoadData();

        toConfirmReturnBooks();
    }

    private void toConfirmReturnBooks() {
        confirmBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                FormBody formBody=new FormBody.Builder()
                        .add("id", String.valueOf(adapter.getBooksId(position)))
                        .build();



                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmReturnBooksActivity.this);
                builder.setTitle(R.string.action_to_return_book)
                        .setMessage(adapter.getBookName(position))
                        .setPositiveButton(R.string.action_alert_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                networkManager.sendPostRequest(Constants.CLOUD_LIBRARY_URL + Constants.CLOUD_LIBRARY_BOOK_URL + Constants.ACTION_RETURN_CONFIRM_API, formBody, new Callback() {
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

    private void toLoadData() {
        dataList.clear();

        networkManager = NetworkManager.getInstance(getApplicationContext());

        networkManager.sendGetRequest(Constants.CLOUD_LIBRARY_URL + Constants.CLOUD_LIBRARY_BOOK_URL + Constants.ACTION_SEARCH_BORROWED_API, new Callback() {
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

                        adapter = new CurrentBorrowingAdapter(getApplicationContext(), R.layout.item_list_for_borrower, dataList);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                confirmBooksList.setAdapter(adapter);
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

    private void initView() {
        confirmBooksList = findViewById(R.id.listView);
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