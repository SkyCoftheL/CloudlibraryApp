package me.skycofthel.cloudlibraryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import me.skycofthel.cloudlibraryapp.ui.login.LoginActivity;

public class StartActivity extends Activity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: 2025/6/1 判断用户类型，确定需要跳转的界面
        jumpIntentActivity();
    }

    private void jumpIntentActivity() {
        String role = sharedPreferences.getString("userRole", "");
        if (role.equals("ADMIN")) {
            startActivity(new Intent(this,MainActivityForAdmin.class));
            finish();
        }
        else if (role.equals("USER")) {
            startActivity(new Intent(this,MainActivityForNormal.class));
            finish();
        }
        else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

    }
}