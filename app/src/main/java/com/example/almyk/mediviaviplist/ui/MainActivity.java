package com.example.almyk.mediviaviplist.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.almyk.mediviaviplist.Database.PlayerEntity;
import com.example.almyk.mediviaviplist.R;
import com.example.almyk.mediviaviplist.Scraping.Scraper;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = findViewById(R.id.tv_text);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Scraper scraper = new Scraper();
                final HashMap<String, PlayerEntity> onlineList = scraper.scrapeOnline("prophecy");
            }
        }).start();
    }
}
