package com.example.demo.web.common.config;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class CognitoOidcClientInitiatedLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Setter
    private String logoutEndpointUri;

    @Setter
    private String postLogoutRedirectUri;

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        String targetUrl = null;
        if (logoutEndpointUri != null) {
            if (authentication instanceof OAuth2AuthenticationToken && authentication.getPrincipal() instanceof OidcUser) {
                String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
                ClientRegistration clientRegistration = this.clientRegistrationRepository
                        .findByRegistrationId(registrationId);
                if (clientRegistration != null) {
                    URI endSessionEndpoint = URI.create(logoutEndpointUri);
                    String clientId = clientRegistration.getClientId();
                    URI logoutUri = postLogoutRedirectUri(request);
                    targetUrl = endpointUri(endSessionEndpoint, clientId, logoutUri);
                }
            }
        }
        return (targetUrl != null) ? targetUrl : super.determineTargetUrl(request, response);
    }

    private URI postLogoutRedirectUri(HttpServletRequest request) {
        if (this.postLogoutRedirectUri == null) {
            return null;
        }
        // @formatter:off
		UriComponents uriComponents = UriComponentsBuilder
				.fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
				.replacePath(request.getContextPath())
				.replaceQuery(null)
				.fragment(null)
				.build();
		return UriComponentsBuilder.fromUriString(this.postLogoutRedirectUri)
				.buildAndExpand(Collections.singletonMap("baseUrl", uriComponents.toUriString()))
				.toUri();
		// @formatter:on
    }

    private String endpointUri(URI endSessionEndpoint, String clientId, URI logoutUri) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(endSessionEndpoint);
        builder.queryParam("client_id", clientId);
        if (logoutUri != null) {
            builder.queryParam("logout_uri", logoutUri);
        }

        // @formatter:off
		return builder.encode(StandardCharsets.UTF_8)
				.build()
				.toUriString();
		// @formatter:on
    }

}
