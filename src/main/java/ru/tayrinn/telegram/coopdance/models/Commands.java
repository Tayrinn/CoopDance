package ru.tayrinn.telegram.coopdance.models;

import com.google.gson.Gson;

public class Commands {
    public static final String ADD_GIRL = "add_girl";
    public static final String ADD_BOY = "add_boy";
    public static final String ADD_GIRL_AND_BOY = "add_girl_and_boy";
    public static final String ADD_BOY_AND_GIRL = "add_boy_and_girl";
    public static final String CANCEL = "cancel";
    private static Gson gson = new Gson();


    public static String format(String command, String message) {
        CallbackData callbackData = new CallbackData();
        callbackData.command = command;
        callbackData.message = message;
        return gson.toJson(callbackData);
    }

    public static CallbackData parseCommand(String value) {
        return gson.fromJson(value, CallbackData.class);
    }

    public static String toJson(Object data) {
        return gson.toJson(data);
    }

}
