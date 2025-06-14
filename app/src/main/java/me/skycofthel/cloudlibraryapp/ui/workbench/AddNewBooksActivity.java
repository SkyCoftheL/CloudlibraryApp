package me.skycofthel.cloudlibraryapp.ui.workbench;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import me.skycofthel.cloudlibraryapp.R;
import me.skycofthel.cloudlibraryapp.constant.Constants;
import me.skycofthel.cloudlibraryapp.domain.LoginBean;
import me.skycofthel.cloudlibraryapp.okhttp.NetworkManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class AddNewBooksActivity extends AppCompatActivity {

    private Button save;
    private EditText addNewBooksName;
    private EditText addNewBooksISBN;
    private EditText addNewBooksPress;
    private EditText addNewBooksAuthor;
    private EditText addNewBooksPagi;
    private EditText addNewBooksPrice;
    private Spinner spinner;
    private String eBookStatus="0";
    private String bookName;
    private String bookISBN;
    private String bookPress;
    private String bookAuthor;
    private String bookPagi;
    private String bookPrice;
    private String bookStatus;
    private static final String TAG="AddNewBooksActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_books);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.title_add_new_books);

        initView();
        initData();
        initLister();

    }

    private void initData() {
        // TODO: 2025/6/11 prepare the data for spinner
        // 准备数据
        String[] status = {getString(R.string.action_book_status), getString(R.string.action_book_status_anti)};
        // 创建适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                status
        );

        // 设置下拉列表的样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);


    }

    private void getData() {
        bookName = addNewBooksName.getText().toString();
        bookISBN = addNewBooksISBN.getText().toString();
        bookPress = addNewBooksPress.getText().toString();
        bookAuthor = addNewBooksAuthor.getText().toString();
        bookPagi = addNewBooksPagi.getText().toString();
        bookPrice = addNewBooksPrice.getText().toString();
        bookStatus = eBookStatus;
    }

    private void initUpLoadData() {
        // TODO: 2025/6/12 发起网络请求

        getData();

        NetworkManager networkManager=NetworkManager.getInstance(getApplicationContext());

        FormBody formBody=new FormBody.Builder()
                .add("name",bookName)
                .add("isbn",bookISBN)
                .add("press",bookPress)
                .add("author",bookAuthor)
                .add("pagination",bookPagi)
                .add("price",bookPrice)
                .add("status",bookStatus)
                .build();

        networkManager.sendPostRequest(Constants.CLOUD_LIBRARY_URL + Constants.CLOUD_LIBRARY_BOOK_URL + Constants.ACTION_ADD_BOOK_API, formBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), R.string.check_connection, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();

                    if (json != null) {
                        Gson gson=new Gson();
                        LoginBean loginBean=gson.fromJson(json,LoginBean.class);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), loginBean.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }


    private void initLister() {
        // 设置选择监听器
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private String TAG="spinner";

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 2025/6/12 value is 0 or 3;
                eBookStatus = "0";
                if (position==1) eBookStatus ="3";
                Log.d(TAG, "onItemSelected: selected status is "+ eBookStatus);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 当没有选择时的处理
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUpLoadData();
            }
        });
    }

    private void initView() {
        save = findViewById(R.id.save_new_books);
        addNewBooksName = findViewById(R.id.add_book_name_edt);
        addNewBooksISBN = findViewById(R.id.add_book_isbn_edt);
        addNewBooksPress = findViewById(R.id.add_book_press_edt);
        addNewBooksAuthor = findViewById(R.id.add_book_author_edt);
        addNewBooksPagi = findViewById(R.id.add_book_pagi_edt);
        addNewBooksPrice = findViewById(R.id.add_book_price_edt);
        spinner = findViewById(R.id.spinner);

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