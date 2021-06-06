package com.example.demo.web.app.mod1;

import javax.validation.groups.Default;

import com.example.demo.web.domain.entity.Mod1;
import com.example.demo.web.domain.service.Mod1Service;
import com.github.dozermapper.core.Mapper;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.message.ResultMessages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("mod1/create")
@SessionAttributes("mod1Form")
@RequiredArgsConstructor
@Slf4j
public class Mod1CreateController {
    private final Mod1Service mod1Service;
    private final Mapper dozerMapper;

    @ModelAttribute("mod1Form")
    public Mod1Form setUpMod1Form() {
        return new Mod1Form();
    }

    @GetMapping(params = "form")
    public String form(Mod1Form mod1Form) {
        log.debug("[MOD1-CREATE]form: {}", mod1Form);
        return "mod1/mod1CreateForm";
    }

    @PostMapping(params = "redo")
    public String redo(Mod1Form mod1Form) {
        log.debug("[MOD1-CREATE]redo: {}", mod1Form);
        return "mod1/mod1CreateForm";
    }

    @PostMapping(params = "confirm")
    public String confirm(@Validated({ Default.class, Mod1Form.Create.class }) Mod1Form mod1Form,
            BindingResult bindingResult) {
        log.debug("[MOD1-CREATE]confirm: {}", mod1Form);

        if (bindingResult.hasErrors()) {
            return redo(mod1Form);
        }

        return "mod1/mod1CreateConfirm";
    }

    @PostMapping
    public String execute(@Validated({ Default.class, Mod1Form.Create.class }) Mod1Form mod1Form,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("[MOD1-CREATE]create: {}", mod1Form);

        if (bindingResult.hasErrors()) {
            return redo(mod1Form);
        }

        // 業務ロジック呼び出し
        // TODO id や version が載っている場合の考慮
        var mod1 = dozerMapper.map(mod1Form, Mod1.class);
        mod1Service.save(mod1);

        // 画面に反映
        var messages = ResultMessages.success().add("i.w1.m1.0001");
        redirectAttributes.addFlashAttribute(messages);
        return "redirect:/mod1/create?complete";
    }

    @GetMapping(params = "complete")
    public String complete(SessionStatus sessionStatus) {
        log.debug("[MOD1-CREATE]complete");
        sessionStatus.setComplete();
        return "mod1/mod1CreateComplete";
    }

    @GetMapping(params = "cancel")
    public String cancel(SessionStatus sessionStatus) {
        log.debug("[MOD1-CREATE]cancel");
        sessionStatus.setComplete();
        return "redirect:/mod1/list";
    }
}
