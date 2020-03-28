package com.project.fit;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.Nullable;


public class MainActivity extends AppCompatActivity  implements SensorEventListener, AdapterView.OnItemClickListener {

    SensorManager sensorManager;
    FirebaseAuth fAuth;
    TextView userName, userSteps;
    boolean walking = false;
    long current_steps;
    float total_steps;
    FirebaseFirestore fStore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Date today = Calendar.getInstance().getTime();
        System.out.println(today);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = findViewById(R.id.user_name);
        View view = findViewById(R.id.view);
        view.setVisibility(View.INVISIBLE);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        userSteps = findViewById(R.id.user_steps);


        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                try { String name = documentSnapshot.getString("Name");
                    current_steps = (long) documentSnapshot.get("Total_steps");
                    total_steps = current_steps;
                    System.out.println(current_steps);
                    userName.setText("     "+name);
                   } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        ArrayList<home_item> home_items1 = new ArrayList<>();
        ArrayList<home_item> home_items2 = new ArrayList<>();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        home_items1.add(new home_item("Your Distance", R.drawable.distance));
        home_items1.add(new home_item("Recipes & Tips", R.drawable.recipes));
        home_items1.add(new home_item("Set Alarm", R.drawable.alarm));
        home_items1.add(new home_item("Settings", R.drawable.ic_settings_black_24dp));
        home_items1.add(new home_item("Log Out", R.drawable.logout));

        home_items2.add(new home_item("View stats", R.drawable.view_stats));
        home_items2.add(new home_item("Leaderboard", R.drawable.leaderboard));
        home_items2.add(new home_item("Weather", R.drawable.weather));
        home_items2.add(new home_item("Account", R.drawable.account));
        home_items2.add(new home_item("Sleep time", R.drawable.sleep));

        ListView homelistview2 = findViewById(R.id.home_list_view2);
        ListView homelistvieww = findViewById(R.id.home_list_view1);
        homelist_adapter homelistAdapter = new homelist_adapter(this, home_items1);
        homelist_adapter homelist = new homelist_adapter(this, home_items2);
        homelistview2.setAdapter(homelist);
        homelistvieww.setAdapter(homelistAdapter);
        homelistvieww.setOnItemClickListener(this);
        homelistview2.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        switch (adapterView.getId()) {
            case R.id.home_list_view1:

                if (position == 0) {
                    Intent intent = new Intent(this, RecipesActivity.class);
                    startActivity(intent);
                }
                if (position == 1) {
                    Intent intent1 = new Intent(this, RecipesActivity.class);
                    startActivity(intent1);
                }
                if (position == 2) {
                    Intent intent = new Intent(this, RecipesActivity.class);
                    startActivity(intent);
                }
                if (position == 3) {
                    Intent intent2 = new Intent(this, RecipesActivity.class);
                    startActivity(intent2);
                }
                if (position == 4) {
                    fAuth.signOut();
                    Intent intent = new Intent(this, startActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "Successfully Signed Out", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.home_list_view2:

                if (position == 2) {
                    Intent intent = new Intent(this, weatherActivity.class);
                    startActivity(intent);
                }

                break;

        }
    }

    public void asasasa(View view) {
        Intent intent1 = new Intent(this, RecipesActivity.class);
        startActivity(intent1);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // Other Sensor code goes here
    }

    @Override
    protected void onPause() {
        super.onPause();
        walking = false;
        //Unregister stops registering steps
        //sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // SENSOR CODE GOES HERE
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
