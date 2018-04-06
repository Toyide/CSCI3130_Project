package com.toyide.csci3130_project;
import android.app.Activity;
import android.content.Context;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
//import android.widget.TableLayout;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;


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

    private MyApplicationData appState;
    private String[] cidList;
    private String courseTitle;
    private String courseInfo;
    private String courseSpot;
    private String location;
    private String courseWeekday;
    private String courseTime;
    private String courseType;
    private int spotMax;
    private int spotCurrent;

    private static final String TAG = "test";
    //Not needed in the registration

    private ListView RegistrationListView;
    private Button RegButton;
    private ArrayList<Courses> CourseList;

    private FirebaseListAdapter<Courses> firebaseAdapter;

    public String currentIDList;// selected courseIDList for later conflict check
    private CheckTimeConflict checkTimeConflict;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create a view instance to add the courseInfo
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //course items that should be shown in the schedule
        CourseList = getData.courses_list;
        checkTimeConflict = new CheckTimeConflict();
        RegistrationListView= (ListView) view.findViewById(R.id.listView_Registration);
        RegButton= (Button) view.findViewById(R.id.RegisterButt);

        final RegistrationAdapter adapter = new RegistrationAdapter(getContext(), R.layout.fragment_register, CourseList);
        RegistrationListView.setAdapter(adapter);

        //Retrieve schedual information for current user
        String userId = LocalData.getUserID(); //Get userID from local


        //Get the reference to the UI contents
        Activity act = getActivity();
        Log.i(TAG, "MyClass.getView()  " + getData.courses_list.toString()+" second");

        //Set-up Firebase
        appState = (MyApplicationData) getActivity().getApplicationContext();
        appState.firebaseDBInstance = FirebaseDatabase.getInstance();
        appState.firebaseReference = appState.firebaseDBInstance.getReference("Registrations");
        //Set-up Firebase

        RegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String IDlist = adapter.CourseIDString.toString().replace("[", "").replace("]", "").replace(" ","");
                //FIXME: remove update to REGISTER
                //appState.firebaseReference.child(LocalData.getUserID()).child("CourseID").setValue(IDlist);

                //confirm information on screen


                currentIDList = IDlist;

                boolean checkConflict = false;//false -> no conflict
                ArrayList<String> selectedCourseTimeList = new ArrayList<String>();
                ArrayList<String> selectedCourseDayList = new ArrayList<String>();
                Log.i(TAG,  "View()"+currentIDList.toString().split(",")[0]+"  alalal  "+currentIDList.toString().split(",").length);

                //Check conflict: CourseList a list of courses
                for (int i = 0; i < currentIDList.toString().split(",").length; i++) {
                    outerloop:
                    for (int j = 0; j < CourseList.size(); j++) {
                        Courses temp_course = CourseList.get(j);
                        //Courss matches get courseTime
                        Log.i(TAG,  "View()"+currentIDList.toString().split(",")[i]+"  alalal  "+temp_course.CourseID.toString());
                        if (temp_course.CourseID.toString().equals(currentIDList.toString().split(",")[i]) ) {

                            String currentCourseTime = temp_course.CourseTime;
                            String currentCourseDay = temp_course.CourseWeekday;
                            //Check if current courseTime and weekday confict with the existing one
                            int length =selectedCourseTimeList.size();
                            for (int k = 0; k < length; k++) {
                                //TODO:method check if two time conflict
                                //FIXME:problem with exception in method developed
                                //Conflictcheck(selectedCourseTimeList[k],currentCourseTime

                                Log.i(TAG,  "  alalal  "+checkTimeConflict.confliCtcheck(selectedCourseTimeList.get(k), currentCourseTime) + "   "+ checkTimeConflict.sameChars(selectedCourseDayList.get(k), currentCourseDay));
                                if (checkTimeConflict.confliCtcheck(selectedCourseTimeList.get(k), currentCourseTime) && checkTimeConflict.sameChars(selectedCourseDayList.get(k), currentCourseDay)) {
                                    Log.i(TAG,  "  alalal  "+k);
                                    checkConflict = true; //there is conflict
                                    break outerloop;
                                }
                                selectedCourseTimeList.add(currentCourseTime) ;
                                selectedCourseDayList.add(currentCourseDay);
                                //No conflict add to courseLis
                            }

                            if (i==0) {
                                selectedCourseTimeList.add(currentCourseTime) ;
                                selectedCourseDayList.add(currentCourseDay);
                            }
                        }
                    }
                }
                if (checkConflict == false) {
                    appState.firebaseReference.child(LocalData.getUserID()).child("CourseID").setValue(currentIDList);
                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Conflicted.", Toast.LENGTH_SHORT).show();
                    /*
                    final PopupWindow popUpWindow =new PopupWindow(getActivity());
                    LinearLayout mainLayout = new LinearLayout(getActivity());
                    LinearLayout containerLayout =new LinearLayout(getActivity());
                    Button btnClick =new Button(getActivity());
                    btnClick.setText("RETURN");
                    btnClick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popUpWindow.dismiss();
                        }
                    });
                    popUpWindow.showAtLocation(  getActivity() , Gravity.BOTTOM, 10, 10);
                    popUpWindow.update(50,50,320,90);
                    TextView tvMsg = new TextView(getActivity());
                    tvMsg.setText("Courses conflicted");

                    ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    containerLayout.setOrientation(LinearLayout.VERTICAL);
                    containerLayout.addView(tvMsg, layoutParams);
                    popUpWindow.setContentView(containerLayout);
                    containerLayout.addView(btnClick, layoutParams);
*/
                    //TODO disable register && conflict messages
                    //TODO update currentSpot

                }

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