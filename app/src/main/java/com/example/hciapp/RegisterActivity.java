package com.example.hciapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    ImageView backBtn;
    Spinner typeSpinner;

    EditText fullNameInput;
    EditText usernameInput;
    EditText emailInput;
    EditText passwordInput;

    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullNameInput = findViewById(R.id.fullNameInput);
        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        backBtn = findViewById(R.id.back);

        registerBtn = findViewById(R.id.registerBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //Intent backIntent = new Intent(getApplicationContext(), LoginActivity.class);
                //startActivity(backIntent);
            }
        });

        typeSpinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types_array, R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_item);

        typeSpinner.setAdapter(adapter);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (fullNameInput.getText().toString().isEmpty() ||
                        usernameInput.getText().toString().isEmpty()||
                        emailInput.getText().toString().isEmpty() ||
                        passwordInput.getText().toString().isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this, R.style.AlertDialog);

                    builder.setTitle("All fields required!")
                            .setMessage("Please fill in all fileds in order to register!")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                }
                List<User> users = MyDatabase.getDatabase(getApplicationContext()).userDAO().getAllUsers();
                boolean exists = false;

                exists = checkDB(users);

                if (!exists) {
                    if (typeSpinner.getSelectedItem().toString().equalsIgnoreCase("user") ||
                            typeSpinner.getSelectedItem().toString().equalsIgnoreCase("venue owner")) {
                        User user = new User(
                                fullNameInput.getText().toString(),
                                usernameInput.getText().toString(),
                                emailInput.getText().toString(),
                                passwordInput.getText().toString(),
                                typeSpinner.getSelectedItem().toString()
                        );

                        MyDatabase.getDatabase(getApplicationContext()).userDAO().addUser(user);
                    } else {
                        //Drawable d = getResources().getDrawable(R.drawable.ic_account_box_black_24dp); // the drawable (Captain Obvious, to the rescue!!!)
                        //Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                        VectorDrawable d = (VectorDrawable) getResources().getDrawable(R.drawable.ic_account_box_black_24dp);
                        Bitmap bitmap = getBitmapFromVector(d);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] bitmapdata = stream.toByteArray();


                        Artist artist = new Artist(
                                usernameInput.getText().toString(),
                                fullNameInput.getText().toString(),
                                emailInput.getText().toString(),
                                passwordInput.getText().toString(),
                                "Description",
                                typeSpinner.getSelectedItem().toString(),
                                bitmapdata
                        );

                        MyDatabase.getDatabase(getApplicationContext()).artistDAO().addArtist(artist);
                    }

                    Toast.makeText(getApplicationContext(), "Registered succesfully", Toast.LENGTH_LONG).show();

                    finish();
                    Intent backIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(backIntent);
                }
            }
        });
    }

    public static Bitmap getBitmapFromVector(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private boolean checkDB(List<User> users) {
        boolean exists = false;

        for (User user: users) {
            if (user.getUsername().equals(usernameInput.getText().toString())) {
                exists = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this, R.style.AlertDialog);

                builder.setTitle("Username exists!")
                        .setMessage("The username you have entered is associated with another account!")
                        .setNegativeButton("Retry", null)
                        .setPositiveButton("Go to login", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(loginIntent);
                            }
                        })
                        .create()
                        .show();
            }
        }

        return exists;
    }
}
