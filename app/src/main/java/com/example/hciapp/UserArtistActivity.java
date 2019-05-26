package com.example.hciapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserArtistActivity extends AppCompatActivity {

    TextView artistName;
    TextView artistDesc;
    TextView artistType;
    TextView usernameDisplay;

    ImageView artistImage;
    Button logOutBtn;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_artist);

        Bundle extras = getIntent().getExtras();

        Event event = MyDatabase.getDatabase(getApplicationContext()).eventsDAO().getEvent(extras.getInt("artistId"));
        Artist artist = MyDatabase.getDatabase(getApplicationContext()).artistDAO().getArtist(event.getArtist());

        artistName = findViewById(R.id.eventName);
        artistDesc = findViewById(R.id.artistDesc);
        artistType = findViewById(R.id.artistType);
        artistImage = findViewById(R.id.artistImage);

        artistName.setText(artist.getFullName());
        artistDesc.setText(artist.getDesc());
        artistType.setText(artist.getType());

        bitmap = BitmapFactory.decodeByteArray(artist.getArtistImage(), 0, artist.getArtistImage().length);
        artistImage.setImageBitmap(bitmap);

        usernameDisplay = findViewById(R.id.usernameDisplay);
        usernameDisplay.setText(artist.getFullName());

        logOutBtn = findViewById(R.id.logOutBtn);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserArtistActivity.this, R.style.AlertDialog);

                builder.setMessage("Are you sure you want to log out?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent logOutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                finish();
                                startActivity(logOutIntent);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }
}
