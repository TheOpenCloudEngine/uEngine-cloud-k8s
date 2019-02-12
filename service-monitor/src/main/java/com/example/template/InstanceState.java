package com.example.template;

public enum InstanceState {
    CREATE("CREATE"),
    MODIFY("MODIFY"),
    DELETE("DELETE"),
    RUNNING("RUNNING");

    private InstanceState(String state){
        this.state = state;
    }

    private final String state;
    public String getState() {
        return state;
    }
}
