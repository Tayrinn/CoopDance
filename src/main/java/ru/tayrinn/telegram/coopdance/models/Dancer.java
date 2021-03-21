package ru.tayrinn.telegram.coopdance.models;

import org.telegram.telegrambots.meta.api.objects.User;

public class Dancer {

    @Sex
    public String sex;
    public String ashNumber;
    public String stubName;
    public User user;

    @Override
    public String toString() {
        if (user == null) {
            return stubName;
        }
        return "<a href=\"tg://user?id=" + user.getId()+ "\">" + user.getLastName() + " " + user.getFirstName() + "</a>";
    }

    public @interface Sex {
        String GIRL = "girl";
        String BOY = "boy";
    }
}
