package com.example.miniproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject.R;
import com.example.miniproject.Reservation;
import com.example.miniproject.ReservationAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReservationsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private List<Reservation> reservationList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations_list);

        recyclerView = findViewById(R.id.reservationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reservationList = new ArrayList<>();
        adapter = new ReservationAdapter(this, reservationList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("reservations");

        // Load reservations
        loadReservations();
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
                // Handle errors
            }
        });
    }
}
