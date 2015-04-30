package com.infive.infive;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SearchActivity extends ActionBarActivity {

    private ListView userList;
    Context context;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context = this.getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }


    public void getUsers(View view) {
        AsyncHttpClient client = new AsyncHttpClient();

        //Static call to get token
        String token = ApiHelper.getSessionToken(context);
        String searchInput = ((EditText)findViewById(R.id.search_bar)).getText().toString();

        client.addHeader("Authorization", token);
        client.addHeader("Search", searchInput);
        client.get(context, ApiHelper.getLocalUrlForApi(getResources()) + "users", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // Display spinner
                dialog = ProgressDialog.show(SearchActivity.this, "",
                        "Loading. Please wait...", true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                String responseText = null;

                try {
                    responseText = new JSONObject(new String(responseBody)).getString("response");
                    JSONArray y = new JSONArray(responseText);
                    User user_data[] = new User[y.length()];

                    for (int x = 0; x < y.length(); x++) {
                        user_data[x] = new User(y.get(x).toString());
                    }

                    final UserAdapter adapter = new UserAdapter(SearchActivity.this,
                            R.layout.user_item, user_data);
                    userList = (ListView) findViewById(R.id.searchList);
                    userList.setAdapter(adapter);

                } catch (JSONException j) {

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable error) {
                dialog.dismiss();
                String responseText = null;
                try{
                        responseText = new JSONObject(new String(errorResponse)).getString("reason");

                } catch (JSONException j) {

                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    public void logoutUser () {
        AsyncHttpClient client = new AsyncHttpClient();
        String token = ApiHelper.getSessionToken(context);
        client.addHeader("Authorization", token);

        client.delete(this.getApplicationContext(), ApiHelper.getLocalUrlForApi(getResources())+"sessions",
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        Intent intent = new Intent(SearchActivity.this.context, Login.class);
                        startActivity(intent);
                        finish();

                        Toast toast = Toast.makeText(SearchActivity.this.context, "Goodbye.", Toast.LENGTH_LONG);
                        toast.show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        String responseText = null;

                        try {
                            responseText = new JSONObject(new String(errorResponse)).getString("reason");
                            Toast toast = Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG);
                            toast.show();
                        } catch (JSONException j) {

                        }
                    }

                    @Override
                    public void onRetry(int retryNo) {

                    }
                });
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
        if (id == R.id.action_logout) {
            logoutUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
