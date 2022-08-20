package com.github.ludmylla.userapi.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found");

    private String title;
    private String uri;

    ProblemType(String path, String title){
        this.uri = "https://ludmyllafarias.com" + path;
        this.title = title;
    }
}
