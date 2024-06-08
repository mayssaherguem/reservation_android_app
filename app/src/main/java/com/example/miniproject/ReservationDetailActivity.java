// ReservationDetailActivity.java
package com.example.miniproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ReservationDetailActivity extends AppCompatActivity {

    private TextView nameTextView, dateTextView, timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);

        nameTextView = findViewById(R.id.nameTextView);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);

        Button backButton = findViewById(R.id.backButton); // Obtenir une référence au bouton

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retour à la MainActivity
                onBackPressed();
            }
        });

        // Récupérer les données passées à l'activité
        String name = getIntent().getStringExtra("name");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");

        // Afficher les données
        if (name != null) {
            nameTextView.setText(name);
        }
        if (date != null) {
            dateTextView.setText(date);
        }
        if (time != null) {
            timeTextView.setText(time);
        }
    }
}
