package me.skycofthel.cloudlibraryapp.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.skycofthel.cloudlibraryapp.R;
import me.skycofthel.cloudlibraryapp.constant.Constants;
import me.skycofthel.cloudlibraryapp.domain.RecordBean;
import me.skycofthel.cloudlibraryapp.domain.SelectNewBooks;
import me.skycofthel.cloudlibraryapp.okhttp.NetworkManager;
import me.skycofthel.cloudlibraryapp.ui.home.adapter.CurrentBorrowingAdapter;
import me.skycofthel.cloudlibraryapp.ui.home.adapter.HistoricalBorrowingAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HistoricalBorrowingActivity extends AppCompatActivity {

    private ListView historicalBooksList;
    private List<RecordBean.DataDTO.RowsDTO> dataList;
    private NetworkManager networkManager;
    private HistoricalBorrowingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_borrowing);
        setTitle(R.string.action_historical_record);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initData();
        toLoadData();
    }

    private void toLoadData() {
        dataList.clear();

        networkManager = NetworkManager.getInstance(getApplicationContext());

        networkManager.sendGetRequest(Constants.CLOUD_LIBRARY_URL +Constants.CLOUD_LIBRARY_RECORD_URL+ Constants.ACTION_HISTORICAL_RECORD_API, new Callback() {
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
                        RecordBean recordBean=gson.fromJson(json, RecordBean.class);
                        RecordBean.DataDTO data = recordBean.getData();
                        List<RecordBean.DataDTO.RowsDTO> rows = data.getRows();

                        dataList.addAll(rows);

                        adapter = new HistoricalBorrowingAdapter(getApplicationContext(),R.layout.item_list_for_borrower,dataList);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                historicalBooksList.setAdapter(adapter);
                            }
                        });
                    }
                }
            }
        });
    }

    private void initData() {
        dataList = new ArrayList();
    }

    private void initView() {
        historicalBooksList = findViewById(R.id.listView);
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
