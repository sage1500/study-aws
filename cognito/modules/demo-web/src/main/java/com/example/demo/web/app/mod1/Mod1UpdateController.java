package com.example.demo.web.app.mod1;

import javax.validation.groups.Default;

import com.example.demo.web.domain.entity.Mod1;
import com.example.demo.web.domain.service.Mod1Service;
import com.github.dozermapper.core.Mapper;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.message.ResultMessages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("mod1")
@SessionAttributes("mod1Form")
@RequiredArgsConstructor
@Slf4j
public class Mod1UpdateController {
    private final Mod1Service mod1Service;
    private final Mapper dozerMapper;

    @ModelAttribute("mod1Form")
    public Mod1Form setupTodoForm() {
        return new Mod1Form();
    }

    @GetMapping(path = "{id}/update", params = "form")
    public String form(@PathVariable("id") long id, Mod1Form mod1Form) {
        log.debug("[MOD1-UPDATE]form: {}", mod1Form);

        var mod1 = mod1Service.findOne(id);
        dozerMapper.map(mod1, mod1Form);

        return "mod1/mod1UpdateForm";
    }

    @PostMapping(path = "{id}/update", params = "redo")
    public String redo(Mod1Form mod1Form) {
        log.debug("[MOD1-UPDATE]redo: {}", mod1Form);
        return "mod1/mod1UpdateForm";
    }

    @PostMapping(path = "{id}/update", params = "confirm")
    public String confirm(@Validated({ Default.class, Mod1Form.Update.class }) Mod1Form mod1Form,
            BindingResult bindingResult) {
        log.debug("[MOD1-UPDATE]confirm: {}", mod1Form);

        if (bindingResult.hasErrors()) {
            return redo(mod1Form);
        }

        return "mod1/mod1UpdateConfirm";
    }

    @PostMapping(path = "{id}/update")
    public String execute(@Validated({ Default.class, Mod1Form.Update.class }) Mod1Form mod1Form,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("[MOD1-UPDATE]execute: {}", mod1Form);

        if (bindingResult.hasErrors()) {
            return redo(mod1Form);
        }

        try {
            // 業務ロジック呼び出し
            var mod1 = dozerMapper.map(mod1Form, Mod1.class);
            mod1Service.save(mod1);

            // 画面に反映
            var messages = ResultMessages.success().add("i.w1.m1.0101");
            redirectAttributes.addFlashAttribute(messages);
        } catch (OptimisticLockingFailureException e) {
            // 更新競合発生

            // 画面に反映
            var messages = ResultMessages.warning().add("w.w1.m1.0102");
            redirectAttributes.addFlashAttribute(messages);
        }
        return "redirect:/mod1/{id}/update?complete";
    }

    @PostMapping(path = "{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean executeForApi(@Validated({ Default.class, Mod1Form.Update.class }) Mod1Form mod1Form,
            BindingResult bindingResult) {
        log.debug("[MOD1-UPDATE]executeForApi: {}", mod1Form);

        if (bindingResult.hasErrors()) {
            log.warn("Binding Error: {}", bindingResult);
            throw new RuntimeException("Binding Error");
        }

        // 業務ロジック呼び出し
        var mod1 = dozerMapper.map(mod1Form, Mod1.class);
        mod1Service.save(mod1);
        return true;
    }

    @GetMapping(path = "{id}/update", params = "complete")
    public String complete(SessionStatus sessionStatus) {
        log.debug("[MOD1-UPDATE]complete");
        sessionStatus.setComplete();
        return "mod1/mod1UpdateComplete";
    }

    @GetMapping(path = "{id}/update", params = "cancel")
    public String cancel(SessionStatus sessionStatus) {
        log.debug("[MOD1-UPDATE]cancel");
        sessionStatus.setComplete();
        return "redirect:/mod1/{id}";
    }
}
