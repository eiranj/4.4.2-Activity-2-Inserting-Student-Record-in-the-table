package com.example.a442activity2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateNewStudentRec extends AppCompatActivity {

    private static Button btnQuery;
    private static EditText last, first, initial, contact, crse, yr;
    private static JSONParser jParser = new JSONParser();
    private static String urlHost = "http://192.168.0.105/studentinfo/InsertTrans.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    private static String lname = "";
    public static String fname = "";
    public static String mi = "";
    public static String cno = "";
    public static String course = "";
    public static String year = "";

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_student_rec);
        btnQuery = (Button) findViewById(R.id.btnQuery);
        last = (EditText) findViewById(R.id.lname);
        first = (EditText) findViewById(R.id.fname);
        initial = (EditText) findViewById(R.id.mi);
        contact = (EditText) findViewById(R.id.cno);
        crse = (EditText) findViewById(R.id.course);
        yr = (EditText) findViewById(R.id.year);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lname = last.getText().toString();
                fname = first.getText().toString();
                mi = initial.getText().toString();
                cno = contact.getText().toString();
                course = crse.getText().toString();
                year = yr.getText().toString();
                new uploadDatatoURL().execute();
            }
        });

    }
    private class uploadDatatoURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(CreateNewStudentRec.this);

        public uploadDatatoURL() {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params){
            int nSuccess;
            try {
                ContentValues cv = new ContentValues();
                cPostSQL = " '" + lname + "' , '" + fname + "' , '" + mi + "' , '" + cno + "'  " + course + "'  " + year + "'  ";
                cv.put("code", cPostSQL);

                JSONObject json = jParser.makeHTTPRequest(urlHost, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } else {
                    return "HTTPSERVER_ERROR";
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            pDialog.dismiss();
            String isEmpty = "";
            AlertDialog.Builder alert = new AlertDialog.Builder(CreateNewStudentRec.this);
            if (s !=null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) {
                }
                Toast.makeText(CreateNewStudentRec.this, s, Toast.LENGTH_SHORT).show();
            }else{
                alert.setMessage("Query Interrupted ... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }

    }
}