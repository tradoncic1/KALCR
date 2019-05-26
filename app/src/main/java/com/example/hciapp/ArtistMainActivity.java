package com.example.hciapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArtistMainActivity extends AppCompatActivity {

    TextView artistName;
    TextView artistDesc;
    TextView artistType;

    TextView nameDisplay;

    EditText artistNameInput;
    EditText artistDescInput;
    EditText artistTypeInput;

    ImageView artistImage;

    ConstraintLayout artistTextView;
    ConstraintLayout artistEditText;

    Button editImgBtn;
    Button editProfileBtn;
    Button applyBtn;
    Button logOutBtn;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_main);

        Bundle extras = getIntent().getExtras();
        final Artist artist = MyDatabase.getDatabase(getApplicationContext()).artistDAO().getArtist(extras.getInt("artistId"));

        artistEditText = findViewById(R.id.artistEditText);
        artistTextView = findViewById(R.id.artistTextView);
        artistEditText.setVisibility(View.GONE);

        artistImage = findViewById(R.id.artistImage);

        editImgBtn = findViewById(R.id.changeImgBtn);
        editImgBtn.setVisibility(View.GONE);

        applyBtn = findViewById(R.id.applyBtn);
        applyBtn.setVisibility(View.GONE);

        artistName = findViewById(R.id.eventName);
        artistDesc = findViewById(R.id.artistDesc);
        artistType = findViewById(R.id.artistType);

        nameDisplay = findViewById(R.id.usernameDisplay);
        nameDisplay.setText(artist.getFullName());

        logOutBtn = findViewById(R.id.logOutBtn);

        artistNameInput = findViewById(R.id.artistNameInput);
        artistDescInput = findViewById(R.id.artistDescInput);
        artistTypeInput = findViewById(R.id.artistTypeInput);

        artistName.setText(artist.getFullName());
        artistDesc.setText(artist.getDesc());
        artistType.setText(artist.getType());

        bitmap = BitmapFactory.decodeByteArray(artist.getArtistImage(), 0, artist.getArtistImage().length);
        artistImage.setImageBitmap(bitmap);

        editProfileBtn = findViewById(R.id.editBtn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artistEditText.setVisibility(View.VISIBLE);
                artistTextView.setVisibility(View.GONE);
                editImgBtn.setVisibility(View.VISIBLE);
                applyBtn.setVisibility(View.VISIBLE);
                editProfileBtn.setVisibility(View.GONE);

                artistNameInput.setText(artist.getFullName());
                artistDescInput.setText(artist.getDesc());
                artistTypeInput.setText(artist.getType());
            }
        });

        editImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select a picture"), 101);

                artistImage.setImageBitmap(bitmap);
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artist.setFullName(artistNameInput.getText().toString());
                artist.setDesc(artistDescInput.getText().toString());
                artist.setType(artistTypeInput.getText().toString());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                artist.setArtistImage(byteArray);

                MyDatabase.getDatabase(getApplicationContext()).artistDAO().updateArtist(artist);

                artistEditText.setVisibility(View.GONE);
                artistTextView.setVisibility(View.VISIBLE);
                editImgBtn.setVisibility(View.GONE);
                applyBtn.setVisibility(View.GONE);
                editProfileBtn.setVisibility(View.VISIBLE);

                artistName.setText(artist.getFullName());
                artistDesc.setText(artist.getDesc());
                artistType.setText(artist.getType());
                artistImage.setImageBitmap(bitmap);

                Toast.makeText(getApplicationContext(), "Changes applied successfully!", Toast.LENGTH_LONG).show();

            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ArtistMainActivity.this, R.style.AlertDialog);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri = null;
        if (requestCode == 101) {
            if(resultCode == RESULT_OK)
                imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                artistImage.setImageBitmap(bitmap);
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
