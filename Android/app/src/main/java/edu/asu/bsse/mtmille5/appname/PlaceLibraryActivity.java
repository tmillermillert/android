package edu.asu.bsse.mtmille5.appname;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.Gson;

public class PlaceLibraryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, DialogInterface.OnClickListener {
    private int delete_position;
    ListView listView;
    private ArrayAdapter<String> listViewAdapter;
    boolean isDialogOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_library);

        try {
            isDialogOpen = savedInstanceState.getBoolean(getString(R.string.is_dialog_open));
            delete_position = savedInstanceState.getInt(getString(R.string.delete_position));
            if(isDialogOpen){
                showDeleteDialog();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            isDialogOpen = false;
        }


        listView = findViewById(R.id.list);
        listViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getPlaces());
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
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

    public void removePlace(String placeName){
        android.util.Log.d(this.getClass().getSimpleName(), "remove Clicked");
        //String delete = "delete from student where student.name='"+this.selectedStudent+"';";
        String delete = "delete from places where places.name=?;";
        try {
            PlacesDB db = new PlacesDB((Context) this);
            SQLiteDatabase crsDB = db.openDB();
            crsDB.execSQL(delete, new String[]{placeName});
            crsDB.close();
            db.close();
        }catch(Exception e){
            android.util.Log.w(this.getClass().getSimpleName()," error trying to delete student");
        }
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

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // Save custom values into the bundle
        savedInstanceState.putBoolean(getString(R.string.is_dialog_open), isDialogOpen);
        savedInstanceState.putInt(getString(R.string.delete_position), delete_position);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        listViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getPlaces());
        listView.setAdapter(listViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
                add();
                return (true);
            case R.id.function:
                function();
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, PlaceDescriptionActivity.class);
        ListView lv = (ListView) parent;
        i.putExtra(getString(R.string.place_description), get((String) lv.getAdapter().getItem(position)));
        i.putExtra(getString(R.string.index), position);
        startActivityForResult(i, 2);
    }

    void function(){
        Intent i = new Intent(this, FunctionActivity.class);
        startActivity(i);
    }

    void add(){
        Intent i = new Intent(this, PlaceDescriptionActivity.class);
        startActivityForResult(i, 3);
    }


    public void showDeleteDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete)
                .setNegativeButton(R.string.delete, this)
                .setNeutralButton(R.string.cancel, null).create();
        isDialogOpen = true;
        builder.show();
    }

    //for delete
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        delete_position = position;
        showDeleteDialog();
        return true;
    }

    // DialogInterface.OnClickListener method. Get the result of the Alert View.
    @Override
    public void onClick(DialogInterface dialog, int which){
        if (which == DialogInterface.BUTTON_NEGATIVE){
            removePlace(listViewAdapter.getItem(delete_position));
            listViewAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    getPlaces());
            listView.setAdapter(listViewAdapter);
        }
        isDialogOpen = false;
    }
}
