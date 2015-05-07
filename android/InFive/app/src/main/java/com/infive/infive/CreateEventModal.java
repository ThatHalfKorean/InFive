package com.infive.infive;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateEventModal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateEventModal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventModal extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    View root;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateEventModal.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEventModal newInstance(String param1, String param2) {
        CreateEventModal fragment = new CreateEventModal();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateEventModal() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        getDialog().setTitle("Create Event");
        View rootview = inflater.inflate(R.layout.fragment_create_event_modal, container, false);

        final Button button = (Button) rootview.findViewById(R.id.getFriendsListButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addFriends();
                dismissModal();
            }
        });
        root = rootview;
        return rootview;

    }

    public void addFriends() {
        EditText eventTitle = (EditText) root.findViewById(R.id.eventName);
        EditText address = (EditText) root.findViewById(R.id.address);
        EditText city = (EditText) root.findViewById(R.id.city);
        EditText state = (EditText) root.findViewById(R.id.state);
        EditText zip = (EditText) root.findViewById(R.id.zip);
        EditText month = (EditText) root.findViewById(R.id.month);
        EditText day = (EditText) root.findViewById(R.id.day);
        EditText year = (EditText) root.findViewById(R.id.year);
        EditText hour = (EditText) root.findViewById(R.id.hour);
        EditText min = (EditText) root.findViewById(R.id.minute);

        Bundle args = new Bundle();
        String eventName = eventTitle.getText().toString();
        String fullAddress = address.getText().toString() + ", " + city.getText().toString() + ", " + state.getText().toString() + ", " + zip.getText().toString();
        String dateTime = year.getText().toString()+"-"+month.getText().toString()+"-"+day.getText().toString()+"T"+hour.getText().toString()+":"+min.getText().toString()+"Z";
        args.putString("event", eventName);
        args.putString("location", fullAddress);
        args.putString("when", dateTime);
        FriendsListModal newFragment = new FriendsListModal();
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "dialog");
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
