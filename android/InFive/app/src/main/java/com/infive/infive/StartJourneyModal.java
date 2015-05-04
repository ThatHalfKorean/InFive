package com.infive.infive;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.LocationManager;
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
 * {@link StartJourneyModal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StartJourneyModal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartJourneyModal extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String title;
    private String host;
    private String address;
    private String date;
    Context context;
    ProgressDialog dialog;
    LocationManager lm;
    GPSService mGPSService;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StartJourneyModal.
     */
    // TODO: Rename and change types and number of parameters
    public static StartJourneyModal newInstance(String param1, String param2) {
        StartJourneyModal fragment = new StartJourneyModal();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public StartJourneyModal() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        title = mArgs.getString("eventTitle");
        host = mArgs.getString("host");
        date = mArgs.getString("eventDate");
        address = mArgs.getString("address");
        this.context = getActivity().getApplicationContext();


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_start_journey_modal, container, false);
        getDialog().setTitle(title);
        TextView jHost = (TextView) rootView.findViewById(R.id.journeyHost);
        TextView jAddress = (TextView) rootView.findViewById(R.id.journeyAddress);
        TextView jTime = (TextView) rootView.findViewById(R.id.journeyTime);
        jHost.setText(host);
        notificationData.setHost(host);
        jAddress.setText(address);
        jTime.setText(date);
        final Button button = (Button) rootView.findViewById(R.id.startJourney);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startJourney();
                dismissModal();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    public JSONObject getMessageObjectRequestAsJson(String content) {
        JSONObject jsonParams = new JSONObject();
        JSONArray recipients = new JSONArray();
        recipients.put(host);

        try {
            jsonParams.put("content", content);
            jsonParams.put("recipients",recipients);
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

    public void startJourney(){
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mGPSService = new GPSService(context);
        mGPSService.getGPSCoordinates(address);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, mGPSService);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(context, ApiHelper.getLocalUrlForApi(getResources()) + "notifications",
                convertJsonUserToStringEntity(getMessageObjectRequestAsJson("is on the way!")), "application/json",
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
