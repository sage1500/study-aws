package com.example.demo.web.common.cognito;

import java.util.List;

import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

public interface CognitoService {
    List<UserType> listUsers();
    void deleteUser(String username);
    void updateUserAttribute(String username, String attrName, String attrValue);
}
