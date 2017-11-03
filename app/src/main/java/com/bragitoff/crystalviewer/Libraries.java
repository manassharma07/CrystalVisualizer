package com.bragitoff.crystalviewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Libraries extends AppCompatActivity {
    TextView License;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libraries);
        License=(TextView)findViewById(R.id.libs);
        License.setText(Html.fromHtml(getResources().getString(R.string.license)));
    }
}
