package ru.tayrinn.telegram.coopdance.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Dances {
    List<Dance> danceList = new ArrayList<>();

    public Dance getDanceByMessageId(String messageId) {
        AtomicReference<Dance> result = new AtomicReference<>();
        danceList.forEach(dance -> {
            if (dance.messageId.equals(messageId)) {
                result.set(dance);
            }
        });
        if (result.get() == null) {
            Dance dance = new Dance(messageId);
            danceList.add(dance);
            return dance;
        } else {
            return result.get();
        }
    }
//
//    public Dance addGirl(String messageId, User user) {
//        Dance dance = getDanceByMessageId(messageId);
//        dance.addGirl(user);
//        return dance;
//    }
//
//    public Dance addBoy(String messageId, User user) {
//        Dance dance = getDanceByMessageId(messageId);
//        dance.addBoy(user);
//        return dance;
//    }
}
