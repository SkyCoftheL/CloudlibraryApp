package me.skycofthel.cloudlibraryapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static final String TAG="ExampleInstrumentedTest";

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();






        // 初始化网络管理器
        NetworkManager networkManager = NetworkManager.getInstance(appContext);

// 登录请求示例
        FormBody formBody = new FormBody.Builder()
                .add("email", "test@test.com")
                .add("password", "test")
                .build();

        networkManager.sendPostRequest("http://10.136.18.196:8080/cloudlibrary/api/login", formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Login failed: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Login successful");
                    String responseData = response.body().string();
                    Log.d(TAG, "Response: " + responseData);
                } else {
                    Log.d(TAG, "Login failed: " + response.code());
                }
            }
        });

// 登录后访问需要认证的资源
        networkManager.sendGetRequest("http://10.136.18.196:8080/cloudlibrary/api/book/selectNewbooks", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: "+response.body().string());
                } else {
                    Log.d(TAG, "Authenticated request failed: " + response.code());
                }
            }
        });




        assertEquals("me.skycofthel.cloudlibraryapp", appContext.getPackageName());
    }
}