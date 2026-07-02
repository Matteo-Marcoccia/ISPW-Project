package com.questtable.bean;

public class LoginBean {
    private final String username;
    private final String password;

    public LoginBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String fornisciUsername() {
        return username;
    }

    public String fornisciPassword() {
        return password;
    }

    public boolean verificaCampiCompilati() {
        return username != null
                && !username.trim().isEmpty()
                && password != null
                && !password.trim().isEmpty();
    }
}
