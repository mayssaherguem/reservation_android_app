package com.example.miniproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private Context context;
    private List<Reservation> reservationList;

    public ReservationAdapter(Context context, List<Reservation> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reservation_item, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.reservationTextView.setText("Name: " + reservation.getName() + "\nDate: " + reservation.getDate() + "\nTime: " + reservation.getTime());


        holder.editButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(context, EditReservationActivity.class);
            editIntent.putExtra("reservationId", reservation.getId());
            editIntent.putExtra("Name", reservation.getName());
            editIntent.putExtra("Date", reservation.getDate());
            editIntent.putExtra("Time", reservation.getTime());
            context.startActivity(editIntent);

        });


        holder.deleteButton.setOnClickListener(v -> {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("reservations").child(reservation.getId());
            databaseReference.removeValue();
            reservationList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Reservation deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView reservationTextView;
        Button editButton, deleteButton;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            reservationTextView = itemView.findViewById(R.id.reservationTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
