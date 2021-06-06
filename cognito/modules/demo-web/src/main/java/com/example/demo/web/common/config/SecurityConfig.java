package com.example.demo.web.common.config;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Value("${services.cognito.logout-endpoint}")
    private String logoutEndpointUrl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 認可設定
        // @formatter:off
        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/manage/**").permitAll()
            .anyRequest().authenticated();        
        // @formatter:on

        // OAuth2 ログイン
        // @formatter:off
        http.oauth2Login(oauth2 -> oauth2
            .authorizationEndpoint(authorization -> authorization
                .authorizationRequestResolver(authorizationRequestResolver(clientRegistrationRepository))));
        // @formatter:on

        // ログアウト
        http.logout(logout -> {
            logout.logoutSuccessHandler(oidcLogoutSuccessHandler());
        });
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        CognitoOidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler = new CognitoOidcClientInitiatedLogoutSuccessHandler(
                this.clientRegistrationRepository);
        logoutSuccessHandler.setLogoutEndpointUri(logoutEndpointUrl);
        logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/");
        return logoutSuccessHandler;
    }

    private OAuth2AuthorizationRequestResolver authorizationRequestResolver(
            ClientRegistrationRepository clientRegistrationRepository) {
        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, "/oauth2/authorization");
        authorizationRequestResolver.setAuthorizationRequestCustomizer(authorizationRequestCustomizer());
        return authorizationRequestResolver;
    }

    private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer() {
        return customizer -> customizer.additionalParameters(params -> {
            var locale = LocaleContextHolder.getLocale();
            log.debug("locale {}", locale.getLanguage());
            params.put("ui_locales", locale.getLanguage());
        });
    }
}
