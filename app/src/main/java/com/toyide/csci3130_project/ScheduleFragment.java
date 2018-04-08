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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



public class ScheduleFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private static final String TAG = "test";
    private ListView ScheduleListView;
    private MyApplicationData appState;
    private ArrayList<String> cidList;
    private ArrayList<Courses> CourseList;
    private String cid;
    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        //course items that should be shown in the schedule
        CourseList = new ArrayList<Courses>();
        cidList = new ArrayList<String>();


        //Retrieve schedual information for current user
        final Profile myprofile = (Profile) getActivity().getIntent().getSerializableExtra("profile") ;
        //Set-up Firebase
        appState = (MyApplicationData) getActivity().getApplicationContext();
        appState.firebaseDBInstance = FirebaseDatabase.getInstance();
        appState.firebaseReference = appState.firebaseDBInstance.getReference("Registrations");
        appState.firebaseReference.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(myprofile.UserID).exists()) {
                    cid = (dataSnapshot.child(myprofile.UserID).child("CourseID").getValue(String.class));
                    Log.i(TAG, "MyClass.getView()  " + cid);
                    for (String s : cid.split(",")) {
                        if (!s.isEmpty())
                            cidList.add(s);
                    }
                    Log.i(TAG, "MyClass.getView()  " + cidList);
                    for (int i=0; i <getData.courses_list.size();i ++){
                        if( cidList.contains(getData.courses_list.get (i).CourseID.toString())){
                            CourseList.add(getData.courses_list.get(i));
                        }
                    }
                    Log.i(TAG, "MyClass.getView()  " + CourseList.toString());
                    ScheduleListView= (ListView) view.findViewById(R.id.lv_schedule);
                    final CourseListAdapter adapter = new CourseListAdapter(getContext(), R.layout.fragment_schedule, CourseList);
                    ScheduleListView.setAdapter(adapter);
                    //look within the ListView(fragment_schedule) layout for the element with id.lv_schedule
                }

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
    public  void setCidList(String Course){
        cid =  Course;
    }
    public void wait(String x){
        x = "";
    }
    public String getCList(){
        return cid;
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
