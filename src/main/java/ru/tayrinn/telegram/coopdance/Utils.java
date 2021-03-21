package ru.tayrinn.telegram.coopdance;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Utils {

    public static String convertToUtf8String(String string) {
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decodeUtf8(String string) {
        try {
            return URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
