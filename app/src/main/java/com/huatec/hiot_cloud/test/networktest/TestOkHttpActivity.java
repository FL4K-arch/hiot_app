package com.huatec.hiot_cloud.test.networktest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.huatec.hiot_cloud.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestOkHttpActivity extends AppCompatActivity {


//    private static  final String  baseUrl= "https://www.baidu.com";
    private static  final String  baseUrl= "http://114.67.88.191:8080/";
    private static final String TAG = "TestOkHttpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ok_http);

        //exexute方法测试
        //btnOkhttpExecute
        Button btnExecute = findViewById(R.id.btn_okhttp_execute);
        btnExecute.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                testExecute();
            }
        });

        //enqueue方法测试
        Button btnEnqueue = findViewById(R.id.btn_okhttp_enqueue);
        btnEnqueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testEnqueue();
            }
        });

        //登录测试
        Button btnLogin = findViewById(R.id.btn_okhttp_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login("","","");
            }
        });
        //获取用户信息
        Button btnGetUserInfo = findViewById(R.id.btn_okhttp_userinfo);
        btnGetUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInfo("");
            }
        });
        //更改邮箱账号
        Button btnUpdateEmail = findViewById(R.id.btn_okhttp_update_email);
        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmail("","");
            }
        });
    }

    /**
     * 修改邮箱
     * @param authorization
     * @param email
     */
    private void updateEmail(String authorization, String email) {
        OkHttpClient okHttpClint = new OkHttpClient();
        FormBody body = new FormBody.Builder().build();
        String url = baseUrl + "/user/email?email="+ email ;
        Request request = new Request.Builder().put(body).url(url).header("Authorization",authorization).build();
        callEnqueue(okHttpClint, request);

    }

    private void callEnqueue(OkHttpClient okHttpClint, Request request) {
        okHttpClint.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage(), e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    /**
     * 获取用户信息
     * @param authorization
     */
    private void getUserInfo(String authorization) {
        OkHttpClient okHttpClint = new OkHttpClient();
        FormBody body = new FormBody.Builder().build();
        String url = baseUrl + "/user/one";
        Request request = new Request.Builder().get().url(url).header("Authorization",authorization).build();
        callEnqueue(okHttpClint, request);
    }

    /**
     *
     * @param userName
     * @param password
     * @param loginCode
     */
    private void login(String userName, String password, String loginCode) {
        OkHttpClient okHttpClint = new OkHttpClient();
        FormBody body = new FormBody.Builder().build();
        String url = baseUrl + String.format("/auth/login?username=%s&password=%s&loginCode=%s",
                userName,password,loginCode);
        Request request = new Request.Builder().post(body).url(url).build();
        callEnqueue(okHttpClint, request);
    }


    private void testExecute() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                OkHttpClient okHttpClint = new OkHttpClient();
                Request request = new Request.Builder().url(baseUrl).build();
                try {
                    Response response = okHttpClint.newCall(request).execute();
                    Log.d(TAG, "run: "+response.body().string());
                } catch (IOException e) {
                    Log.e(TAG, "testExecute:"+e.getMessage(),e );
                }
            }
        }.start();
    }
    private void testEnqueue() {
        OkHttpClient okHttpClint = new OkHttpClient();
        Request request = new Request.Builder().url(baseUrl).build();
        callEnqueue(okHttpClint, request);
    }
}