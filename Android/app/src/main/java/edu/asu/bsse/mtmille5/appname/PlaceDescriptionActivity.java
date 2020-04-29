package edu.asu.bsse.mtmille5.appname;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class PlaceDescriptionActivity extends
        AppCompatActivity {

    public static final int PLACE_DESCRIPTION = R.string.place_description;
    private PlaceDescription placeDescription;
    private int index;
    private boolean isUpdate = false;
    private String origName;
    private EditText name;
    private EditText description;
    private EditText category;
    private EditText addressTitle;
    private EditText addressStreet;
    private EditText elevation;
    private EditText longitude;
    private EditText latitude;

    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_description);

        name =(EditText) findViewById(R.id.nameValue);
        description = (EditText) findViewById(R.id.descriptionValue);
        category = (EditText) findViewById(R.id.categoryValue);
        addressTitle = (EditText) findViewById(R.id.addressTitleValue);
        addressStreet = (EditText) findViewById(R.id.addressStreetValue);

        elevation = (EditText) findViewById(R.id.elevationValue);
        longitude = (EditText) findViewById(R.id.longitudeValue);
        latitude = (EditText) findViewById(R.id.latitudeValue);

        placeDescription = (PlaceDescription) getIntent().getSerializableExtra(getString(PLACE_DESCRIPTION));
        index = getIntent().getIntExtra(getString(R.string.index), index);
        requestCode = getIntent().getIntExtra(getString(R.string.requestCode), requestCode);
        if (placeDescription != null) {
            isUpdate = true;
            origName = placeDescription.name;
            name.setText(placeDescription.name);
            description.setText(placeDescription.description);
            category.setText(placeDescription.category);
            addressTitle.setText(placeDescription.addressTitle);
            addressStreet.setText(placeDescription.addressStreet);

            elevation.setText(String.valueOf(placeDescription.elevation));
            longitude.setText(String.valueOf(placeDescription.longitude));
            latitude.setText(String.valueOf(placeDescription.latitude));
        }
        else{
            placeDescription = new PlaceDescription();
        }
    }

    public void addPlace(PlaceDescription place){
        android.util.Log.d(this.getClass().getSimpleName(), "Adding: " + this.name.getText().toString());
        try{
            PlacesDB db = new PlacesDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            ContentValues hm = new ContentValues();
            hm.put("name", place.name);
            hm.put("description", place.description);
            hm.put("category", place.category);
            hm.put("address_title", place.addressTitle);
            hm.put("address_street", place.addressStreet);
            hm.put("elevation", place.elevation);
            hm.put("latitude", place.latitude);
            hm.put("longitude", place.longitude);
            crsDB.insert("places",null, hm);
            crsDB.close();
            db.close();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception adding place information: "+
                    ex.getMessage());
        }
    }

    public void updatePlace(PlaceDescription place, String originalName){
        android.util.Log.d(this.getClass().getSimpleName(), "Updating: " + this.name.getText().toString());
        try{
            PlacesDB db = new PlacesDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            ContentValues hm = new ContentValues();
            hm.put("name", place.name);
            hm.put("description", place.description);
            hm.put("category", place.category);
            hm.put("address_title", place.addressTitle);
            hm.put("address_street", place.addressStreet);
            hm.put("elevation", place.elevation);
            hm.put("latitude", place.latitude);
            hm.put("longitude", place.longitude);
            String where = "places.name = ?";
            crsDB.update("places",hm, where, new String[]{originalName});
            crsDB.close();
            db.close();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception adding place information: "+
                    ex.getMessage());
        }
    }

    public void clickedSave(View view){
        Intent resultIntent = new Intent();
        if(name.getText().toString().length() < 1){
            Context context = getApplicationContext();
            CharSequence text = "Name Required!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        placeDescription.name = name.getText().toString();
        if(description.getText().toString().length() >= 1) {
            placeDescription.description = description.getText().toString();
        }
        else{
            placeDescription.description = "Unknown";
        }
        if (category.getText().toString().length() >= 1) {
            placeDescription.category = category.getText().toString();
        }
        else{
            placeDescription.category = "Unknown";
        }
        if(addressTitle.getText().length() >= 1) {
            placeDescription.addressTitle = addressTitle.getText().toString();
        }
        else{
            placeDescription.addressTitle = "Unknown";
        }
        if(addressStreet.getText().length() < 1){
            addressStreet.setText("Unknown");
        }

        placeDescription.addressStreet = addressStreet.getText().toString();

        if (elevation.getText().toString().length() >= 1){
            placeDescription.elevation = Double.valueOf(elevation.getText().toString());
        }
        else{
            placeDescription.elevation = 0.0;
        }
        if (longitude.getText().toString().length() >= 1) {
            placeDescription.longitude = Double.valueOf(longitude.getText().toString());
        }
        else{
            placeDescription.longitude = 0.0;
        }
        if(latitude.getText().toString().length() >= 1) {
            placeDescription.latitude = Double.valueOf(latitude.getText().toString());
        }
        else{
            placeDescription.latitude = 0.0;
        }
        if (isUpdate){
            updatePlace(placeDescription, origName);
        }
        else{
            addPlace(placeDescription);
        }
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
    public void clickedCancel(View view){
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }
}
