package com.toyide.csci3130_project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.sql.Array;
import java.util.ArrayList;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.toyide.csci3130_project.login;


public class ScheduleFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    private static String CourseID;

    //course items that should be shown in the schedule
    private static ArrayList<Course> CourseList = new ArrayList<>();

    //Retrieve schedual information for current user
    private String UserID ; //Get userID from local

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        Profile myprofile = (Profile) getActivity().getIntent().getSerializableExtra("profile") ;
        Map<String, Object> pros = myprofile.toMap();
        UserID = pros.get("UserID").toString();
        //Set-up Firebase
        MyApplicationData appState = (MyApplicationData) getActivity().getApplication();
        appState.firebaseDBInstance = FirebaseDatabase.getInstance();
        //Go to "Registrations" table
        appState.firebaseReference = appState.firebaseDBInstance.getReference("Registrations");
        //Look up a specific userID in "Registrations" table
        appState.firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(UserID).exists()) {
                    String t = dataSnapshot.child("Registration1").child("CourseID").getValue().toString();
                    CourseID = t;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Transfer to "Courses" table
        appState.firebaseReference = appState.firebaseDBInstance.getReference("Courses");
        //Look up courses according to their courseID
        String[] courses = CourseID.split(",");
        for(int i = 0; i < courses.length-1; i++) {
            final String CourseID = courses[i];
            appState.firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String CourseTitle="CourseTitle", CourseType="CourseType", CourseWeekday="CourseWeekday", CourseTime="CourseTime", CourseInfo="CourseInfo";
                    if (dataSnapshot.child(CourseID).exists()) {
                        Course course = dataSnapshot.getValue(Course.class);
                        CourseTitle = course.getCourseTitle();
                        CourseType = course.getCourseType();
                        CourseWeekday = course.getCourseWeekday();
                        CourseTime = course.getCourseTime();
                        CourseInfo = course.getCourseInfo();
                    }
                    Course C = new Course(
                            CourseTitle,
                            CourseType,
                            CourseWeekday,
                            CourseTime,
                            CourseInfo
                    );
                    CourseList.add(C);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        //create a new CourseListAdapter object(CourseListAdapter.java)
        //turns the content of courseArrayList into things that the ListView(fragment_schedule) can display
        CourseListAdapter adapter = new CourseListAdapter(getContext(), R.layout.fragment_schedule, CourseList);

        //look within the ListView(fragment_schedule) layout for the element with id.lv_schedule
        ListView listView = (ListView) view.findViewById(R.id.lv_schedule);

        //use ListView(fragment_schedule) adapter to draw the things on the screen
        listView.setAdapter(adapter);

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
