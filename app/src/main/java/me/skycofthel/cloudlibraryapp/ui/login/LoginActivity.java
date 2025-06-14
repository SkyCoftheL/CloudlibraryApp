package me.skycofthel.cloudlibraryapp.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import me.skycofthel.cloudlibraryapp.MainActivityForAdmin;
import me.skycofthel.cloudlibraryapp.MainActivityForNormal;
import me.skycofthel.cloudlibraryapp.R;
import me.skycofthel.cloudlibraryapp.constant.Constants;
import me.skycofthel.cloudlibraryapp.databinding.ActivityLoginBinding;
import me.skycofthel.cloudlibraryapp.domain.LoginBean;
import me.skycofthel.cloudlibraryapp.okhttp.NetworkManager;
import me.skycofthel.cloudlibraryapp.okhttp.OkHttpSingleton;
import me.skycofthel.cloudlibraryapp.okhttp.OkhttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {


    private EditText username;
    private EditText passwd;
    private Button login;
    private ProgressBar progressBar;
    //    private OkhttpUtil okhttpUtil;
    private SharedPreferences sharedPreferences;
    private static final String TAG = "LoginActivity";
    private OkHttpClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        client = OkHttpSingleton.getInstance(getApplicationContext()).getClient();

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        initView();
        initLister();
    }

    private void initLister() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                开始登录


                String passwdStr = passwd.getText().toString();
                String emailStr = username.getText().toString();

                Log.d(TAG, "onClick: the username is " + username.getText().toString());
                Log.d(TAG, "onClick: the password is " + passwd.getText().toString());

                // 初始化网络管理器
                NetworkManager networkManager = NetworkManager.getInstance(getApplicationContext());

// 登录请求示例
                FormBody formBody = new FormBody.Builder()
                        .add("email", emailStr)
                        .add("password", passwdStr)
                        .build();

                networkManager.sendPostRequest(Constants.CLOUD_LIBRARY_URL + Constants.LOGIN_API, formBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "Login failed: " + e.getMessage());
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), R.string.check_connection, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "Login successful");
                            String json = response.body().string();
                            if (json != null) {
                                Gson gson = new Gson();
                                LoginBean loginBean = gson.fromJson(json, LoginBean.class);


//                                提示是否成功
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), loginBean.getMsg(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                Log.d(TAG, "onResponse: response body is " + json);


                                LoginBean.DataDTO data = loginBean.getData();
                                if (data != null) {
//                                    设置相关的数据
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("userRole", data.getRole());
                                    editor.putString("userName", data.getName());
                                    editor.putString("userEmail", data.getEmail());
                                    editor.apply();
//                                    根据规则跳转界面
                                    jumpIntentActivity();
                                    Log.d(TAG, "Response: " + json);
                                } else {
                                    Log.d(TAG, "Login failed: " + response.code());
                                }
                            }
                        }
                    }
                });


//                封装数据到formbody
//                FormBody formBody = new FormBody.Builder()
//                .add("email", emailStr)
//                .add("password", passwdStr)
//                .build();
//
//                Request request = new Request.Builder()
//                        .url(Constants.CLOUD_LIBRARY_URL + Constants.LOGIN_API)
//                        .post(formBody)
//                        .tag(this) // 添加 Tag 以便取消请求
//                        .build();
//
////                Call task = client.postRequestOk(Constants.CLOUD_LIBRARY_URL, Constants.LOGIN_API,formBody);
//
//                Call task = client.newCall(request);
//
//// 返回的数据
//                task.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getApplicationContext(),R.string.check_connection,Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                        if (response.code()== HttpURLConnection.HTTP_OK){
//                            String json = response.body().string();
//                            if (json != null) {
//                                Gson gson=new Gson();
//                                LoginBean loginBean=gson.fromJson(json,LoginBean.class);
//
//
////                                提示是否成功
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(getApplicationContext(),loginBean.getMsg(),Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//                                Log.d(TAG, "onResponse: response body is "+json);
//
//
//                                LoginBean.DataDTO data = loginBean.getData();
//                                if (data != null) {
////                                    设置相关的数据
//                                    SharedPreferences.Editor editor=sharedPreferences.edit();
//                                    editor.putString("userRole",data.getRole());
//                                    editor.putString("userName", data.getName());
//                                    editor.putString("userEmail",data.getEmail());
//                                    editor.apply();
////                                    根据规则跳转界面
//                                    jumpIntentActivity();
//
//                                }
//                            }
//                        }
//                    }
//                });
//
            }
        });


    }

    private void jumpIntentActivity() {
        String role = sharedPreferences.getString("userRole", "");
        if (role.equals("ADMIN")) {
            startActivity(new Intent(this, MainActivityForAdmin.class));
            finish();
        } else if (role.equals("USER")) {
            startActivity(new Intent(this, MainActivityForNormal.class));
            finish();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), R.string.system_error, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void initView() {
        progressBar = findViewById(R.id.loading);
        username = findViewById(R.id.username);
        passwd = findViewById(R.id.password);
        login = findViewById(R.id.login);

        String userEmail = sharedPreferences.getString("userEmail", "");
        username.setText(userEmail);

    }


}