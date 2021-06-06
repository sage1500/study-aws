package com.example.demo.web.common.web;

import org.springframework.data.domain.Page;
import org.thymeleaf.util.NumberUtils;

public class PageInfo {
    public Integer[] sequence(Page<?> page, int pageLinkMaxDispNum) {
        int begin = Math.max(1, page.getNumber() + 1 - pageLinkMaxDispNum / 2);
        int end = begin + (pageLinkMaxDispNum - 1);
        if (end > page.getTotalPages() - 1) {
            end = page.getTotalPages();
            begin = Math.max(1, end - (pageLinkMaxDispNum - 1));
        }

        return NumberUtils.sequence(begin, end);
    }
}
