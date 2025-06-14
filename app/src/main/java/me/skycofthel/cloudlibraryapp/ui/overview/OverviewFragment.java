package me.skycofthel.cloudlibraryapp.ui.overview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;

import me.skycofthel.cloudlibraryapp.R;
import me.skycofthel.cloudlibraryapp.constant.Constants;
import me.skycofthel.cloudlibraryapp.domain.LoginBean;
import me.skycofthel.cloudlibraryapp.okhttp.NetworkManager;
import me.skycofthel.cloudlibraryapp.okhttp.OkhttpUtil;
import me.skycofthel.cloudlibraryapp.ui.login.LoginActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OverviewFragment extends Fragment {


    private View root;
    private TextView showName;
    private TextView showEmail;
    private TextView showRole;
    private Button logOutUser;
    private SharedPreferences login;
    private SharedPreferences.Editor editor;
    private static final String TAG = "OverviewFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_overview, container, false);


            initView();
            initLister();
            initData();


//            开始视图


        }

        return root;
    }

    private void initData() {

        login = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = login.edit();
        showName.setText(login.getString("userName", ""));
        showEmail.setText(login.getString("userEmail", ""));
        showRole.setText(login.getString("userRole", ""));
    }

    private void initLister() {
        logOutUser.setOnClickListener(new View.OnClickListener() {

            // TODO: 2025/6/3 无论何时都退出到登录界面，跳转规则需要清空
            @Override
            public void onClick(View view) {


                NetworkManager networkManager = NetworkManager.getInstance(getContext());

//                Call task = okhttpUtil.getRequestOk(Constants.CLOUD_LIBRARY_URL, Constants.LOGOUT_API);

                // 登录后访问需要认证的资源
                networkManager.sendGetRequest(Constants.CLOUD_LIBRARY_URL + Constants.LOGOUT_API, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "Request failed: " + e.getMessage());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), R.string.check_connection, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), LoginActivity.class));
                                getActivity().finish();
                            }
                        });

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "Authenticated request successful");

                            String json = response.body().string();
                            Log.d(TAG, "onResponse: json is " + json);
                            if (json != null) {
                                Gson gson = new Gson();
                                LoginBean loginBean = gson.fromJson(json, LoginBean.class);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), loginBean.getMsg(), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getContext(), LoginActivity.class));
                                        getActivity().finish();
                                    }
                                });




                            }
                        }


                    }
                });

                editor.putString("userRole", "logout");
                editor.apply();
                Log.d(TAG, "onResponse: userRole is " + login.getString("userRole", "no string"));




//                task.enqueue(new Callback() {
//                    @Override
//                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                        e.printStackTrace();
//                        Log.d(TAG, "onFailure: ");
//
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getContext(),R.string.check_connection,Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//
//
//                        startActivity(new Intent(getContext(), LoginActivity.class));
//                        getActivity().finish();
//                    }
//
//                    @Override
//                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                        if (response.code()== HttpURLConnection.HTTP_OK){
//                            String json = response.body().string();
//                            Log.d(TAG, "onResponse: json is "+json);
//                            if (json != null) {
//                                Gson gson=new Gson();
//                                LoginBean loginBean=gson.fromJson(json,LoginBean.class);
//
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(getContext(),loginBean.getMsg(),Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//
//
//                                startActivity(new Intent(getContext(),LoginActivity.class));
//                                getActivity().finish();
//                            }
//                        }
//                    }
//                });
//                editor.putString("userRole","logout");
//                editor.apply();
//                Log.d(TAG, "onResponse: userRole is "+login.getString("userRole","no string"));
            }


        });
    }

    private void initView() {
        showName = root.findViewById(R.id.over_show_name_te);
        showEmail = root.findViewById(R.id.over_show_email_te);
        showRole = root.findViewById(R.id.over_show_role_te);

        logOutUser = root.findViewById(R.id.log_out_btn);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}