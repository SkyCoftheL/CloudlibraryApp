package me.skycofthel.cloudlibraryapp.okhttp;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpSingleton {
    private static OkHttpSingleton instance;
    private OkHttpClient client;
    private static final long CACHE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String CACHE_DIR = "http_cache";
    private static final String PREFS_NAME = "okhttp_cookies";

    private OkHttpSingleton(Context context) {
        // 创建缓存目录
        File cacheDir = new File(context.getCacheDir(), CACHE_DIR);
        Cache cache = new Cache(cacheDir, CACHE_SIZE);

        // 构建 OkHttpClient
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .cache(cache)
                .cookieJar(new PersistentCookieJar(context))
                .addInterceptor(new AuthInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    // 获取单例实例
    public static synchronized OkHttpSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new OkHttpSingleton(context.getApplicationContext());
        }
        return instance;
    }

    // 获取 OkHttpClient 实例
    public OkHttpClient getClient() {
        return client;
    }

    // 持久化 Cookie 的实现
    private static class PersistentCookieJar implements CookieJar {
        private final SharedPreferences prefs;
        private final String COOKIE_KEY = "cookies";

        public PersistentCookieJar(Context context) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            StringBuilder sb = new StringBuilder();
            for (Cookie cookie : cookies) {
                sb.append(cookie.name()).append("=").append(cookie.value()).append("; ");
            }
            prefs.edit().putString(url.host(), sb.toString()).apply();
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            String cookieString = prefs.getString(url.host(), "");
            return parseCookies(url, cookieString);
        }

        private List<Cookie> parseCookies(HttpUrl url, String cookieString) {
            // 简化实现，实际需要解析 Cookie 字符串

            return new java.util.ArrayList<>();
        }
    }

    // 添加认证头的拦截器
    private static class AuthInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder builder = original.newBuilder();

            // 添加通用请求头
            builder.header("User-Agent", "YourApp/1.0");

            // 如果有 Token，添加到请求头
            String token = getSavedToken();
            if (token != null) {
                builder.header("Authorization", "Bearer " + token);
            }

            return chain.proceed(builder.build());
        }

        private String getSavedToken() {
            // 从本地存储获取 Token

            return null; // 需根据实际情况实现
        }
    }
}