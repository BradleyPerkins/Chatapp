package com.bradperkins.chatgroupapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bradperkins.chatgroupapp.GroupObj;
import com.bradperkins.chatgroupapp.R;
import com.bradperkins.chatgroupapp.fragments.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private DatabaseReference mRef;

    private ProgressBar progSpin;
    private TextView noNetTV;

    private ArrayList<GroupObj> groupList;
    private int listPos = 0;

    private GroupObj group;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    mRef.child("groups").child("Group 2_"+timeStamp).child("title").setValue("Group 2");
                    mRef.child("groups").child("Group 2_"+timeStamp).child("chatting").setValue("0");
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        groupList = new ArrayList<>();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();

        progSpin = findViewById(R.id.list_prog);
        progSpin.setVisibility(View.INVISIBLE);

        noNetTV = findViewById(R.id.no_network_tv);
        noNetTV.setVisibility(View.INVISIBLE);



        firebaseData();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.add_group:
//                //Get current time stamp
//                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//                mRef.child("groups").child(timeStamp).child("title").setValue("Group 1");
//                mRef.child("groups").child(timeStamp).child("chatting").setValue("0");
//
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //Pull Groups from Firebase
    private void firebaseData() {
        mRef = FirebaseDatabase.getInstance().getReference();
        group = new GroupObj();
        mRef.child("groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupList.clear();
                for (final DataSnapshot drinksSnapshot : dataSnapshot.getChildren()) {
                    group = drinksSnapshot.getValue(GroupObj.class);
                    groupList.add(group);
                }
                //Create Fragment when Firebase Data is loaded
                getFragmentManager().beginTransaction().replace(R.id.main_placeholder, HomeFragment.newInstance(groupList, listPos)).commit();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }




}
