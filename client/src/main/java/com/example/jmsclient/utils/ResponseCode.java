package com.example.jmsclient.utils;

public enum ResponseCode {
    OK("Операция прошла успешно!"),
    WARNING("Операция прошла с предупреждениями!"),
    ERROR("Произошла ошибка!");
    private final String description;

    ResponseCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
