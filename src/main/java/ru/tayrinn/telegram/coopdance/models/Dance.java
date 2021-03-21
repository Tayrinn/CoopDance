package ru.tayrinn.telegram.coopdance.models;

import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

public class Dance {

    public final String message;
    public final String messageId;
    private final List<Dancer> girls = new ArrayList<>();
    private final List<Dancer> boys = new ArrayList<>();
    private final List<DancePair> pairs = new ArrayList<>();

    public Dance(String message, String messageId) {
        this.message = message;
        this.messageId = messageId;
    }

    public void processCommand(String command, User user) {
        switch (command) {
            case Commands.ADD_GIRL : addGirl(user); break;
            case Commands.ADD_BOY : addBoy(user); break;
            default: // do nothing
        }
    }

    public void addPair(Dancer boy, Dancer girl) {
        pairs.add(new DancePair(boy, girl));
    }

    public void addGirl(User user) {
        Dancer dancer = new Dancer();
        dancer.user = user;
        dancer.sex = Dancer.Sex.GIRL;
        addDancer(dancer);
    }

    public void addBoy(User user) {
        Dancer dancer = new Dancer();
        dancer.user = user;
        dancer.sex = Dancer.Sex.BOY;
        addDancer(dancer);
    }

    private void addDancer(Dancer dancer) {
        if (dancer.sex.equals(Dancer.Sex.GIRL)) {
            if (!boys.isEmpty()) {
                Dancer boy = boys.remove(0);
                pairs.add(new DancePair(dancer, boy));
            } else {
                girls.add(dancer);
            }
        } else {
            if (!girls.isEmpty()) {
                Dancer girl = girls.remove(0);
                pairs.add(new DancePair(girl, dancer));
            } else {
                boys.add(dancer);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(message).append("\n\n");
        sb.append("\uD83D\uDD7A+\uD83D\uDC83:\n");
        pairs.forEach(pair -> sb.append(pair).append("\n"));

        sb.append("\n\uD83D\uDC83:\n");
        girls.forEach(girl -> sb.append("\uD83D\uDD53").append(girl).append("\n"));

        sb.append("\n\uD83D\uDD7A:\n");
        boys.forEach(boy -> sb.append("\uD83D\uDD53").append(boy).append("\n"));

        return sb.toString();
    }
}
