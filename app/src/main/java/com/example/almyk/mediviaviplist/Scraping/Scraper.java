package com.example.almyk.mediviaviplist.Scraping;

import com.example.almyk.mediviaviplist.Database.PlayerEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class Scraper {
    private static final String BASE_URL_ONLINE = "https://medivia.online/community/online/";
    private static final String PROPHECY = "prophecy";
    private List<PlayerEntity> mOnlineList;

    private static final String BASE_URL_PLAYER = "https://medivia.online/community/character/";

    private Document mDoc;

    public List<PlayerEntity> scrapeOnline(String server) {
        getDocument(BASE_URL_ONLINE+server);
        PlayerEntity player = new PlayerEntity();

        if(!mOnlineList.isEmpty()) {
            mOnlineList.clear();
        }

        boolean first = true;
        Elements entries = mDoc.select("li");
        for(Element entry : entries) {
            if(first) { // to account for first list element showing the column names
                first = false;
                continue;
            }
            player.setName(entry.select("div[class=med-width-35]").text());
            player.setServer(server);
            player.setLevel(entry.select("div[class='med-width-25 med-text-right med-pr-40']").text());
            player.setVocation(entry.select("div[class=med-width-15]").text());

            mOnlineList.add(player);
        }

        return mOnlineList;
    }

    public PlayerEntity scrapePlayer(String name) {
        PlayerEntity player = new PlayerEntity(name);

        getDocument(BASE_URL_PLAYER+name);

        return player;
    }

    private void getDocument(String url) {
        try {
            mDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
