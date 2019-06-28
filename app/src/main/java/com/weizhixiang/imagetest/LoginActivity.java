package com.weizhixiang.imagetest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weizhixiang.imagetest.data.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    BmobUser bmobUser = new BmobUser();
    public static final String USERNAME="com.weizhixang.imagetest.LoginActivity.USERNAME";
    private Boolean bPwdSwitch = false;
    private EditText etPwd;
    private EditText etAccount;
    private CheckBox cbRememberPwd;
    private ImageView ivPwdSwitch;
    private Button btLogin;
    private TextView btSignUp;
    boolean iscorrect = false;
    User user = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "9bfe7b07ff60f8bc689320203b96149d");
        setContentView(R.layout.activity_login);
        ivPwdSwitch = findViewById (R.id.iv_pwd_switch );
        etPwd = findViewById (R.id.et_pwd );
        etAccount = findViewById (R.id.et_account );
        cbRememberPwd = findViewById (R.id.cb_remember_pwd );
        btLogin = findViewById (R.id.bt_login );
        btSignUp = findViewById(R.id.tv_sign_up);


        initCheck();

        ivPwdSwitch.setOnClickListener (this);

        btLogin.setOnClickListener(this);

        btSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pwd_switch:SwitchClick();
            break;
            case R.id.bt_login :
                SaveCount();
                Login();
                Intent mainintent = new Intent();
                ComponentName componentName = new ComponentName(LoginActivity.this, MainActivity.class);
                mainintent.setComponent(componentName);
                mainintent.putExtra(LoginActivity.USERNAME,user.getUsername());
                //startActivityForResult(mainintent, REQUEST_CODE_LOGINACTIVITY);
                startActivity(mainintent);
                finish();
            break;
            case R.id.tv_sign_up : IntentSignUp();
            break;
        }
    }

    public void IntentSignUp(){
        Intent signintent = new Intent();
        ComponentName componentName = new ComponentName(LoginActivity.this,SignUpActivity.class);
        signintent.setComponent(componentName);
        startActivity(signintent);
        finish();
    }

    public void IntentMain(){
        Intent mainintent = new Intent();
        if (iscorrect == true) {
            ComponentName componentName = new ComponentName(LoginActivity.this, MainActivity.class);
            mainintent.setComponent(componentName);
            mainintent.putExtra(LoginActivity.USERNAME,user.getUsername());
            //startActivityForResult(mainintent, REQUEST_CODE_LOGINACTIVITY);
            startActivity(mainintent);
            finish();
        }
    }

    //CheckBox点击事件
    public void SwitchClick(){
        bPwdSwitch = ! bPwdSwitch;
        if(bPwdSwitch) {
            ivPwdSwitch.setImageResource (
                    R.drawable.ic_visibility_black_24dp );
            etPwd.setInputType (
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            ivPwdSwitch.setImageResource (
                    R.drawable.ic_visibility_off_black_24dp );
            etPwd.setInputType (
                    InputType.TYPE_TEXT_VARIATION_PASSWORD |
                            InputType.TYPE_CLASS_TEXT);
            etPwd.setTypeface(Typeface.DEFAULT);
        }
    }

    //保存账号信息
    public void SaveCount(){
        String spFileName = getResources ()
                .getString (R.string.shared_preferences_file_name );
        String accountKey = getResources ()
                .getString (R.string.login_account_name );
        String passwordKey = getResources ()
                .getString (R.string.login_password );
        String rememberPasswordKey = getResources ()
                .getString (R.string.login_remember_password );
        SharedPreferences spFile = getSharedPreferences (
                spFileName ,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spFile.edit ( );
        if (cbRememberPwd.isChecked ( ) ) {
            String password = etPwd.getText ( ).toString ( );
            String account = etAccount.getText ( ).toString ( );
            editor.putString ( accountKey , account );
            editor.putString ( passwordKey , password );
            editor.putBoolean ( rememberPasswordKey , true );
            editor.apply ( );
        } else {
            editor.remove ( accountKey );
            editor.remove ( passwordKey );
            editor.remove ( rememberPasswordKey );
            editor.apply ( );
        }
    }

    public void initCheck(){     //检查是否记住密码
        Intent receive = getIntent();
        String spFileName = getResources ()
                .getString (R.string.shared_preferences_file_name);
        String accountKey = getResources ()
                .getString (R.string.login_account_name );
        String passwordKey = getResources ()
                .getString (R.string.login_password );
        String rememberPasswordKey = getResources ()
                .getString (R.string.login_remember_password );
        SharedPreferences spFile = getSharedPreferences (
                spFileName ,
                MODE_PRIVATE);
        String account = spFile .getString ( accountKey , null );
        String password = spFile .getString ( passwordKey , null );
        Boolean rememberPassword = spFile .getBoolean (
                rememberPasswordKey ,
                false );
        if ( account != null && ! TextUtils.isEmpty ( account )) {
            etAccount .setText ( account );
        }
        if ( password != null && ! TextUtils .isEmpty ( password )) {
            etPwd .setText ( password );
        }
        cbRememberPwd .setChecked ( rememberPassword );
        account = receive.getStringExtra(SignUpActivity.USERNAME);
        password = receive.getStringExtra(SignUpActivity.PASSWORD);
        if(account!=null){
            etAccount .setText (account);
            etPwd .setText (password);
        }
    }
    private void Login(){
        BmobQuery<User> query= new BmobQuery<User>();;
        user.setUsername(etAccount.getText().toString());
        user.setPassword(etPwd.getText().toString());
        user.login(LoginActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                iscorrect = true;

                Toast.makeText(LoginActivity.this, "Login success",3*1000).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(LoginActivity.this, s,3*1000).show();
            }
        });
    }
}
