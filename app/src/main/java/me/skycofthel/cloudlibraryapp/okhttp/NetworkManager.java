package me.skycofthel.cloudlibraryapp.okhttp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class NetworkManager {
    private static NetworkManager instance;
    private OkHttpClient client;
    private Context context;
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String TAG = "NetworkManager";

    private NetworkManager(Context context) {
        this.context = context.getApplicationContext();

        // 创建日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 构建OkHttpClient
        client = new OkHttpClient.Builder()
                .cookieJar(new PersistentCookieJar())
                .addInterceptor(loggingInterceptor)    // 日志拦截器
                .addInterceptor(new CookieInterceptor()) // Cookie拦截器
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();
    }

    public static synchronized NetworkManager getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkManager(context);
        }
        return instance;
    }

    // 发起GET请求
    public void sendGetRequest(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }

    // 发起POST请求
    public void sendPostRequest(String url, FormBody formBody, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    // Cookie管理类
    private class PersistentCookieJar implements CookieJar {
        private SharedPreferences cookiePrefs;

        public PersistentCookieJar() {
            cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE);
        }

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            SharedPreferences.Editor editor = cookiePrefs.edit();

            // 清除该域名下的旧Cookie
            String domain = url.host();
            editor.remove(domain);

            // 保存新Cookie
            StringBuilder cookieBuilder = new StringBuilder();
            for (Cookie cookie : cookies) {
                cookieBuilder.append(cookie.name()).append("=").append(cookie.value()).append(";");
            }

            if (cookieBuilder.length() > 0) {
                editor.putString(domain, cookieBuilder.toString());
            }
            editor.apply();

            Log.d(TAG, "Saved cookies for " + domain + ": " + cookieBuilder.toString());
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = new ArrayList<>();
            String domain = url.host();
            String cookieString = cookiePrefs.getString(domain, "");

            if (!cookieString.isEmpty()) {
                String[] cookiePairs = cookieString.split(";");
                for (String pair : cookiePairs) {
                    if (!pair.trim().isEmpty()) {
                        String[] nameValue = pair.split("=", 2);
                        if (nameValue.length == 2) {
                            String name = nameValue[0].trim();
                            String value = nameValue[1].trim();

                            Cookie cookie = new Cookie.Builder()
                                    .name(name)
                                    .value(value)
                                    .domain(domain)
                                    .path("/")
                                    .build();

                            cookies.add(cookie);
                        }
                    }
                }
            }

            Log.d(TAG, "Loaded cookies for " + domain + ": " + cookieString);
            return cookies;
        }
    }

    // Cookie拦截器 - 用于获取和打印Cookie
    private class CookieInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            // 打印请求中的Cookie
            Log.d(TAG, "Request Cookies: " + request.headers("Cookie"));

            // 打印响应中的Set-Cookie
            if (!response.headers("Set-Cookie").isEmpty()) {
                Log.d(TAG, "Response Set-Cookie Headers:");
                for (String header : response.headers("Set-Cookie")) {
                    Log.d(TAG, "  " + header);
                }
            }

            return response;
        }
    }
}
