package com.weizhixiang.imagetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weizhixiang.imagetest.data.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SaveListener;

public class SignUpActivity extends AppCompatActivity {
    private EditText mPwd;
    private EditText mPwdis;
    private EditText mAccount;
    private Button mSignUp;
    private Button mlogin;
    public static final String USERNAME="com.weizhixang.imagetest.USERNAME";
    public static final String PASSWORD="com.weizhixang.imagetest.PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAccount = findViewById(R.id.sign_user);
        mPwd = findViewById(R.id.sing_pass);
        mPwdis = findViewById(R.id.singis_pass);
        mSignUp = findViewById(R.id.bt_sign);
        mlogin = findViewById(R.id.bt_login);

        mSignUp.setOnClickListener(SignUpClick);

        mlogin.setOnClickListener(ReLogin);
    }

    private View.OnClickListener ReLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent reintent = new Intent(SignUpActivity.this,LoginActivity.class);
            reintent.putExtra(SignUpActivity.USERNAME,mAccount.getText().toString());
            reintent.putExtra(SignUpActivity.PASSWORD,mPwd.getText().toString());
            startActivity(reintent);
            finish();
        }
    };

    private View.OnClickListener SignUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String sAccount = mAccount.getText().toString();
            String sPwd = mPwd.getText().toString();
            String sPwdis = mPwdis.getText().toString();
            if(sPwd.equals(sPwdis)){
                doRegister();
            }
            else {
                Toast.makeText(SignUpActivity.this,"请从新确认密码",Toast.LENGTH_LONG).show();

            }

        }
    };

    private void doRegister(){
        BmobQuery<User> query= new BmobQuery<User>();
        User user = new User();
        user.setUsername(mAccount.getText().toString());
        user.setPassword(mPwdis.getText().toString());
        user.signUp(SignUpActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(SignUpActivity.this, "register success",3*1000).show();
            }
            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(SignUpActivity.this, s,3*1000).show();
            }
        });
    }

}
