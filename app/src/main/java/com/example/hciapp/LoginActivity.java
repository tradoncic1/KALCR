package com.example.hciapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText usernameInput;
    EditText passwordInput;
    TextView registerHere;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);

        registerHere = findViewById(R.id.registerHere);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialog);

                if (usernameInput.getText().toString().isEmpty()) {
                    builder.setTitle("Username required")
                            .setMessage("You must enter a username in order to log in.")
                            .setNegativeButton("Retry.", null)
                            .create()
                            .show();
                } else if (passwordInput.getText().toString().isEmpty()) {
                    builder.setTitle("Password required")
                            .setMessage("You must enter a password to log in.")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                } else {
                    boolean logged = false;

                    List<Artist> artists = MyDatabase.getDatabase(getApplicationContext()).artistDAO().getAllArtists();

                    List<User> users = MyDatabase.getDatabase(getApplicationContext()).userDAO().getAllUsers();

                    for (User user: users) {
                        if (user.getUsername().equals(usernameInput.getText().toString())) {
                            if (user.getPassword().equals(passwordInput.getText().toString())) {
                                logged = true;
                                logIn(user);
                            } else {
                                builder.setTitle("Invalid password!")
                                        .setMessage("The password you have entered is not associated with any username.")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        }
                    }

                    for (Artist artist: artists) {
                        if (artist.getUsername().equals(usernameInput.getText().toString())) {
                            if (artist.getPassword().equals(passwordInput.getText().toString())) {
                                logged = true;
                                logIn(artist);
                            } else {
                                builder.setTitle("Invalid password!")
                                        .setMessage("The password you have entered is not associated with any username.")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        }
                    }


                    if (!logged) {
                        builder.setTitle("Invalid username!")
                                .setMessage("The username you have entered is not associated with any account!")
                                .setNegativeButton("Retry", null)
                                .setPositiveButton("Create account", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                                        startActivity(registerIntent);
                                    }
                                })
                                .create()
                                .show();
                    }
                }
            }
        });

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }

    private void logIn(User user) {
        if (user.getType().equalsIgnoreCase("venue owner")) {
            Intent loggedIn = new Intent(getApplicationContext(), VenueOwnerMainActivity.class);
            loggedIn.putExtra("userId", user.getUserId());

            startActivity(loggedIn);
        } else if (user.getType().equalsIgnoreCase("user")) {
            Intent loggedIn = new Intent(getApplicationContext(), UserMainActivity.class);
            loggedIn.putExtra("userId", user.getUserId());

            startActivity(loggedIn);
        }
    }


    private void logIn(Artist artist) {
        Intent loggedIn = new Intent(getApplicationContext(), ArtistMainActivity.class);
        loggedIn.putExtra("artistId", artist.getArtistId());

        startActivity(loggedIn);
    }



    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
