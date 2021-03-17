package ru.tayrinn.telegram.coopdance.models;

import org.telegram.telegrambots.meta.api.objects.User;

public class Dancer {

    @Sex
    public String sex;
    public String ashNumber;
    public User user;

    @Override
    public String toString() {
        return "<a href=\"@" + user.getUserName()+ "\">" + user.getLastName() + " " + user.getFirstName() + "</a>";
    }

    @interface Sex {
        String GIRL = "girl";
        String BOY = "boy";
    }
}
