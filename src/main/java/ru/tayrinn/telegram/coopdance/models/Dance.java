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
        StringBuilder sb = new StringBuilder(message).append("\n\n");
        sb.append("Партнёрши:\n");
        girls.forEach(girl -> sb.append(girl).append("\n"));
        sb.append("\nПартнёры:\n");
        boys.forEach(boy -> sb.append(boy).append("\n"));
        return sb.toString();
    }
}
