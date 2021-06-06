package com.example.demo.web.common.cognito;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDeleteUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminUpdateUserAttributesRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

@Component
@Slf4j
public class CognitoServiceImpl implements CognitoService {
    private CognitoIdentityProviderClient cognito;

    @Value("${services.cognito.user-pool-id}")
    private String userPoolId;

    @PostConstruct
    public void init() {
        log.info("init");
        cognito = CognitoIdentityProviderClient.builder().build();
    }

    @Override
    public List<UserType> listUsers() {
        // @formatter:off
        var users = cognito.listUsers(ListUsersRequest.builder()
            .userPoolId(userPoolId)
            .build());
        // @formatter:on
        return users.users();
    }

    @Override
    public void deleteUser(String username) {
        // @formatter:off
        cognito.adminDeleteUser(
            AdminDeleteUserRequest.builder()
                .userPoolId(userPoolId)
                .username(username)
                .build());
        // @formatter:on
    }

    @Override
    public void updateUserAttribute(String username, String attrName, String attrValue) {
        // @formatter:off
        cognito.adminUpdateUserAttributes(
            AdminUpdateUserAttributesRequest.builder()
                .userAttributes(AttributeType.builder()
                    .name(attrName)
                    .value(attrValue)
                    .build())
                .userPoolId(userPoolId)
                .username(username)
                .build());
        // @formatter:on
    }

}
