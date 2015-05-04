package com.infive.infive;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFriendModal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFriendModal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFriendModal extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String newFriend;
    Context context;
    ProgressDialog dialog;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFriend.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFriendModal newInstance(String param1, String param2) {
        AddFriendModal fragment = new AddFriendModal();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddFriendModal() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        this.context = getActivity().getApplicationContext();
        newFriend = mArgs.getString("friend");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_friend, container, false);
        getDialog().setTitle("Add Friend");
        TextView confirmation = (TextView) rootView.findViewById(R.id.newFriend);
        confirmation.setText("Are you sure you want to add "+newFriend+"?");
        final Button button = (Button) rootView.findViewById(R.id.confirmAdd);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addFriend();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }
    public void addFriend() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(context, ApiHelper.getLocalUrlForApi(getResources()) + "friends",
                convertJsonUserToStringEntity(getMessageObjectRequestAsJson()), "application/json",
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        dialog = ProgressDialog.show(getActivity(), "",
                                "Loading. Please wait...", true);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        String responseText = null;
                        try {
                            responseText = new JSONObject(new String(response)).getString("response");
                        } catch (JSONException j) {

                        }

                        dismissModal();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        dialog.dismiss();
                        String responseText = null;

                        try {
                            responseText = new JSONObject(new String(errorResponse)).getString("reason");

                        } catch (JSONException j) {

                        }
                        dismissModal();
                    }

                    @Override
                    public void onRetry(int retryNo) {

                    }
                });
    }

    public JSONObject getMessageObjectRequestAsJson() {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("newFriend", newFriend);
            jsonParams.put("Authorization", ApiHelper.getSessionToken(context));

        } catch (JSONException j) {

        }

        return jsonParams;
    }

    public StringEntity convertJsonUserToStringEntity(JSONObject jsonParams) {
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException i) {
            System.out.println(i);
        }

        return entity;
    }

    public void dismissModal() {
        this.dismiss();
    }




}
