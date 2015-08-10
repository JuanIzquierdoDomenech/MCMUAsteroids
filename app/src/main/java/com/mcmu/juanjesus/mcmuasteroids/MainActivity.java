package com.mcmu.juanjesus.mcmuasteroids;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //region Private Member Variables
    private Button btnLaunchAboutActivity;
    private Button btnExit;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linkUI();
    }

    private void linkUI()
    {
        btnLaunchAboutActivity = (Button)findViewById(R.id.btn_asteroids_about);
        btnLaunchAboutActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutActivityIntent = new Intent(v.getContext(), AboutActivity.class);
                aboutActivityIntent.putExtra("SomeData", "This is some data :D");
                startActivity(aboutActivityIntent);
            }
        });

        btnExit = (Button)findViewById(R.id.btn_asteroids_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
