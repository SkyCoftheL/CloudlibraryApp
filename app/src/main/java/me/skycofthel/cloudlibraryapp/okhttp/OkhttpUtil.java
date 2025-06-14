package me.skycofthel.cloudlibraryapp.okhttp;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkhttpUtil {

    private  OkHttpClient okHttpClient;
    private static final String TAG="OkhttpUtil";

    private static  OkhttpUtil INSTANCE;

    private static final long CACHE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String CACHE_DIR = "http_cache";
    private final Context context;

//    private OkhttpUtil() {

//        File cacheDir = new File(Environment.getDataDirectory(), CACHE_DIR);
//        Cache cache = new Cache(cacheDir, CACHE_SIZE);
//        //        要有一个客户端，浏览器
//        okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)  // 连接超时
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10,TimeUnit.SECONDS)
//                .cookieJar(new PersistentCookieJar())
//                .cache(cache)
//                .build();
//    }


    public OkhttpUtil(Context context) {
        this.context=context;
    }

    public static OkhttpUtil getInstance() {
        OkHttpClient client = OkHttpSingleton.getInstance(getInstance().context).getClient();


        return INSTANCE;
    }


    public Call getRequestOk(String url, String api){

        Log.d(TAG, "getRequestOk: on get method");

        Request request=new Request.Builder()
                .get()
                .url(url+api)
                .build();
//        用浏览器去创建连接
        Call task = okHttpClient.newCall(request);
//        异步请求

        return task;

    }

//    public Call postRequestOk(String url, String api, Map<String, String> params){
//
//
//        Gson gson=new Gson();
////        User user=new User("admin@cloudlibrary.com","admin");
//
//        String urlParams = gson.toJson(params);
//        MediaType mediaType=MediaType.parse("application/json");
////        RequestBody requestBody=RequestBody.create(urlParams,mediaType);
//        List<String> username=new ArrayList<>();
//        username.add("email");
//        username.add("password");
//        List<String> passwd=new ArrayList<>();
//        passwd.add("1");
//        passwd.add("123456");
//        RequestBody requestBody=new FormBody(username,passwd);
//        Log.d(TAG, "postRequestOk: the requestBody is "+requestBody);
//
//        Request request=new Request.Builder()
//                .post(requestBody)
//                .url(url+api)
//                .build();
//        Log.d(TAG, "postRequestOk: urlParams are "+urlParams);
//
//        Log.d(TAG, "postRequestOk: the request body is "+ request);
//
//        Call task = okHttpClient.newCall(request);
//
//
//
//
////        task.enqueue(new Callback() {
////            @Override
////            public void onFailure(@NonNull Call call, @NonNull IOException e) {
////                Log.d(TAG, "onFailure: ");
////            }
////
////            @Override
////            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
////                int code = response.code();
////                Log.d(TAG, "onResponse: response code is "+code);
////                ResponseBody body = response.body();
////                Log.d(TAG, "onResponse: response code is "+body.string());
////
////
////            }
////        });
//
//        return task;
//    }

    public Call postRequestOk(String url, String api,FormBody getFormBody){

        FormBody formBody=getFormBody;


//        FormBody formBody = new FormBody.Builder()
//                .add("email", email)
//                .add("password", passwd)
//                .build();



        Request request=new Request.Builder()
                .post(formBody)
                .url(url+api)
                .build();


        Call task = okHttpClient.newCall(request);


        return task;
    }
}
