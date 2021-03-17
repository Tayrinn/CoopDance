package ru.tayrinn.telegram.coopdance.models;

public class Commands {
    public static final String ADD_GIRL = "add_girl";
    public static final String ADD_BOY = "add_boy";
    public static final String ADD_GIRL_AND_BOY = "add_girl_and_boy";
    public static final String ADD_BOY_AND_GIRL = "add_boy_and_girl";
    public static final String CANCEL = "cancel";

    public static String format(String command, String systemInfo) {
        return command + "," + systemInfo;
    }

    public static String parseCommand(String value) {
        return value.substring(0, value.indexOf(","));
    }

    public static String parseSystemInfo(String value) {
        return value.substring(value.indexOf(",") + 1);
    }
}
