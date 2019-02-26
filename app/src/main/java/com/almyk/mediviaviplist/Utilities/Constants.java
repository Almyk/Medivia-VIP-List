package com.almyk.mediviaviplist.Utilities;

import java.util.Arrays;
import java.util.List;

public final class Constants {
    public static final String UPDATE_VIP_LIST_TAG = "updateviplist_tag";
    public static final String UPDATE_VIP_LIST_UNIQUE_NAME = "updateviplist_name";

    public static final String UPDATE_VIP_DETAIL_TAG = "updatevipdetail_tag";
    public static final String UPDATE_VIP_DETAIL_UNIQUE_NAME = "updatevipdetail_name";

    // Name of Notification Channel for verbose notifications of background work
    public static final CharSequence LOGIN_NOTIFICATION_CHANNEL_NAME =
            "Player Login Notifications";

    public static String LOGIN_NOTIFICATION_CHANNEL_DESCRIPTION =
            "Shows notifications whenever a player login";
    public static final CharSequence NOTIFICATION_TITLE = "Player has logged in";
    public static final String LOGIN_CHANNEL_ID = "LOGIN_NOTIFICATION" ;
    public static final int NOTIFICATION_ID = 1;

    public static final String DO_BGSYNC = "BACKGROUND_SYNCHRONIZATION";

    public static final String PLAYER_NOTIFICATION_ON = "Turn ON Notifications";
    public static final String PLAYER_NOTIFICATION_OFF = "Turn OFF Notifications";

    public static final String UPDATE_PLAYER_KEY = "update player";

    public static final String PLAYER_NAME = "player name";

    public static final String SERVER_KEY = "server key";
    public static final List<String> SERVERS = Arrays.asList("prophecy", "legacy", "destiny", "pendulum");
    public static final String PROPHECY = "Prophecy";
    public static final String LEGACY = "Legacy";
    public static final String DESTINY = "Destiny";
    public static final String PENDULUM = "Pendulum";

    public static final String UPDATE_HIGHSCORES_KEY = "update highscores key";
    public static final String UPDATE_HIGHSCORES_TAG = "update highscores tag";
    public static final String UPDATE_HIGHSCORES_UNIQUE_NAME = "update highscores name";
    public static final String UPDATE_HIGHSCORE_FOR = "UPDATE HIGHSCORE FOR: ";

    public Constants() {}
}
