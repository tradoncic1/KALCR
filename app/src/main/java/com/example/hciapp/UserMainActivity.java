package com.example.hciapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class UserMainActivity extends AppCompatActivity {

    TextView usernameDisplay;
    ListView listView;

    EditText locationFilter;
    EditText typeFilter;

    ImageView searchBtn;

    List<Event> eventList;

    Button logOutBtn;

    User user;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        Bundle extras = getIntent().getExtras();
        user = MyDatabase.getDatabase(getApplicationContext()).userDAO().getUser(extras.getInt("userId"));

        usernameDisplay = findViewById(R.id.usernameDisplay);
        //usernameDisplay.setText(receivedIntent.getStringExtra("username"));
        usernameDisplay.setText(user.getFullName());

        locationFilter = findViewById(R.id.eventLocationInput);
        typeFilter = findViewById(R.id.eventTypeInput);

        searchBtn = findViewById(R.id.artistImage);

        listView = findViewById(R.id.eventList);

        logOutBtn = findViewById(R.id.logOutBtn);

        eventList = MyDatabase.getDatabase(getApplicationContext()).eventsDAO().getAllEvents();
        final EventListAdapter adapter = new EventListAdapter(this, eventList);

        listView.setAdapter(adapter);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserMainActivity.this, R.style.AlertDialog);

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

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence filterPattern;
                if (locationFilter.getText().toString().isEmpty()) {
                    filterPattern = "empty/" + typeFilter.getText().toString();
                    adapter.getFilter().filter(filterPattern);
                } else if (typeFilter.getText().toString().isEmpty()) {
                    filterPattern = locationFilter.getText().toString() + "/empty";
                    adapter.getFilter().filter(filterPattern);
                } else if (!typeFilter.getText().toString().isEmpty() && !locationFilter.getText().toString().isEmpty()) {
                    filterPattern = locationFilter.getText().toString() + "/" + typeFilter.getText().toString();
                    adapter.getFilter().filter(filterPattern);
                } else {
                    eventList.clear();
                    eventList.addAll(MyDatabase.getDatabase(getApplicationContext()).eventsDAO().getAllEvents());
                    //filterPattern = "empty";
                }

                //adapter.getFilter().filter(filterPattern);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent eventIntent = new Intent(getApplicationContext(), EventItemUser.class);
                Event event = (Event) parent.getItemAtPosition(position);

                eventIntent.putExtra("eventId", event.getEventId());
                eventIntent.putExtra("userId", user.getUserId());

                startActivity(eventIntent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
