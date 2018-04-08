package com.toyide.csci3130_project;
import android.app.Activity;
import android.content.Context;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private static final String TAG = "test";
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    FrameLayout progressBarHolder;
    private MyApplicationData appState;

    //Not needed in the registration

    private ListView RegistrationListView;
    private Button RegButton;
    private ArrayList<Courses> CourseList;

    private FirebaseListAdapter<Courses> firebaseAdapter;

    public String currentIDList;// selected courseIDList for later conflict check

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Retrieve schedual information for current user
        final String userId = LocalData.getUserID(); //Get userID from local

        // create a view instance to add the courseInfo
        final View view = inflater.inflate(R.layout.fragment_register, container, false);

        //Set-up Firebase
        appState = (MyApplicationData) getActivity().getApplicationContext();
        appState.firebaseDBInstance = FirebaseDatabase.getInstance();
        appState.firebaseReference = appState.firebaseDBInstance.getReference("Registrations").child(userId).child("CourseID");
        appState.firebaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentIDList = dataSnapshot.getValue(String.class);
                //course items that should be shown in the schedule
                CourseList = getData.courses_list;
                RegistrationListView= (ListView) view.findViewById(R.id.listView_Registration);
                RegButton= (Button) view.findViewById(R.id.RegisterButt);
                progressBarHolder = (FrameLayout) getActivity().findViewById(R.id.progressBarHolder);
                final RegistrationAdapter adapter = new RegistrationAdapter(getContext(), R.layout.fragment_register, CourseList, currentIDList);
                RegistrationListView.setAdapter(adapter);

                RegButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean checkLab_Tut = false;
                        boolean checkLec_count = false;
                        int LecCount = 0;
                        int num_Tub_Lab = 0;
                        ArrayList<String> tut_lab = new ArrayList();
                        boolean checkConflict = false;//false -> no conflict
                        ArrayList<Courses> curCourses = new ArrayList<Courses>();
                        for (Courses c : CourseList) {
                            for (String s : adapter.getCourseList().split(",")) {
                                if (s.equals(c.CourseID.toString())) {
                                    curCourses.add(c);
                                    if (c.CourseType.toString().equals("Lec")) {
                                        LecCount++;
                                        if (!c.TutID.toString().equals("00000")) {

                                            tut_lab.add(c.TutID);
                                        }
                                        if (!c.LabID.toString().equals("00000")) {
                                            tut_lab.add(c.LabID);
                                        }
                                    }
                                    if (c.CourseType.equals("Tut")){
                                        if (tut_lab.toString().indexOf(c.CourseID.toString()) != -1)

                                            num_Tub_Lab++;
                                    }
                                    if (c.CourseType.equals("Lab")){
                                        if (tut_lab.toString().indexOf(c.CourseID.toString()) !=- 1)
                                            num_Tub_Lab++;
                                    }
                                }
                            }
                        }
                        if(tut_lab.size() != num_Tub_Lab){
                            checkLab_Tut =true;
                        }
                        if(LecCount >5){
                            checkLec_count =true;
                        }
                        for (int i = 0; i < curCourses.size(); i++) {
                            for (int j = i + 1; j < curCourses.size(); j++) {
                                for (char day : curCourses.get(i).CourseWeekday.toCharArray()) {
                                    if (curCourses.get(j).CourseWeekday.indexOf(day) !=  -1) {
                                        String time1[] = curCourses.get(i).CourseTime.split("-");
                                        String time2[] = curCourses.get(j).CourseTime.split("-");
                                        if ((time1[0].compareTo(time2[0]) >= 0 && time1[0].compareTo(time2[1]) < 0) || (time2[0].compareTo(time1[0]) >= 0 && time2[0].compareTo(time1[1]) < 0)) {
                                            checkConflict = true;
                                            break;
                                        }
                                    }
                                }
                                if (checkConflict)
                                    break;
                            }
                            if (checkConflict)
                                break;
                        }
                        if (checkConflict == false &&checkLab_Tut == false &&checkLec_count == false) {
                            currentIDList = adapter.getCourseList();
                            appState.firebaseReference.setValue(currentIDList);
                            new MyTask().execute();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                                }
                            }, 2200);
                        }
                        else  if (checkConflict){
                            new MyTask().execute();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Conflict!", Toast.LENGTH_SHORT).show();
                                }
                            }, 2200);

                            //TODO disable register && conflict messages
                            //TODO update currentSpot

                        }
                        else if (checkLab_Tut){
                            new MyTask().execute();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Lecture, tut, lab is not paired!", Toast.LENGTH_SHORT).show();
                                }
                            }, 2200);
                        }
                        else {
                            new MyTask().execute();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "Five more courses chosen", Toast.LENGTH_SHORT).show();
                                }
                            }, 2200);
                        }

                    }

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            /*
            ArrayList<Courses> curCourses = new ArrayList<Courses>();
            for (String s : adapter.CourseIDString) {
                for (Courses c : CourseList) {
                    if (s.equals(c.CourseID))
                        curCourses.add(c);
                }
            }

            for (int i = 0; i < curCourses.size(); i++) {
                for (int j = i + 1; j < curCourses.size(); j++) {
                    for (char day : curCourses.get(i).CourseWeekday.toCharArray()) {
                        if (curCourses.get(j).CourseWeekday.indexOf(day) != -1) {
                            String time1[] = curCourses.get(i).CourseTime.split("-");
                            String time2[] = curCourses.get(j).CourseTime.split("-");

                        }
                    }

                }
            }
            */


            mListener.onFragmentInteraction(uri);
        }
    }
        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof OnFragmentInteractionListener) {
                mListener = (OnFragmentInteractionListener) context;
            } else {
                Toast.makeText(context, "RegisterFragment attached", Toast.LENGTH_SHORT).show();
            }
        }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RegButton.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            RegButton.setEnabled(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < 2; i++) {
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}