package com.mediacollector.tools;

public class StringFilter {

    /**
     * Wandelt url Sonderzeichen um
     * @param s Zu bearbeitender String
     */
    public static String normalizeString(String s) {
        s = s.replaceAll("%DC","Ü").
                replaceAll("%C4","Ä").
                replaceAll("%D6","Ö").
                replaceAll("%FC","ü").
                replaceAll("&#252;","ü").
                replaceAll("%E4","ä").
                replaceAll("%F6","ö").
                replaceAll("%DF","ß").
                replaceAll("&#225;","ß").
                replaceAll("&amp;","&").
                replaceAll("%E1","á").
                replaceAll("&#233;","é");

        return s;
    }

}