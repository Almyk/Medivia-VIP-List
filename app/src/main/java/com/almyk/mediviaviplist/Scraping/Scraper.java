package com.almyk.mediviaviplist.Scraping;

import android.util.Log;

import com.almyk.mediviaviplist.Database.PlayerEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

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

        if(elements.size() == 0) {
            return null;
        }
        int i = 1;
        text = elements.get(i).text(); // name
        player.setName(text);

        if(elements.get(2).text().equals("previous name:")) {
            i += 2; // take previous name into account when finding vocation
        }


        i += 6; // 7 or 9
        text = elements.get(i).text(); // vocation
        player.setVocation(text);
        i += 2; // 9
        text = elements.get(i).text(); // level
        player.setLevel(text);
        i += 2; // 11
        text = elements.get(i).text(); // server
        player.setServer(text);

        if(elements.get(i+3).text().equals("guild:")) {
            i += 2; // if player has a guild
        }

        if(elements.get(i+3).text().equals("house:")) {
            i += 2; // if player has a house
        }

        i += 6; // 17
        text = elements.get(i).text(); // online status
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
