package com.infive.infive;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpcomingEvents.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpcomingEvents#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingEvents extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView eventList;
    Context context;
    ProgressDialog dialog;
    EventAdapter adapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingEvents.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingEvents newInstance() {
        UpcomingEvents fragment = new UpcomingEvents();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public UpcomingEvents() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.context = getActivity().getApplicationContext();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_upcoming_events, container, false);
        getEvents(rootView);
        // Inflate the layout for this fragment
        return rootView;
    }

    public void setEventAdapter (EventAdapter eventAdapter) {
        this.adapter = eventAdapter;
    }
    public void getEvents(final View view) {
        AsyncHttpClient client = new AsyncHttpClient();
        String token = ApiHelper.getSessionToken(context);
        RequestParams params = new RequestParams();

        client.addHeader("Authorization", token);
        client.get(context, ApiHelper.getLocalUrlForApi(getResources()) + "eventsFeed",
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
                            Event event_data[] = new Event[y.length()];

                            for (int x = 0; x < y.length(); x++) {

                                event_data[x] = new Event(new JSONObject(y.get(x).toString()));
                            }

                            final EventAdapter adapter = new EventAdapter(getActivity(),
                                    R.layout.event_item, event_data);
                            UpcomingEvents.this.setEventAdapter(adapter);
                            eventList = (ListView) view.findViewById(R.id.upcomingEventsListView);
                            eventList.setAdapter(adapter);

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
