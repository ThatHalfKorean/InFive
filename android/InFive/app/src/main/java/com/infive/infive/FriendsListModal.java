package com.infive.infive;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendsListModal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsListModal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsListModal extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView friendList;
    String address;
    String eventTitle;
    String dateTime;
    ProgressDialog dialog;
    FriendInviteAdapter adapter;
    FriendInvite friend_data[];
    Context context;
    Button createEventButton;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsListModal.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsListModal newInstance(String param1, String param2) {
        FriendsListModal fragment = new FriendsListModal();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FriendsListModal() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle mArgs = getArguments();
        address = mArgs.getString("location");
        eventTitle = mArgs.getString("event");
        dateTime = mArgs.getString("when");
        this.context = getActivity().getApplicationContext();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void setFriendAdapter(FriendInviteAdapter friendInviteAdapter) {
        this.adapter = friendInviteAdapter;
    }

    public void getFriends(final View view) {
        AsyncHttpClient client = new AsyncHttpClient();
        String token = ApiHelper.getSessionToken(context);
        RequestParams params = new RequestParams();

        client.addHeader("Authorization", token);
        client.get(context, ApiHelper.getLocalUrlForApi(getResources()) + "friends",
                params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        dialog = ProgressDialog.show(getActivity(), "",
                                "Loading. Please wait...", true);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseText = null;

                        try {
                            responseText = new JSONObject(new String(responseBody)).getString("response");
                            JSONArray y = new JSONArray(responseText);
                            friend_data = new FriendInvite[y.length()];

                            for (int x = 0; x < y.length(); x++) {

                                friend_data[x] = new FriendInvite(y.get(x).toString(), false);
                            }

                            final FriendInviteAdapter adapter = new FriendInviteAdapter(getActivity(),
                                    R.layout.friend_invite_item, friend_data);
                            FriendsListModal.this.setFriendAdapter(adapter);
                            friendList = (ListView) view.findViewById(R.id.friendsListModal);
                            friendList.setAdapter(adapter);

                        } catch (JSONException j) {

                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable error) {
                        dialog.dismiss();
                        String responseText = null;

                        try {

                            responseText = new JSONObject(new String(errorResponse)).getString("reason");
                        } catch (JSONException j) {

                        }

                    }
                });
    }


    public JSONObject getMessageObjectRequestAsJson() {
        JSONObject jsonParams = new JSONObject();
        JSONArray friends = new JSONArray();
        for (int i = 0; i < friend_data.length; i++) {
            if (friend_data[i].isSelected()) {
                friends.put(friend_data[i].friend);
            }
        }
        try {
            jsonParams.put("eventTitle", eventTitle);
            jsonParams.put("address", address);
            jsonParams.put("recipients", friends);
            jsonParams.put("eventDate", dateTime);
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

    public void attemptCreateEvent() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(context, ApiHelper.getLocalUrlForApi(getResources()) + "events",
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

    public void dismissModal() {
        this.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_friends_list_modal, container, false);
        getDialog().setTitle("Add Friends");
        getFriends(rootView);
        createEventButton = (Button) rootView.findViewById(R.id.sendInvite);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean postMessage = false;
                for (int i = 0; i < friend_data.length; i++) {
                    if (friend_data[i].isSelected()) {
                        postMessage = true;
                    }
                }
                if (postMessage) {
                    attemptCreateEvent();
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("No Friends Selected")
                            .setMessage("Please select a friend.")
                            .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
