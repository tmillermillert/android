package edu.asu.bsse.mtmille5.appname;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;

import java.util.ArrayList;

public class FunctionActivity extends AppCompatActivity {

    private Spinner functionSpinner;
    private Spinner startLocationSpinner;
    private Spinner endLocationSpinner;
    private TextView resultText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function);

        resultText = (TextView) findViewById(R.id.result);

        functionSpinner = (Spinner) findViewById(R.id.functionSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> functionAdapter = ArrayAdapter.createFromResource(this,
                R.array.functions_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        functionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        functionSpinner.setAdapter(functionAdapter);

        startLocationSpinner = (Spinner) findViewById(R.id.startLocationSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> startLocationAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getPlaces());
        // Specify the layout to use when the list of choices appears
        startLocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        startLocationSpinner.setAdapter(startLocationAdapter);

        endLocationSpinner = (Spinner) findViewById(R.id.endLocationSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> endLocationAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getPlaces());
        // Specify the layout to use when the list of choices appears
        endLocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        endLocationSpinner.setAdapter(endLocationAdapter);
    }

    public PlaceDescription get(String placeName){
        PlaceDescription pd = new PlaceDescription();
        try{
            PlacesDB db = new PlacesDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select * from places where name=?;", new String[]{placeName});
            while(cur.moveToNext()){
                try{
                    pd.name = cur.getString(0);
                    pd.description = cur.getString(1);
                    pd.category = cur.getString(2);
                    pd.addressTitle = cur.getString(3);
                    pd.addressStreet = cur.getString(4);
                    pd.elevation = cur.getDouble(5);
                    pd.latitude = cur.getDouble(6);
                    pd.longitude = cur.getDouble(7);
                }catch(Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"exception stepping thru cursor"+ex.getMessage());
                }
            }
            cur.close();
            crsDB.close();
            db.close();
        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to setup student spinner");
        }
        finally {
            return pd;
        }
    }

    public ArrayList<String> getPlaces(){
        ArrayList<String> al = new ArrayList<String>();
        try{
            PlacesDB db = new PlacesDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select name from places;", new String[]{});
            while(cur.moveToNext()){
                try{
                    al.add(cur.getString(0));
                }catch(Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"exception stepping thru cursor"+ex.getMessage());
                }
            }
            cur.close();
            crsDB.close();
            db.close();
        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to setup student spinner");
        }
        finally {
            return al;
        }
    }

    public void clickedCancel(View view){
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

    public void clickedSubmit(View view){
        String start = (String) startLocationSpinner.getSelectedItem();
        String end = (String) endLocationSpinner.getSelectedItem();
        Double x = 0.0;
        PlaceDescription startPD = get(start);
        PlaceDescription endPD = get(end);

        if(functionSpinner.getSelectedItemPosition() == 0){
            x = startPD.greatCircleSphericalDistance(startPD, endPD);
        }
        else{
            x = startPD.initialHeading(startPD, endPD);
        }
        resultText.setText("Result: " + String.valueOf(x));
    }
}
