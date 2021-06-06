package com.example.demo.web.app;

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

    @GetMapping
    public String home(Model model) {
        log.debug("[HOME]index");
        return "home";
    }

}
