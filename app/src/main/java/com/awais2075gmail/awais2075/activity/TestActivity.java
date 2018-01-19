package com.awais2075gmail.awais2075.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.awais2075gmail.awais2075.R;

public class TestActivity extends AppCompatActivity {

    private static final String SHARED_PREF_VALUE = "mysharedpref";
    private static final String SHARED_PREF_KEY = "keyname";

    private EditText edit_value;
    private TextView text_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        edit_value = findViewById(R.id.edit_value);
        text_value = findViewById(R.id.text_value);
        displayName();


        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveName();
                displayName();
            }
        });
    }

    private void saveName() {
        String name = edit_value.getText().toString();

        if (name.isEmpty()) {
            edit_value.setError("Name required");
            edit_value.requestFocus();
            return;
        }

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_VALUE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(SHARED_PREF_KEY, name);

        editor.apply();

        edit_value.setText("");
    }

    private void displayName() {
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_VALUE, MODE_PRIVATE);
        String name = sp.getString(SHARED_PREF_KEY, null);

        if (name != null) {
            text_value.setText("Welcome " + name);
        }
    }

}

