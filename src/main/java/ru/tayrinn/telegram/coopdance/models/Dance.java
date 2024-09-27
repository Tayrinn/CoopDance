package ru.tayrinn.telegram.coopdance.models;

import org.telegram.telegrambots.meta.api.objects.User;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

public class Dance {

    public final static String KEY_MESSAGE_ID = "key_message_id";
    public final static String KEY_JSON = "key_json";

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
    public Dancer findDancerByUserName(String userName) {
        return findDancerByUserName(userName, null);
    }

    public Dancer findDancerByUserName(String userName, Dancer defaultDancer) {
        int i = 0;
        for (Dancer girl: girls) {
            if (girl.user.getUserName().equals(userName) || girl.stubName.equals(userName))
                return girl;
            ++i;
        }
        i = 0;
        for (Dancer boy: boys) {
            if (boy.user.getUserName().equals(userName) || boy.stubName.equals(userName))
                return boy;
            ++i;
        }
        return defaultDancer;
    }
    public boolean hasDancer(User user) {
        Long userId = user.getId();
        for (Dancer girl : girls) {
            if (girl.user != null && girl.user.getId().equals(userId)) {
                return true;
            }
        }
        for (Dancer boy : boys) {
            if (boy.user != null && boy.user.getId().equals(userId)) {
                return true;
            }
        }
        for (DancePair pair : pairs) {
            if (pair.getBoy().user != null && pair.getBoy().user.getId().equals(userId)) {
                return true;
            }
            if (pair.getGirl().user != null && pair.getGirl().user.getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public boolean findSingleDancerAndRemove(User user) {
        Long userId = user.getId();
        for (int i = 0; i < girls.size(); i++) {
            if (girls.get(i).isUser(user)) {
                girls.remove(i);
                return true;
            }
        }
        for (int i = 0; i < boys.size(); i++) {
            if (boys.get(i).isUser(user)) {
                boys.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean findPairAndRemoveDancer(User user) {
        ///TODO: надо отрефакторить, код повторяется
        for (int i = 0; i < pairs.size(); i++) {
            if (pairs.get(i).getBoy().isUser(user)) {
                if (pairs.get(i).getGirl().user == null) {
                    pairs.remove(i);
                    return true;
                }
                Dancer girl = pairs.get(i).getGirl();
                addGirl(girl.user);
                //addDancerToWaitlist(girl);
                pairs.remove(i);
                return true;
            }
            if (pairs.get(i).getGirl().isUser(user)) {
                if (pairs.get(i).getBoy().user == null) {
                    pairs.remove(i);
                    return true;
                }
                Dancer boy = pairs.get(i).getBoy();
                addBoy(boy.user);
                //addDancerToWaitlist(boy);
                pairs.remove(i);
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
        ///TODO: надо отрефакторить, код повторяется
        if (dancer.sex.equals(Dancer.Sex.GIRL)) {
            if (!boys.isEmpty()) {
                Dancer boy = boys.remove(0);
                pairs.add(new DancePair(dancer, boy));
            } else {
                addDancerToWaitlist(dancer);
            }
        } else {
            if (!girls.isEmpty()) {
                Dancer girl = girls.remove(0);
                pairs.add(new DancePair(girl, dancer));
            } else {
                addDancerToWaitlist(dancer);
            }
        }
    }
    private void addDancerToWaitlist(Dancer dancer) {
        // если есть свободные партнёры в списке одидания
        var waitlist = dancer.sex.equals(Dancer.Sex.GIRL) ? this.girls : this.boys;
        if (waitlist.isEmpty()) {
            var otherList = dancer.sex.equals(Dancer.Sex.GIRL) ? this.boys : this.girls;
            if (!otherList.isEmpty()) {
                if (dancer.sex.equals(Dancer.Sex.GIRL))
                    addPair(otherList.get(0), dancer);
                else
                    addPair(dancer, otherList.get(0));
                return;
            }
        }
        // добавить в список ожидания
        for (int i = 0; i < waitlist.size(); ++i)
        {
            if (dancer.getNumber() < waitlist.get(i).getNumber()) {
                waitlist.add(i, dancer);
                return;
            }
        }
        waitlist.add(dancer);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(message == null ? "" : message).append("\n\n");
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
