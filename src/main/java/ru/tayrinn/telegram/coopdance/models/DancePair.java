package ru.tayrinn.telegram.coopdance.models;

public class DancePair {
    private Dancer girl;
    private Dancer boy;

    public Dancer getGirl() {
        return girl;
    }

    public void setGirl(Dancer girl) {
        this.girl = girl;
        this.girl.sex = Dancer.Sex.GIRL;
    }

    public Dancer getBoy() {
        return boy;
    }

    public void setBoy(Dancer boy) {
        this.boy = boy;
        this.boy.sex = Dancer.Sex.BOY;
    }

    @Override
    public String toString() {
        return boy + " и " + girl;
    }
}
