package com.zyloon.underscore.movie;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by _underscore on 01-05-2016.
 */
public class SummaryPopup extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        String summaryData = (String)getIntent().getExtras().get("Summary");
        setContentView(R.layout.summary_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int)(dm.widthPixels *0.9);
        int height = (int)(dm.heightPixels * 0.9);

        getWindow().setLayout(width, height);

        TextView summary = (TextView) findViewById(R.id.summarytext);
        summary.setText(summaryData);

        Button b = (Button) findViewById(R.id.closebutton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
