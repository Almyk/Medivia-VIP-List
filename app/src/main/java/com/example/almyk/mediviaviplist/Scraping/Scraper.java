package com.example.almyk.mediviaviplist.Scraping;

import android.util.Log;

import com.example.almyk.mediviaviplist.Database.PlayerEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Scraper {
    private static final String TAG = Scraper.class.getSimpleName();

    private static final String BASE_URL_ONLINE = "https://medivia.online/community/online/";
    private static final String PROPHECY = "prophecy";
    private HashMap<String, PlayerEntity> mOnlineList = new HashMap<>();
    //private List<PlayerEntity> mOnlineList;

    private static final String BASE_URL_PLAYER = "https://medivia.online/community/character/";

    private Document mDoc;

    public Scraper() {
    }

    public HashMap<String, PlayerEntity> scrapeOnline(String server) {
        getDocument(BASE_URL_ONLINE+server);
        int count = 0;

        if(!mOnlineList.isEmpty()) {
            mOnlineList.clear();
        }

        Element table = mDoc.selectFirst("div[class='med-width-100 med-text-left']");
        Elements entries = table.select("li");

        boolean first = true;
        for(Element entry : entries) {
            if(first) { // to account for first list element showing the column names
                first = false;
                continue;
            }
            PlayerEntity player = new PlayerEntity();
            player.setName(entry.select("div[class=med-width-35]").text());
            player.setServer(server);
            player.setLevel(entry.select("div[class='med-width-25 med-text-right med-pr-40']").text());
            player.setVocation(entry.select("div[class=med-width-15]").text());
            player.setOnline(true);
//            Log.d(TAG, "Player: " + player.getName() + " " + player.getLevel() + " " + player.getVocation() + " " + player.getServer());

            mOnlineList.put(player.getName(), player);
            count++;
        }

//        Log.d(TAG, count + " Players online.");
        return mOnlineList;
    }

    public PlayerEntity scrapePlayer(String name) {
        getDocument(BASE_URL_PLAYER+name);
        PlayerEntity player = new PlayerEntity();
        String text;

        Elements elements = mDoc.select("div[class='med-width-50 med-white-space-normal']");
        elements = elements.select("div[class='med-width-50']");

        text = elements.get(1).text(); // name
        player.setName(text);
        text = elements.get(7).text(); // vocation
        player.setVocation(text);
        text = elements.get(9).text(); // level
        player.setLevel(text);
        text = elements.get(11).text(); // server
        player.setServer(text);
        text = elements.get(19).text(); // online status
        if(text.equals("Online")) {
            player.setOnline(true);
        } else {
            player.setOnline(false);
        }

        Log.d(TAG, "Player: " + player.getName() + " " + player.getLevel() + " " + player.getVocation() + " " + player.getServer());

        return player;
    }

    private void getDocument(String url) {
        try {
            mDoc = Jsoup.connect(url).get();
//            Log.d(TAG, "Succesfully connected to " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
