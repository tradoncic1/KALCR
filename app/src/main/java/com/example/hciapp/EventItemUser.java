package com.example.hciapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EventItemUser extends AppCompatActivity {
    TextView eventName;
    TextView eventDesc;
    TextView eventDateTime;
    TextView eventLocation;
    TextView eventType;
    TextView eventPrice;
    TextView eventVenue;
    TextView eventArtist;

    Button getTicketBtn;
    Button cancelBtn;

    ImageView eventImage;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_item_user);

        Bundle extras = getIntent().getExtras();

        eventName = findViewById(R.id.eventName);
        eventDesc = findViewById(R.id.eventDesc);
        eventDateTime = findViewById(R.id.eventDateTime);
        eventLocation = findViewById(R.id.eventLocationInput);
        eventType = findViewById(R.id.eventType);
        eventPrice = findViewById(R.id.eventPrice);
        eventVenue = findViewById(R.id.eventVenue);
        eventArtist = findViewById(R.id.artistName);

        getTicketBtn = findViewById(R.id.getTicketBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        eventImage = findViewById(R.id.eventImage);

        Event event = MyDatabase.getDatabase(getApplicationContext()).eventsDAO().getEvent(extras.getInt("eventId"));
        eventName.setText(event.getTitle());
        eventDesc.setText(event.getDescription());
        eventType.setText(event.getType());
        eventDateTime.setText(event.getDateTime());
        eventLocation.setText(event.getLocation());
        eventPrice.setText(event.getPrice());
        eventVenue.setText(event.getVenue());
        final Artist artist = MyDatabase.getDatabase(getApplicationContext()).artistDAO().getArtist(event.getArtist());
        eventArtist.setText(artist.getFullName());
        eventArtist.setPaintFlags(eventArtist.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        user = MyDatabase.getDatabase(getApplicationContext()).userDAO().getUser(extras.getInt("userId"));

        Bitmap bitmap = BitmapFactory.decodeByteArray(
                event.getEventImage(),
                0,
                event.getEventImage().length);

        eventImage.setImageBitmap(bitmap);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(), UserMainActivity.class);
                backIntent.putExtra("userId", user.getUserId());

                finish();
                startActivity(backIntent);
            }
        });

        getTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Ticket Reserved", Toast.LENGTH_LONG).show();

                Intent backIntent = new Intent(getApplicationContext(), UserMainActivity.class);
                backIntent.putExtra("userId", user.getUserId());

                finish();
                startActivity(backIntent);
            }
        });

        eventArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToArtist = new Intent(getApplicationContext(), UserArtistActivity.class);
                goToArtist.putExtra("artistId", artist.getArtistId());

                startActivity(goToArtist);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(getApplicationContext(), UserMainActivity.class);
        startMain.putExtra("userId", user.getUserId());

        finish();
        startActivity(startMain);
    }
}
