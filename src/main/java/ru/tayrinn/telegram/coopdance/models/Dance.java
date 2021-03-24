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

    public Dancer findDancer(User user) {
        for (Dancer girl : girls) {
            if (girl.user.getId().equals(user.getId())) {
                return girl;
            }
        }
        for (Dancer boy : boys) {
            if (boy.user.getId().equals(user.getId())) {
                return boy;
            }
        }
        for (DancePair pair : pairs) {
            if (pair.getBoy().user.getId().equals(user.getId())) {
                return pair.getBoy();
            }
            if (pair.getGirl().user.getId().equals(user.getId())) {
                return pair.getGirl();
            }
        }
        return null;
    }

    public boolean findSingleDancerAndRemove(User user) {
        for (int i = 0; i < girls.size(); i++) {
            if (girls.get(i).user.getId().equals(user.getId())) {
                girls.remove(i);
                return true;
            }
        }
        for (int i = 0; i < boys.size(); i++) {
            if (boys.get(i).user.getId().equals(user.getId())) {
                boys.remove(i);
                return true;
            }
        }
        return false;
    }

    public void addPair(Dancer boy, Dancer girl) {
        pairs.add(new DancePair(girl, boy));
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
        StringBuilder sb = new StringBuilder(message + "").append("\n\n");
        if (!pairs.isEmpty()) {
            sb.append("\uD83D\uDD7A+\uD83D\uDC83:\n");
            for (int i = 0; i < pairs.size(); i++) {
                sb.append(i + 1).append(". ").append(pairs.get(i)).append("\n");
            }
        }
        if (!girls.isEmpty()) {
            sb.append("\n\uD83D\uDD53 \uD83D\uDC83:\n");
            for (int i = 0; i < girls.size(); i++) {
                sb.append(i + 1).append(". ").append(girls.get(i)).append("\n");
            }
        }
        if (!boys.isEmpty()) {
            sb.append("\n\uD83D\uDD53 \uD83D\uDD7A:\n");
            for (int i = 0; i < boys.size(); i++) {
                sb.append(i + 1).append(". ").append(boys.get(i)).append("\n");
            }
        }

        return sb.toString();
    }
}
