package com.example.hciapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VenueOwnerMainActivity extends AppCompatActivity {
    ImageView eventImage;

    EditText eventName;
    EditText eventDate;
    EditText eventLocation;
    EditText eventPrice;
    EditText eventDesc;
    EditText eventType;

    TextView usernameDisplay;

    Button addBtn;
    Button addImgBtn;
    Button logOutBtn;

    ListView listView;

    Bitmap bitmap;

    List<Event> eventList;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_owner_main);

        //final Intent receivedIntent = getIntent();
        Bundle extras = getIntent().getExtras();

        eventImage = findViewById(R.id.eventImage);

        eventName = findViewById(R.id.eventNameInput);
        eventDate = findViewById(R.id.eventDateInput);
        eventLocation = findViewById(R.id.eventLocationInput);
        eventPrice = findViewById(R.id.eventPriceInput);
        eventDesc = findViewById(R.id.eventDescInput);
        eventType = findViewById(R.id.eventTypeInput);

        addBtn = findViewById(R.id.addEventBtn);
        addImgBtn = findViewById(R.id.addImgBtn);
        logOutBtn = findViewById(R.id.logOutBtn);

        listView = findViewById(R.id.eventList);

        usernameDisplay = findViewById(R.id.usernameDisplay);
        //usernameDisplay.setText(receivedIntent.getStringExtra("username"));
        user = MyDatabase.getDatabase(getApplicationContext()).userDAO().getUser(extras.getInt("userId"));
        usernameDisplay.setText(user.getUsername());


        eventList = MyDatabase.getDatabase(getApplicationContext()).eventsDAO().getAllEvents();

        //String username = receivedIntent.getStringExtra("username");

        for (Event event: eventList) {
            if (!event.getVenue().equals(user.getFullName())) {
                eventList.remove(event);
            }
        }

        final EventListAdapterOwner adapter = new EventListAdapterOwner(getApplicationContext(), eventList);

        listView.setAdapter(adapter);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VenueOwnerMainActivity.this, R.style.AlertDialog);

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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (eventName.getText().toString().isEmpty() ||
                    eventType.getText().toString().isEmpty() ||
                    eventDate.getText().toString().isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VenueOwnerMainActivity.this, R.style.AlertDialog);

                    builder.setTitle("All  fields are required!")
                            .setMessage("Please fill in all fields in order to add the event")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                } else if (eventDesc.getText().toString().isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VenueOwnerMainActivity.this, R.style.AlertDialog);

                    builder.setTitle("Empty event description field!")
                            .setMessage("You are about to add an event without a description. You can do so but it is recommended to add a description.")
                            .setNegativeButton("Enter description", null)
                            .setPositiveButton("Create anyway", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    addEvent(adapter);
                                }
                            })
                            .create()
                            .show();
                } else {
                    addEvent(adapter);
                }

            }
        });

        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select a picture"), 101);

                eventImage.setImageBitmap(bitmap);
            }
        });
    }

    private void addEvent(EventListAdapterOwner adapter) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        //bitmap.recycle();

        Event event = new Event(eventName.getText().toString(),
                eventDesc.getText().toString(),
                eventLocation.getText().toString(),
                eventDate.getText().toString(),
                eventPrice.getText().toString(),
                eventType.getText().toString(),
                //receivedIntent.getStringExtra("username"),
                user.getFullName(),
                byteArray);

        MyDatabase.getDatabase(getApplicationContext()).eventsDAO().addEvent(event);
        eventList.add(event);

        eventName.setText(null);
        eventDate.setText(null);
        eventLocation.setText(null);
        eventPrice.setText(null);
        eventDesc.setText(null);
        eventType.setText(null);
        eventImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_image_black_24dp));


        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri = null;
        if (requestCode == 101) {
            if(resultCode == RESULT_OK)
                imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                eventImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
