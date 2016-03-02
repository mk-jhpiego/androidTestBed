package com.example.mkabila.donotpressthisbutton;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class DoNotEat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_not_eat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_do_not_eat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void showFailedToConvertDialog(String customMessage){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog
                .setTitle("Error processing data entered")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int id) {
                        dlg.dismiss();
                    }
                })
                .setMessage(customMessage.equals("") ? "An error occurred trying to process the values entered" : customMessage)
                .create().show();
    }

    public void clearAndStartOver(final View view){
        ((EditText)findViewById(R.id.tfirstName)).setText("");
        ((EditText)findViewById(R.id.tsurname)).setText("");
        ((EditText)findViewById(R.id.tnrc)).setText("");
        ((EditText)findViewById(R.id.tphoneNumber)).setText("");
    }

    public void ProcessData(final View view){
        //we show that someone ate the forbidden fruit
        MainClassBody currentRecord = new MainClassBody();
        currentRecord.firstName = ((EditText)findViewById(R.id.tfirstName)).getText().toString();
        currentRecord.surName = ((EditText)findViewById(R.id.tsurname)).getText().toString();
        currentRecord.nrc = ((EditText)findViewById(R.id.tnrc)).getText().toString();
        currentRecord.phoneNumber = ((EditText)findViewById(R.id.tphoneNumber)).getText().toString();
        String asJson="";

        try{
            asJson = currentRecord.toJson();
        }catch(JSONException e){
            //we show a failed to convert dialog
            showFailedToConvertDialog("Error converting values to Json");
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog
                .setTitle("Confirm Save action")
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int id) {
                    }
                })
                .setPositiveButton("Save and Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg, int id) {
                        saveAndClearForm();
                    }
                })
                .setMessage("Do you want to Save the information and enter a new record")
                .create().show();

    }

    public void saveAndClearForm(){
        //we save to temp store
        SQLiteDatabase mainDb = SQLiteDatabase.openOrCreateDatabase ("mainDb.db", null);
        Short tValue = 1;
        //Toast.makeText(this,"Database Created Successfully", tValue).show();


        //and clear the form
        mainDb.close();
    }

    public class MainClassBody{
        public String firstName;
        public String surName;
        public String nrc;
        public String phoneNumber;

        public String toJson() throws JSONException {
            try{
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("firstName",firstName);
                jsonObj.put("surName",surName);
                jsonObj.put("nrc",nrc);
                jsonObj.put("phoneNumber",phoneNumber);

                return jsonObj.toString();
            }catch  (JSONException e){
                e.printStackTrace();
                throw(e);
            }
        }
    }
}
