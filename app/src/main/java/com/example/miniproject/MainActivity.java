package com.example.miniproject;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject.AddReservationActivity;
import com.example.miniproject.Reservation;
import com.example.miniproject.ReservationAdapter;
import com.example.miniproject.ReservationsListActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button addReservationButton;
    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private List<Reservation> reservationList;
    private DatabaseReference databaseReference;
    private TextView currentDateTimeTextView, textView1;
    private Button viewReservationsButton; // Added button for viewing reservations
    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addReservationButton = findViewById(R.id.addReservationButton);
        currentDateTimeTextView = findViewById(R.id.currentDateTimeTextView);
        viewReservationsButton = findViewById(R.id.viewReservationsButton);
        textView1 = findViewById(R.id.textView1);

        Animation flashAnimation = AnimationUtils.loadAnimation(this, R.anim.flash);

        // Apply the animation to the TextView
        textView1.startAnimation(flashAnimation);

        databaseReference = FirebaseDatabase.getInstance().getReference("reservations");

        addReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddReservationActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for "View Reservations" button
        viewReservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReservationsListActivity.class);
                startActivity(intent);
            }
        });

        // Initialize handler for updating date and time
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                setCurrentDateTime();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }



    private void loadReservations() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reservationList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Reservation reservation = postSnapshot.getValue(Reservation.class);
                    reservationList.add(reservation);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs ici
            }
        });
    }

    private void setCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String currentDateTime = dateFormat.format(new Date());
        currentDateTimeTextView.setText(currentDateTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
