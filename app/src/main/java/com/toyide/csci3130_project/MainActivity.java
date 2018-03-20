package com.toyide.csci3130_project;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Luwei Cai & Zhiyuan Wang on 2018/2/21.
 * This activity is used to read password and username.
 * It is used to check whether users exist.
 */
public class MainActivity extends AppCompatActivity {


    private MyApplicationData appData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appData = (MyApplicationData)getApplication();
        //Set-up Firebase
        appData.firebaseDBInstance = FirebaseDatabase.getInstance();
        appData.firebaseReference = appData.firebaseDBInstance.getReference("Users");

    }

    public void onClick(View v) {
        EditText userField = findViewById(R.id.input_username);
        EditText psField = findViewById(R.id.input_password);
        final String userID = userField.getText().toString();
        final String password = psField.getText().toString();
        final TextView view = findViewById(R.id.login_error);
        if (userID.equals("")){
            String error = "No input username or password";
            view.setText(error);
        }
        appData.firebaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {        @Override
            public void onDataChange(DataSnapshot userSnapshot) {
                if (userSnapshot.child(userID).exists()) {
                            if (userSnapshot.child(userID).child("Password").getValue().toString().equals(password)){
                                LocalData.setUserID(userID);
                                Profile a = userSnapshot.child(userID).getValue(Profile.class);
                                showUser(a);
                            }else {

                                String text = "Incorrect username or password. Please try again.";
                                view.setText(text);
                            }
                } else {
                    String error2 = "User does not exist";
                    view.setText(error2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void showUser(Profile profile){
            Intent intent = new Intent(this, NavActivity.class);
            intent.putExtra("profile", profile);
            startActivity(intent);
    }
}


