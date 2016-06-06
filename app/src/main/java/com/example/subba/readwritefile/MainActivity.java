package com.example.subba.readwritefile;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private String filename = "test.txt";
    private String filepath = "MyFileStorage";
    File myExternalFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button saveToExternalStorage = (Button) findViewById(R.id.buttonWrite);
        saveToExternalStorage.setOnClickListener(this);

        Button readFromExternalStorage = (Button) findViewById(R.id.buttonRead);
        readFromExternalStorage.setOnClickListener(this);

        Button deleteFromExternalStorage = (Button) findViewById(R.id.buttonDel);
        deleteFromExternalStorage.setOnClickListener(this);

        //check if external storage is available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            saveToExternalStorage.setEnabled(false);
        }
        else {
            myExternalFile = new File(getExternalFilesDir(filepath), filename);
        }

    }
    public void onClick(View v) {

        EditText myInputText = (EditText) findViewById(R.id.editText);
        TextView resText = (TextView) findViewById(R.id.resText);
        TextView responseText = (TextView) findViewById(R.id.responseText);
        String myData = "";

        switch (v.getId()) {
            case R.id.buttonWrite:
                try {
                    File myFile = new File(Environment.getExternalStorageDirectory(), filename);
                    if (!myFile.exists())
                        myFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(myFile);
                    fos.write(myInputText.getText().toString().getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myInputText.setText("");
                Toast.makeText(getApplicationContext(), "Saved file", Toast.LENGTH_SHORT).show();
                /*responseText
                        .setText("MySampleFile.txt saved to External Storage...");
              */
                break;

            case R.id.buttonRead:
                try {
                    File myFile = new File(Environment.getExternalStorageDirectory(), filename);
                    FileInputStream fis = new FileInputStream(myFile);
                    DataInputStream in = new DataInputStream(fis);
                    BufferedReader br =
                            new BufferedReader(new InputStreamReader(in));
                    String strLine;
                    while ((strLine = br.readLine()) != null) {
                        myData = myData + strLine;
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*myInputText.setText(myData);*/
                resText.setText(myData);
                Toast.makeText(getApplicationContext(), "Data Read from file", Toast.LENGTH_SHORT).show();
                break;

            case R.id.buttonDel:
                try {
                    File myFile = new File(Environment.getExternalStorageDirectory(), filename);
                    myFile.delete();
                    Toast.makeText(getApplicationContext(),"Deleted file",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

}
