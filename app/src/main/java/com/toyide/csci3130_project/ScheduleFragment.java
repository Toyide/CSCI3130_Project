package com.toyide.csci3130_project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class ScheduleFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private static final String TAG = "test";

    private MyApplicationData appState;
    private ArrayList<String> cidList;
    private ArrayList<Courses> CourseList;
    private String cid;
    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        final String userId = LocalData.getUserID(); //Get userID from local
        Log.i("getView()", "it is  "+getData.courses_list);
        //course items that should be shown in the schedule
        cidList = new ArrayList<>();


        //Set-up Firebase
        appState = (MyApplicationData) getActivity().getApplicationContext();
        appState.firebaseDBInstance = FirebaseDatabase.getInstance();
        appState.firebaseReference = appState.firebaseDBInstance.getReference("Registrations");
        appState.firebaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cid = dataSnapshot.child(userId).child("CourseID").getValue(String.class);
                for (String s : cid.split(",")) {
                    for(Courses c : getData.courses_list) {
                        if (s.equals(c.CourseID.toString())) {
                            CourseList.add(c);
                            break;
                        }
                    }
                }



                //create a new CourseListAdapter object(CourseListAdapter.java)
                // turns the content of courseArrayList into things that the ListView(fragment_schedule) can display
                CourseListAdapter adapter = new CourseListAdapter(getContext(), R.layout.fragment_schedule, CourseList);

                //look within the ListView(fragment_schedule) layout for the element with id.lv_schedule
                ListView listView = (ListView) view.findViewById(R.id.lv_schedule);

                //use ListView(fragment_schedule) adapter to draw the things on the screen
                listView.setAdapter(adapter);



                //Set background color for different course type
                view.setBackgroundColor(0x00FF00);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Toast.makeText(context, "ScheduleFragment attached", Toast.LENGTH_SHORT).show();
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
