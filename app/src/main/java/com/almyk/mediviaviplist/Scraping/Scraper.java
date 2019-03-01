package com.almyk.mediviaviplist.Scraping;

import android.util.Log;

import com.almyk.mediviaviplist.Database.HighscoreEntity;
import com.almyk.mediviaviplist.Database.PlayerEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Scraper {
    private static final String TAG = Scraper.class.getSimpleName();

    private static final String BASE_URL_ONLINE = "https://medivia.online/community/online/";
    private static final String PROPHECY = "prophecy";
    //private HashMap<String, PlayerEntity> mOnlineList = new HashMap<>();

    private static final String BASE_URL_PLAYER = "https://medivia.online/community/character/";

    private static final String BASE_URL_HIGHSCORE = "https://medivia.online/highscores/";
    private final List<String> vocations = Arrays.asList("all", "none", "sorcerers", "clerics", "scouts", "warriors");
    private final List<String> skills = Arrays.asList("level", "maglevel", "fist", "club", "sword", "axe", "distance", "shielding", "fishing", "mining");

    private Document mDoc;

    public Scraper() {
    }

    public HashMap<String, PlayerEntity> scrapeOnline(String server) {
        HashMap<String, PlayerEntity> onlineList = new HashMap<>();
        boolean success = getDocument(BASE_URL_ONLINE+server);
        if(!success) {
            return null;
        }
//        int count = 0;

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
            player.setLastLogin(entry.select("div[class='med-width-25']").text());
//            Log.d(TAG, "Player: " + player.getName() + " " + player.getLevel() + " " + player.getVocation() + " " + player.getServer());

            onlineList.put(player.getName(), player);
//            count++;
        }

//        Log.d(TAG, count + " Players online.");
        return onlineList;
    }

    public PlayerEntity scrapePlayer(String name) {
        boolean success = getDocument(BASE_URL_PLAYER+name);
        if(!success) {
            return null;
        }

        PlayerEntity player = new PlayerEntity();
        boolean hasGuild = false;
        boolean hasHouse = false;

//        Elements elements = mDoc.select("div[class='med-width-50 med-white-space-normal']");
        Elements allElements = mDoc.select("div[class='med-p-10']");
        Elements elements = allElements.select("div[class='med-width-50']");

        if(elements.size() == 0) {
            return null;
        }

        for(int i = 0; i+1 < elements.size(); i++) {
            String key = elements.get(i).text();
            String value = elements.get(i+1).text();

            switch (key) {
                case "name:":
                    player.setName(value);
                    break;
                case "previous name:":
                    player.setPreviousName(value);
                    break;
                case "sex:":
                    player.setSex(value);
                    break;
                case "profession:":
                    player.setVocation(value);
                    break;
                case "level:":
                    player.setLevel(value);
                    break;
                case "world:":
                    player.setServer(value);
                    break;
                case "residence:":
                    player.setResidence(value);
                    break;
                case "guild:":
                    hasGuild = true;
                    break;
                case "house:":
                    hasHouse = true;
                    break;
                case "last login:":
                    player.setLastLogin(value);
                    break;
                case "status:":
                    if(value.equals("Online")) {
                        player.setOnline(true);
                    } else {
                        player.setOnline(false);
                    }
                    break;
                case "account status:":
                    player.setAccountStatus(value);
                    break;
                case "banishment:":
                    player.setBanishment(value);
                default:
                    break;
            }
        }

        Elements guildAndHouse = allElements.select("div[class='med-width-50 black-link']");
        if(hasGuild) {
            String value = guildAndHouse.get(0).text();
            player.setGuild(value);
            if(hasHouse) {
                value = guildAndHouse.get(1).text();
                player.setHouse(value);
            }
        } else if(hasHouse) {
            String value = guildAndHouse.get(0).text();
            player.setHouse(value);
        }

        Elements comment = allElements.select("div[class='med-width-75 med-text-italic med-vertical-middle']");
        if(comment != null && comment.size() > 0) {
            String value = comment.get(0).text();
            player.setComment(value);
        }

//        Log.d(TAG, "Player: " + player.getName() + " " + player.getLevel() + " " + player.getVocation() + " " + player.getServer());
//        Log.d(TAG, player.getGuild() + " " + player.getHouse() + " " + player.getSex() + " " + player.getComment());
//        Log.d(TAG, "" + player.getAccountStatus());

        return player;
    }

    public List<HighscoreEntity> scrapeHighscore(String server) {
        List<HighscoreEntity> highscores = new ArrayList<>();
        String urlServer = BASE_URL_HIGHSCORE + server + "/";

        for(String voc : vocations) {
            String urlVoc = urlServer + voc + "/";

            for(String skill : skills) {
                String urlSkill = urlVoc + skill;
                Log.d(TAG, "Current URL: " + urlSkill);

                boolean success = getDocument(urlSkill);
                if(!success) {
                    continue;
                }

                Document doc = mDoc.clone();

                Elements table = doc.select("div[class='content med-pt-20 med-border-top-grey rank']");
                Elements entries = table.select("li");

                boolean first = true;
                for(Element entry : entries) {
                    if (first) { // to account for first list element showing the column names
                        first = false;
                        continue;
                    }
                    int rank = Integer.parseInt(entry.select("div[class='nr']").text());
                    String name = entry.select("div[class='med-width-66']").text();
                    String value = entry.select("div[class='med-width-35 med-text-right med-pr-40']").text();

                    HighscoreEntity newEntry = new HighscoreEntity(server, skill, rank, name, value, voc);
                    highscores.add(newEntry);
//                    Log.d(TAG, "New highscore entry: " + server + " " + skill + " " + rank + " " + name + " " + value);
                }
            }
        }

        return highscores;
    }

    public List<HighscoreEntity> scrapeHighscoreByServerAndSkill(String server, String skill) {
        List<HighscoreEntity> highscores = new ArrayList<>();
        String urlServer = BASE_URL_HIGHSCORE + server + "/";

        for(String voc : vocations) {
            String url = urlServer + voc + "/" + skill;
            Log.d(TAG, "Current URL: " + url);

            boolean success = getDocument(url);
            if(!success) {
                continue;
            }

            Document doc = mDoc.clone();

            Elements table = doc.select("div[class='content med-pt-20 med-border-top-grey rank']");
            Elements entries = table.select("li");

            boolean first = true;
            for(Element entry : entries) {
                if (first) { // to account for first list element showing the column names
                    first = false;
                    continue;
                }
                int rank = Integer.parseInt(entry.select("div[class='nr']").text());
                String name = entry.select("div[class='med-width-66']").text();
                String value = entry.select("div[class='med-width-35 med-text-right med-pr-40']").text();

                HighscoreEntity newEntry = new HighscoreEntity(server, skill, rank, name, value, voc);
                highscores.add(newEntry);
//                    Log.d(TAG, "New highscore entry: " + server + " " + skill + " " + rank + " " + name + " " + value);
            }

        }

        return highscores;
    }

    private boolean getDocument(String url) {
        try {
            mDoc = Jsoup.connect(url).get();
//            Log.d(TAG, "Succesfully connected to " + url);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
