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
        String lastName = "";
        if (user.getLastName() != null) {
            lastName = user.getLastName() + " ";
        }
        String name = "";
        if (user.getFirstName() != null) {
            name = user.getFirstName();
        } else if (user.getLastName() == null) {
            name = user.getUserName() == null ? user.getId().toString() : user.getUserName();
        }
        return "<a href=\"tg://user?id=" + user.getId()+ "\">" + lastName + name + "</a>";
    }

    public @interface Sex {
        String GIRL = "girl";
        String BOY = "boy";
    }
}
