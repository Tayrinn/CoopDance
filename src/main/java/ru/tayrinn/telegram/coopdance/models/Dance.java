package ru.tayrinn.telegram.coopdance.models;

import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

public class Dance {

    public final String messageId;
    private List<Dancer> girls = new ArrayList<>();
    private List<Dancer> boys = new ArrayList<>();
    private List<DancePair> pairs = new ArrayList<>();

    public Dance(String messageId) {
        this.messageId = messageId;
    }

    public void addGirl(User user) {
        Dancer dancer = new Dancer();
        dancer.user = user;
        dancer.sex = Dancer.Sex.GIRL;
        girls.add(dancer);
    }

    public void addBoy(User user) {
        Dancer dancer = new Dancer();
        dancer.user = user;
        dancer.sex = Dancer.Sex.BOY;
        boys.add(dancer);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Партнёрши:\n");
        girls.forEach(girl -> sb.append(girl).append("\n"));
        sb.append("\nПартнёры:\n");
        boys.forEach(boy -> sb.append(boy).append("\n"));
        return sb.toString();
    }
}
