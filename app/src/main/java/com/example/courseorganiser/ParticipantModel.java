package com.example.courseorganiser;

public class ParticipantModel {
    private String name;
    private boolean payed;

    public ParticipantModel(String name, boolean payed) {
        this.name = name;
        this.payed = payed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }
}
