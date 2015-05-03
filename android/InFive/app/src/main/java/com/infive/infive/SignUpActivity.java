package com.infive.infive;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.loopj.android.http.*;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

public class SignUpActivity extends ActionBarActivity {

    EditText mUsername;
    EditText mEmail;
    EditText mPassword;
    EditText mConfirmPassword;
    Context context;
    TextView loginButton;
    ProgressDialog dialog;

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

    public JSONObject getUserObjectRequestAsJson() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("username", mUsername.getText().toString());
            jsonParams.put("email", mEmail.getText().toString());
            jsonParams.put("password", mPassword.getText().toString());
            jsonParams.put("confirmPassword", mConfirmPassword.getText().toString());
        } catch (JSONException j) {

        }
        return jsonParams;
    }

    public StringEntity convertJsonUserToStringEntity(JSONObject jsonParams) {
        StringEntity entity = null;

        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException i) {

        }

        return entity;
    }

    public void attemptCreateAccount() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(context, ApiHelper.getLocalUrlForApi(getResources()) + "signup",
                convertJsonUserToStringEntity(getUserObjectRequestAsJson()), "application/json",
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        dialog = ProgressDialog.show(SignUpActivity.this, "",
                                "Loading. Please wait...", true);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        dialog.dismiss();
                        String s = new String(response);
                        String responseText = null;

                        try {
                            responseText = new JSONObject(new String(response)).getString("response");
                        } catch (JSONException j) {

                        }

                        Toast toast = Toast.makeText(SignUpActivity.this.context, responseText, Toast.LENGTH_LONG);
                        toast.show();
                        showLogin();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        dialog.dismiss();
                        String responseText = null;

                        try {
                            responseText = new JSONObject(new String(errorResponse)).getString("reason");
                            Toast toast = Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG);
                            toast.show();
//                            }
                        } catch (JSONException j) {

                        }
                        e.printStackTrace();
                    }

                    @Override
                    public void onRetry(int retryNo) {

                    }
                });
    }

    public void signupButtonClicked(View view) {
        attemptCreateAccount();
    }
}
