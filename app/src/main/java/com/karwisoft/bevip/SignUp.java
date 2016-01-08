package com.karwisoft.bevip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    HttpURLConnection urlConnection = null;
    URL url = null;
    String urlstring;
    EditText nom,prenom,mail,password,confpass;
    String name,lastname,email,pass,conf;
    Button save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_container_signup);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackgroundColor(Color.parseColor("#0099cc"));
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        save=(Button)findViewById(R.id.button);
        nom=(EditText)findViewById(R.id.name);
        prenom=(EditText)findViewById(R.id.lastname);
        mail=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.pass);
        confpass=(EditText)findViewById(R.id.confpass);




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=nom.getText().toString();
                lastname=prenom.getText().toString();
                email=mail.getText().toString();
                pass=password.getText().toString();
                conf=confpass.getText().toString();

                //urlstring="http://www.karwisoft.com/bevip/register.php?nom=med&prenom=swiden&email=mswiden42@gmail.com&password=1234567890";
                //new Save().execute();


                if(name.trim().equals("")||lastname.trim().equals("")||email.trim().equals("")||pass.trim().equals("")||conf.trim().equals("")){
                    Toast.makeText(getApplicationContext(), "check your infromation", Toast.LENGTH_LONG).show();
                }else if(!conf.equals(pass)){
                    Toast.makeText(getApplicationContext(), "enter the same password", Toast.LENGTH_LONG).show();
                }else if(!isEmailValid(email)){
                    Toast.makeText(getApplicationContext(), "please verify your email", Toast.LENGTH_LONG).show();
                }else{
                    if(!isNetworkAvailable(getApplicationContext())){
                        Toast.makeText(getApplicationContext(), "please check your internet connection", Toast.LENGTH_LONG).show();

                    }else{
                        try {
                            urlstring="http://www.karwisoft.com/bevip/register.php?nom="+ URLEncoder.encode(name, "Windows-1252")
                                    +"&prenom="+URLEncoder.encode(lastname, "Windows-1252")
                                    +"&mail="+URLEncoder.encode(email, "Windows-1252")
                                    +"&password="+URLEncoder.encode(pass, "Windows-1252");
                            new Save().execute();

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }
        });













    }
    private class Save extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {

        }
        protected String doInBackground(String... urls) {

            String test="not ok";
                try {
                    url = new URL(urlstring);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setChunkedStreamingMode(0);
                    test="ok";
                    OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                    //writeStream(out);


                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    //readStream(in);



                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }


            return test;
        }




        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), url+"", Toast.LENGTH_LONG).show();

            if(result.equals("ok")) {
                Toast.makeText(getApplicationContext(), "registred", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getApplicationContext(), "There is somthing wrong", Toast.LENGTH_LONG).show();

            }
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
