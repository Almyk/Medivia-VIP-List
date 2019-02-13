package com.example.almyk.mediviaviplist.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.almyk.mediviaviplist.Database.PlayerEntity;
import com.example.almyk.mediviaviplist.R;
import com.example.almyk.mediviaviplist.Scraping.Scraper;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
