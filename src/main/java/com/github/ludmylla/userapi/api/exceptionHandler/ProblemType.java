package com.github.ludmylla.userapi.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
    ENTITY_IN_USE("/entity-in-use", "Entity in use"),
    ERROR_BUSINESS("/error-business", "Error business"),
    BAD_CREDENTIALS("/bad-credentials", "Bad credentials"),
    INVALID_DATA("/invalid-data","Invalid data.");

    private String title;
    private String uri;

    ProblemType(String path, String title){
        this.uri = "https://ludmyllafarias.com" + path;
        this.title = title;
    }
}
