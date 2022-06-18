package ru.tayrinn.telegram.coopdance.models;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.tayrinn.telegram.coopdance.models.Dance;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.xpath.XPathEvaluationResult;
import java.util.ArrayList;

class DanceTest {
    ArrayList<User> boys;
    ArrayList<User> girls;

    public DanceTest() {
        girls = new ArrayList<User>(5);
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUserName("TestGirl"+String.valueOf(i));
            user.setId(i + 100);
            user.setIsBot(false);
            user.setFirstName("Test"+String.valueOf(i));
            user.setLastName("Girl");
            girls.add(user);
        }
        boys = new ArrayList<User>(5);
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUserName("TestBoy"+String.valueOf(i));
            user.setId(i + 200);
            user.setIsBot(false);
            user.setFirstName("Test"+String.valueOf(i));
            user.setLastName("Boy");
            boys.add(user);
        }
    }
    boolean findDancerByUserName(Dance d) {
        var test1 = d.findDancerByUserName("TestGirl3");
        return test1 != null && test1.user.getId().equals(103);
    }

    boolean hasDancer(Dance d) {
        User user = girls.get(3);
        return d.hasDancer(user);
    }

}
class Test {

    private static void addPair(Dance dance, User author, User partnerUser) {
        Dancer partner = dance.findDancerByUserName(partnerUser.getUserName());
        if (partner == null) {
            partner = new Dancer();
            partner.user = partnerUser;
            partner.stubName = partnerUser.getUserName();
        }
        dance.findSingleDancerAndRemove(partner.user);

        Dancer authorDancer = new Dancer();
        authorDancer.user = author;

        partner.sex = Dancer.Sex.GIRL;
        authorDancer.sex = Dancer.Sex.BOY;
        dance.addPair(authorDancer, partner);

    }
    public static void cancel(Dance dance, User cancelingUser) {

        System.out.println(cancelingUser.getUserName() +  " - Вы не пойдёте");
        if (!dance.findSingleDancerAndRemove(cancelingUser)) {
            dance.findPairAndRemoveDancer(cancelingUser);
        }

    }

    public static void main(String[] args) {
        DanceTest dt = new DanceTest();
        Dance d = new Dance("Test", "Test");
        for (User girl: dt.girls) {
            d.addGirl(girl);
        }
        for(int i = 3; i < 8; i++) {
            addPair(d, dt.boys.get(i % 5), dt.girls.get(i-3));
        }
        System.out.println(d.toString());
        cancel(d, dt.girls.get(3));
        System.out.println(d.toString());
        cancel(d, dt.boys.get(3));
        System.out.println(d.toString());
        cancel(d, dt.boys.get(2));
        System.out.println(d.toString());
        cancel(d, dt.boys.get(4));
        System.out.println(d.toString());
        System.out.println(dt.findDancerByUserName(d));
        System.out.println(dt.hasDancer(d));
    }
}