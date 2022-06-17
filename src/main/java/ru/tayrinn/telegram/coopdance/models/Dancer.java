package ru.tayrinn.telegram.coopdance.models;

import org.telegram.telegrambots.meta.api.objects.User;

public class Dancer {

    @Sex
    public String sex;
    public String ashNumber;
    public String stubName;
    public User user;
    private Integer number;
    private static Integer lastNumber = 0;

    public Dancer() {
        this.number = Dancer.lastNumber++;
    }
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

    public boolean isUser(User another) {
        return user != null && user.getId().equals(another.getId());
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public @interface Sex {
        String GIRL = "girl";
        String BOY = "boy";
    }
}
