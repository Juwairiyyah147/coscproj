package com.example.cosc_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static android.text.TextUtils.isEmpty;

public class login extends AppCompatActivity {
    Button login;
    EditText username, password;
    private TextView textView;
    private RequestQueue queue;
    JsonObjectRequest objectRequest;
    private static final String Key_Username = "Username";
    private static final String Key_Password = "Passw";
    private String user;
    private String passw;
    String accessTkn;

    JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.pass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogin();
                queue.add(objectRequest);
            }
        });
        textView = (TextView) findViewById(R.id.register);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(login.this, registration.class);
                startActivity(register);
            }
        });
    }
    public void showLogin() {
        user = username.getText().toString();
        passw = password.getText().toString();
        if(isEmpty(user)||isEmpty(passw)){
            Toast toast = Toast.makeText(getApplicationContext(),user+passw, Toast.LENGTH_LONG);
            toast.show();
        }
        String URL = "https://cbit-qp-api.herokuapp.com/user-login";
        data = new JSONObject();
        try {
            data.put("uname", user);
            data.put("password", passw);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        queue = Volley.newRequestQueue(this);
        objectRequest = new JsonObjectRequest(Request.Method.POST,
                URL,
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            accessTkn=response.getString("access_token");
                            //MainPage.accessTkn = response.getString("access_token");
                            //MainPage.username = username;
                            opennext();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }

    private void opennext() {
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
    }


    }
