package me.skycofthel.cloudlibraryapp.ui.workbench;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import me.skycofthel.cloudlibraryapp.domain.SelectNewBooks;
import me.skycofthel.cloudlibraryapp.okhttp.NetworkManager;
import me.skycofthel.cloudlibraryapp.ui.home.adapter.NewArrivesAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditBooksInformationActivity extends AppCompatActivity {

    private ListView editBooksList;
    private List<SelectNewBooks.DataDTO.RowsDTO> dataList;
    private NetworkManager networkManager;
    private NewArrivesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_books_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.title_edit_books_information);
        
        initView();
        initLister();
        initData();
        searchAll();

        toEditBooks();
    }

    private void toEditBooks() {
        // TODO: 2025/6/13 通过id去获取相应的信息
        editBooksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int id=adapter.getBooksId(position);
                Intent intent =new Intent(getApplicationContext(),EInforActivity.class);
                intent.putExtra("id",id);

                startActivity(intent);
            }
        });
    }

    private void searchAll() {
        dataList.clear();

        networkManager = NetworkManager.getInstance(this);

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
                                editBooksList.setAdapter(adapter);
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

    private void initLister() {
    }

    private void initView() {
        editBooksList = findViewById(R.id.listView);
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