package com.example.demo.web.app.mod1;

import com.example.demo.web.domain.service.Mod1Service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("mod1")
@RequiredArgsConstructor
@Slf4j
public class Mod1ListController {
    private final Mod1Service mod1Service;

    @GetMapping("list")
    public String list(Model model, @PageableDefault(size = 10) Pageable pageable) {
        log.debug("[MOD1]list: pageable={}", pageable);

        var page = mod1Service.findPage(pageable);

        model.addAttribute("page", page);
        return "mod1/mod1List";
    }

}
