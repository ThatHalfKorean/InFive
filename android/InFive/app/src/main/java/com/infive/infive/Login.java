package com.infive.infive;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Login extends ActionBarActivity {
    TextView signUpButton;
    Context context;
    ProgressDialog dialog;
    EditText mUsername;
    EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.context = getApplicationContext();

        signUpButton = (TextView) findViewById(R.id.signUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpButton.setTextColor(Color.parseColor("#4cc1ff"));
                showSignUp();
            }
        });
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
    }

    public JSONObject getUserObjectRequestAsJson() {
        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("username", mUsername.getText().toString());
            jsonParams.put("password", mPassword.getText().toString());
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

    public void attemptLogin() {
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(context, ApiHelper.getLocalUrlForApi(getResources()) + "sessions",
                convertJsonUserToStringEntity(getUserObjectRequestAsJson()), "application/json",
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        dialog = ProgressDialog.show(Login.this, "",
                                "Loading. Please wait...", true);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        dialog.dismiss();
                        String s = new String(response);
                        String responseText = null;
                        JSONObject returnVal = new JSONObject();
                        try {
                            returnVal = new JSONObject(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            responseText = new JSONObject(new String(response)).getString("response");
                            Toast toast = Toast.makeText(Login.this.context, responseText, Toast.LENGTH_LONG);
                            toast.show();
                        } catch (JSONException j) {

                        }
                        try {
                            ApiHelper.saveAuthorizationToken(context,
                                    returnVal.getString("token"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(Login.this.context, MainActivity.class);
                        startActivity(intent);
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

    public void showSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    public void loginClicked(View view) {
        attemptLogin();
    }
}
