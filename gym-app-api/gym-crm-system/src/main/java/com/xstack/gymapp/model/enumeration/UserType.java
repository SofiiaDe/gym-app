package com.xstack.gymapp.model.enumeration;

public enum UserType {
    TRAINEE("trainee"), TRAINER("trainer");

    private final String name;

    UserType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
