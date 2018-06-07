package com.latenightcode.write.writer.Config;

public class Config {

    public static final int NOTIFICATION_REQUEST_CODE = 1;
    public static final String ALARM_TIME_SAVED = "ALARM_SAVED";
    public static final String TOTAL_WORDS_SO_FAR_CHILD_NAME = "total_words_so_far";

    public static int wordCount(String writtenTexts){
        if (writtenTexts == null)
            return 0;
        return writtenTexts.trim().split("\\s+").length;
    }

    public static int wordPerMinuteReadTime(int  words){
        return words / 200;
    }

}
