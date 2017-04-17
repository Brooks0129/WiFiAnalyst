package com.infor.wifi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.infor.wifi.R;
import com.infor.wifi.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private AutoCompleteTextView email;
    private EditText password;
    private Button sign_in;
    private ProgressBar progressBar;
    private Button sign_up;
    private Button jump;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        sign_in = (Button) findViewById(R.id.sign_in_button);
        sign_up = (Button) findViewById(R.id.sign_up_button);
        jump = (Button) findViewById(R.id.jump_button);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        User currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = email.getText().toString();
                String sPassword = password.getText().toString();
                if (!sEmail.isEmpty() && !sPassword.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    User user = new User();
                    user.setUsername(sEmail);
                    user.setPassword(sPassword);
                    user.login(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            progressBar.setVisibility(View.GONE);
                            if (e == null) {
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "参数不合法", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = email.getText().toString();
                String sPassword = password.getText().toString();
                if (!sEmail.isEmpty() && !sPassword.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    User user = new User();
                    user.setUsername(sEmail);
                    user.setPassword(sPassword);
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            progressBar.setVisibility(View.GONE);
                            if (e == null) {
                                Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "参数不合法", Toast.LENGTH_SHORT).show();
                }
            }
        });
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

