package com.example.miniproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddReservationActivity extends AppCompatActivity {

    private EditText nameEditText, dateEditText, timeEditText;
    private Button saveButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reservation);

        nameEditText = findViewById(R.id.nameEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        saveButton = findViewById(R.id.saveButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("reservations");

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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReservation();
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

    private void addReservation() {
        String name = nameEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(time)) {
            String id = databaseReference.push().getKey();
            Reservation reservation = new Reservation(id, name, date, time);
            databaseReference.child(id).setValue(reservation);

            Toast.makeText(this, "Réservation ajoutée", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        }
    }
}
