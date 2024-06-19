package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditReservationActivity extends AppCompatActivity {

    private EditText nameEditText, dateEditText, timeEditText;
    private Button editButton;
    private DatabaseReference databaseReference;

    private String reservationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);


        nameEditText = findViewById(R.id.nameEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        editButton = findViewById(R.id.editButton2);

        databaseReference = FirebaseDatabase.getInstance().getReference("reservations");


        Intent intent = getIntent();

        if (intent != null) {
            reservationId = intent.getStringExtra("reservationId");
            String name = intent.getStringExtra("Name");
            String date = intent.getStringExtra("Date");
            String time = intent.getStringExtra("Time");

            nameEditText.setText(name);
            dateEditText.setText(date);
            timeEditText.setText(time);
        }

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReservation();
            }
        });

    }


    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    month1 = month1 + 1; // Month is 0-based in Calendar
                    String date = dayOfMonth + "/" + month1 + "/" + year1;
                    dateEditText.setText(date);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute1) -> {
                    String time = hourOfDay + ":" + (minute1 < 10 ? "0" + minute1 : minute1);
                    timeEditText.setText(time);
                },
                hour, minute, true);
        timePickerDialog.show();
    }

    private void updateReservation() {
        String name = nameEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(time)) {
            Reservation updatedReservation = new Reservation(reservationId, name, date, time);
            databaseReference.child(reservationId).setValue(updatedReservation);

            Toast.makeText(this, "Réservation mise à jour", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        }
    }
}