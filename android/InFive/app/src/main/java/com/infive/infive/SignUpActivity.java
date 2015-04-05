package com.infive.infive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class SignUpActivity extends ActionBarActivity {

    EditText mUsername;
    EditText mEmail;
    EditText mPassword;
    EditText mConfirmPassword;
    Context context;
    TextView loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loginButton = (TextView) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setTextColor(Color.parseColor("#4cc1ff"));
                showLogin();
            }

        });



        //Get handle, email, password, and confirm fields
        mUsername = (EditText) findViewById(R.id.username);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirmPassword);

    }

    public void showLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    public void signupButtonClicked (View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
