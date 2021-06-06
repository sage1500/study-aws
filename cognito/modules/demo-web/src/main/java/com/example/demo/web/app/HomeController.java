package com.example.demo.web.app;

import com.example.demo.web.common.cognito.CognitoService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("home")
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final CognitoService cognitoService;


    @GetMapping
    public String home(Model model) {
        log.debug("[HOME]index");
        return "home";
    }

    @GetMapping("test")
    public String test(@AuthenticationPrincipal OidcUser user) {
        log.debug("[HOME]test: {}", user);

        var cognitoUsers = cognitoService.listUsers();
        for (var cognitoUser : cognitoUsers) {
            log.info("user : {}", user);
            log.info("username={} status={}", cognitoUser.username(), cognitoUser.userStatus());
            for (var attr : cognitoUser.attributes()) {
                log.info("  {}: {}", attr.name(), attr.value());
            }
        }

        return "home";
    }

}
