package com.example.pilot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class LoginActivity extends Activity {


    Bundle currUser;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEdit = prefs.edit();

        if(prefs.getStringSet("usernames", null)==null){
            prefsEdit.putStringSet("usernames", new HashSet<String>());
            prefsEdit.commit();
        }

        if(savedInstanceState != null){

        }

        //what to do when creating a new User
        findViewById(R.id.NewUserbutton).
                setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        setContentView(R.layout.login_view);
                    }
                });
        //what to do when creating an existing user
        findViewById(R.id.ExistingUserbutton).
                setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        setContentView(R.layout.existinguserview);


                    }
                });
    }


    //onComplete function for when the user is done entering his information
    public void onComplete(View view){

        //editText fields
        EditText ETusername = (EditText)findViewById(R.id.username);
        EditText ETage = (EditText)findViewById(R.id.age);
        EditText ETsex = (EditText)findViewById(R.id.sex);
        EditText ETdominantHand = (EditText)findViewById(R.id.dominantHand);

        //to be used for the mapping below
        String username = ETusername.getText().toString();
        String age = ETage.getText().toString();
        String sex = ETsex.getText().toString();
        String dominantHand = ETdominantHand.getText().toString();


        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEdit = prefs.edit();

        Set<String> usernameSet = prefs.getStringSet("usernames", null);
        usernameSet.add(username);
        prefsEdit.putStringSet("usernames", usernameSet);

        prefsEdit.commit();

        currUser.putString(username, username);
        currUser.putString(username+"age", age);
        currUser.putString(username+"sex", sex);
        currUser.putString(username+"dominantHand", dominantHand);
        currUser.putString("username",username);
        currUser.putIntegerArrayList(username+"Tap Test Results", new ArrayList<Integer>());


        Intent welcomeIntent = new Intent(this, WelcomeActivity.class);
        welcomeIntent.putExtras(currUser);

        startActivity(welcomeIntent);

    }

    public void addButton(View view){
        Button button  = new Button(this);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        button.setLayoutParams(lp);
        button.setText(username);
        LinearLayout layout = (LinearLayout) findViewById(R.id.existinguserButtons);
        layout.addView(button);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putBundle(username, currUser);
        super.onSaveInstanceState(savedInstanceState);
    }


}

        /*
        commenting out this implementation until i see things working
        //mapping data to be used for sharedPreferences
        Map<String, String> userDataMap = new HashMap<String, String>();
        userDataMap.put(username, username);
        userDataMap.put(username+"age", age);
        userDataMap.put(username+"sex", sex);
        userDataMap.put(username+"dominantHand", dominantHand);

        //everything being stored in the defaultsharedpreferences
        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEdit = prefs.edit();

        //updating the maintained username set
        Set<String> usernameSet = prefs.getStringSet("usernames", defVals);
        usernameSet.add(username);
        prefsEdit.putStringSet("usernames", usernameSet);

        //create the mapping in the sharedPreferences
        for (String s : userDataMap.keySet()) {
            prefsEdit.putString(s, userDataMap.get(s));
        }

        prefsEdit.commit();*/