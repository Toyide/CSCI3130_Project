package com.toyide.csci3130_project;
import android.app.Activity;
import android.content.Context;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.google.firebase.database.Transaction;
import com.google.firebase.database.MutableData;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
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
    private Context mContext;
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
        final Context a = mContext;
        appState.firebaseReference = appState.firebaseDBInstance.getReference("Courses");
        appState.firebaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> courseSnapshot =dataSnapshot.getChildren();
                final ArrayList<Courses> courseChildren = new ArrayList<Courses>();
                for (DataSnapshot course : courseSnapshot ){
                    //Log.i(TAG,  "View()"+course);
                    Courses temp = course.getValue(Courses.class);
                    courseChildren.add(temp);
                }
                appState.firebaseReference = appState.firebaseDBInstance.getReference("Registrations").child(userId).child("CourseID");
                appState.firebaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentIDList = dataSnapshot.getValue(String.class);
                        //course items that should be shown in the schedule


                        CourseList = courseChildren;

                        RegistrationListView = view.findViewById(R.id.listView_Registration);
                        RegButton = view.findViewById(R.id.RegisterButt);
                        final RegistrationAdapter adapter = new RegistrationAdapter(a, R.layout.fragment_register, CourseList, currentIDList);
                        RegistrationListView.setAdapter(adapter);

                        RegButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean checkLab_Tut = false;
                                boolean checkLec_count = false;
                                int LecCount = 0;
                                int num_Tut = 0;
                                int num_Lab = 0;
                                ArrayList<String> tut = new ArrayList();
                                ArrayList<String> lab = new ArrayList();

                                boolean checkConflict = false;//false -> no conflict
                                final ArrayList<String> courseFull = new ArrayList<>();
                                final ArrayList<Courses> curCourses = new ArrayList<>();
                                final ArrayList<Courses> oldCourses = new ArrayList<>();
                                ArrayList<Courses> checkList = new ArrayList<>();

                                for (Courses c : CourseList) {
                                    for (String s : adapter.getCourseList().split(",")) {
                                        if (s.equals(c.CourseID.toString())) {
                                            if (c.CourseType.toString().equals("Lec")) {
                                                LecCount++;
                                                if (!c.TutID.toString().equals("00000")) {

                                                    tut.add(c.TutID);

                                                }
                                                if (!c.LabID.toString().equals("00000")) {
                                                    lab.add(c.LabID);
                                                }
                                            }
                                            if (c.CourseType.equals("Tut")){
                                                if (tut.toString().contains(c.CourseID.toString()))
                                                    num_Tut++;
                                                else {
                                                    checkLab_Tut =true;
                                                }


                                            }
                                            if (c.CourseType.equals("Lab")){
                                                if (lab.toString().contains(c.CourseID.toString()))
                                                    num_Lab++;
                                                else {
                                                    checkLab_Tut =true;
                                                }
                                            }
                                            curCourses.add(c);
                                        }
                                    }

                                    for (String s : adapter.getOldList()) {
                                        if (s.equals(c.CourseID.toString()))
                                            oldCourses.add(c);

                                    }

                                }

                                checkList.addAll(curCourses);
                                checkList.removeAll(oldCourses);
     /*                   Log.i("getVIew() ",checkList.toString()+" asassa");
                        Log.i("getVIew() ",checkList.toString()+" asassa");*/
                                for (Courses c: checkList) {
                                    if (c.SpotCurrent == c.SpotMax) {
                                        courseFull.add(c.CourseTitle);
                                    }
                                }

                                if((tut.size() !=num_Tut)||(lab.size() !=num_Lab)){

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
                                if (checkConflict == false && checkLab_Tut == false && checkLec_count == false && courseFull.isEmpty()) {
                                    currentIDList = adapter.getCourseList();
                                    appState.firebaseReference = appState.firebaseDBInstance.getReference("Registrations").child(userId).child("CourseID");
                                    appState.firebaseReference.setValue(currentIDList);
                                    appState.firebaseReference = appState.firebaseDBInstance.getReference("Courses");
                                    appState.firebaseReference.runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {

                                            HashMap<String, Integer> map = new HashMap<>();
                                            for (Courses c: oldCourses) {
                                                Courses course = mutableData.child(c.CourseID.toString()).getValue(Courses.class);
                                                if (map.containsKey(course.CourseID.toString())) {
                                                    map.put(course.CourseID.toString(), map.get(course.CourseID.toString()));
                                                } else {
                                                    map.put(course.CourseID.toString(), course.SpotCurrent);
                                                }
                                                appState.firebaseReference.child(c.CourseID.toString()).child("SpotCurrent").setValue(map.get(course.CourseID.toString()));
                                            }

                                            for (Courses c : curCourses) {
                                                Courses course = mutableData.child(c.CourseID.toString()).getValue(Courses.class);

                                                if (map.containsKey(course.CourseID.toString())) {
                                                    map.put(course.CourseID.toString(), map.get(course.CourseID.toString()) + 1);
                                                } else {
                                                    map.put(course.CourseID.toString(), course.SpotCurrent + 1);
                                                }

                                                appState.firebaseReference.child(c.CourseID.toString()).child("SpotCurrent").setValue(map.get(course.CourseID.toString()));
                                            }


                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b,
                                                               DataSnapshot dataSnapshot) {
                                            // Transaction completed
                                            Log.d("SpotUpdateError", "" + databaseError);
                                        }
                                    });
                                    //Refresh fragment
                                    Fragment frg = getFragmentManager().findFragmentByTag("regFragment");
                                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.setReorderingAllowed(false);
                                    ft.detach(frg).attach(frg).commitAllowingStateLoss();
                                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                                }
                                else if (!courseFull.isEmpty()) {

                                    Toast.makeText(getActivity(), courseFull.toString().replace("[","").replace("]","") + " already full!", Toast.LENGTH_SHORT).show();


                                    //TODO disable register && conflict messages
                                    //TODO update currentSpot

                                }
                                else  if (checkConflict){

                                    Toast.makeText(getActivity(), "Conflict!", Toast.LENGTH_SHORT).show();


                                    //TODO disable register && conflict messages
                                    //TODO update currentSpot

                                }
                                else if (checkLab_Tut){

                                    Toast.makeText(getActivity(), "Lecture, tut, lab is not paired!", Toast.LENGTH_SHORT).show();

                                }
                                else {

                                    Toast.makeText(getActivity(), "Five more courses chosen", Toast.LENGTH_SHORT).show();

                                }
                            }

                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
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
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
        mContext = context;
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

}