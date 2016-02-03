package com.chamas.luis.getver2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class SetUpProfile extends AppCompatActivity {
    private Spinner sexSelect;
    private EditText name, age, bio;
    private String usernameFromParse;
    private TextView UserName;
    private ImageButton profilePic;
    ParseUser parseUser = ParseUser.getCurrentUser();
    Bitmap photo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile);

        sexSelect = (Spinner)findViewById(R.id.GenderSetUpSpinner);
        name = (EditText)findViewById(R.id.NameSetUpEditText);
        UserName = (TextView)findViewById(R.id.UsernameSetUpTextView);
        age = (EditText)findViewById(R.id.AgeSetUpEditText);
        bio = (EditText)findViewById(R.id.BioSetUpEditText);
        profilePic = (ImageButton)findViewById(R.id.SetUpProfileImageButton);
        registerForContextMenu(profilePic);

        String[] items = new String[]{"Male", "Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        sexSelect.setAdapter(adapter);

//        UserName.setText(parseUser.getUsername().toString());

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.contextual_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.title_take_photo:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 0);
                return true;

            case R.id.title_choose_existing:
                Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT);
                pickPhoto.setType("image/*");
                startActivityForResult(pickPhoto, 1);
                return true;

        }

        return super.onContextItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 0){
            photo = (Bitmap)data.getExtras().get("data");
            profilePic.setImageBitmap(photo);
        }

        if(requestCode == 1){
            Uri selectedImage = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                photo =  BitmapFactory.decodeStream(imageStream);
                profilePic.setImageBitmap(photo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeProfilePic(View view) {
        this.openContextMenu(view);
    }
    public void finishSetUp(View view) throws ParseException {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving profile");
        progressDialog.show();
        parseUser.put("Name", name.getText().toString());
        parseUser.put("Sex", sexSelect.getSelectedItem().toString());
        parseUser.put("Bio", bio.getText().toString());

        if(Objects.equals(age.getText().toString(), "")){
            parseUser.put("Age", 0);
        }else {
            parseUser.put("Age", Integer.parseInt(age.getText().toString()));
        }

        if(photo == null){
            photo = ((BitmapDrawable)profilePic.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            ParseFile file = new ParseFile("profile.png", byteArray);
            parseUser.put("Picture", file);
        }else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            ParseFile file = new ParseFile("profile.png", byteArray);
            parseUser.put("Picture", file);
        }
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(SetUpProfile.this, "setup success", Toast.LENGTH_LONG).show();
                    Intent region = new Intent(SetUpProfile.this, EventList.class);
                    SetUpProfile.this.startActivity(region);
                    SetUpProfile.this.finish();
                } else {
                    Toast.makeText(SetUpProfile.this, "setup failed", Toast.LENGTH_LONG).show();
                    Log.d("DEBUG", "PARSE EXCEPTION: " + e.toString());
                    progressDialog.dismiss();
                }
            }
        });
    }

}
