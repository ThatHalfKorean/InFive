package com.infive.infive;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.facebook.Session;

import java.security.MessageDigest;
import java.util.ArrayList;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;


public class Login extends ActionBarActivity {

    private UiLifecycleHelper uihelper;

    void showMsg(String string)
    {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }


    private Session.StatusCallback callback =new Session.StatusCallback()
    {

        @Override
        public void call(Session session, SessionState state, Exception exception)
        {
            onSessionStateChange(session,state,exception);
        }
    };


    void onSessionStateChange(Session session, SessionState state, Exception exception)
    {
        if (state.isOpened())
        {
            Log.i("facebook", "Logged in...");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            
            Request.newMeRequest(session, new Request.GraphUserCallback()
            {

                @Override
                public void onCompleted(GraphUser user, Response response)
                {

                    if(user!=null)
                    {
                        showMsg(user.getName());
                        showMsg(user.getProperty("email")+"");
                        showMsg(user.getProperty("gender")+"");
                        showMsg(user.getId()+"");
                    }
                    else
                    {
                        showMsg("its null");
                        showMsg(response.getError().getErrorMessage());
                    }
                }
            }).executeAsync();

        }
        else if (state.isClosed())
        {
            Log.i("facebook", "Logged out...");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        uihelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uihelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uihelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uihelper.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uihelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uihelper =new UiLifecycleHelper(this,callback);
        uihelper.onCreate(savedInstanceState);

        ArrayList<String> permission =new ArrayList<String>();
        permission.add("email");
        permission.add("public_profile");
        permission.add("user_friends");

        LoginButton btn=(LoginButton)findViewById(R.id.fbbtn);
        btn.setPublishPermissions(permission);


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.infive.infive",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
