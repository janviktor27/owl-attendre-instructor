package com.example.zero.Instructor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class ChangePass extends AppCompatActivity implements View.OnClickListener {
    Button btChangePass;
    EditText etOldpass, etNewpass, etConfirmpass;
    String password, username;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        //every activity needs this upto
        pref = getSharedPreferences("logi.conf", Context.MODE_PRIVATE);
        editor = pref.edit();

        username = pref.getString("username", "");
        password = pref.getString("password", "");

        if((username.equals("") && password.equals(""))){
            Intent login = new Intent(ChangePass.this, LoginActivity.class);
            startActivity(login);
        }

        Log.d(TAG, pref.getString("username", ""));
        Log.d(TAG, pref.getString("password", ""));
        //here end

        btChangePass =(Button)findViewById(R.id.btnChpass);
        etOldpass = (EditText)findViewById(R.id.etOldPassword);
        etNewpass = (EditText)findViewById(R.id.etNewPassword);
        etConfirmpass = (EditText)findViewById(R.id.etConfimPassword);
        btChangePass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        HashMap data = new HashMap();
        data.put("cin", username);
        data.put("etOldPass", etOldpass.getText().toString());
        data.put("etNewPass", etNewpass.getText().toString());
        data.put("etConfirmPass", etConfirmpass.getText().toString());

        PostResponseAsyncTask task = new PostResponseAsyncTask(ChangePass.this, data, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.d(TAG, s);
                if(s.contains("success")){
                    editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    Intent out = new Intent(ChangePass.this, LoginActivity.class);
                    startActivity(out);
                }
            }
        });

        task.execute("http://10.0.2.2/owl_attendance/chpass.php");
    }
}
