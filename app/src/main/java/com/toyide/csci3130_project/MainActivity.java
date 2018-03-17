package com.toyide.csci3130_project;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Luwei Cai & Zhiyuan Wang on 2018/2/21.
 * This activity is used to read password and username.
 * It is used to check whether users exist.
 */
public class MainActivity extends AppCompatActivity {
    private Button submitButton;

    private static  final String TAG = "MainActivity";
    private MyApplicationData appState;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private MyApplicationData appData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appData = (MyApplicationData)getApplication();
        //Set-up Firebase
        appData.firebaseDBInstance = FirebaseDatabase.getInstance();
        appData.firebaseReference = appData.firebaseDBInstance.getReference("login");
        appState = ((MyApplicationData) getApplicationContext());

}
public void onClick(View v) {
    EditText userField = findViewById(R.id.input_username);
    EditText psField = findViewById(R.id.input_password);
    final String userID = userField.getText().toString();
    final String password = psField.getText().toString();
    final TextView view = findViewById(R.id.login_error);
    appData.firebaseReference.orderByChild(userID).limitToFirst(1).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot userSnapshot) {
            if (userSnapshot != null) {
                Query query = appData.firebaseReference.child(userID).child("password");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String pass = dataSnapshot.getValue(String.class);
                        // Data is ordered by increasing height, so we want the first entry
                        if (pass.equals(password)){
                            login log =new login(userID,password);
                            LocalData.setUserID(userID);
                            showUser(log);
                        }else {
                            String text = "Incorrect username or password. Please try again.";
                            view.setText(text);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // ...
                    }
                });
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}
private void showUser(login login){
        Intent intent = new Intent(this, NavActivity.class);
        intent.putExtra("login", login);
        startActivity(intent);
}
}


