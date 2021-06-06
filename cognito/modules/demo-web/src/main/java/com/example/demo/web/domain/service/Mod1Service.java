package com.example.demo.web.domain.service;

import java.util.List;

import com.example.demo.web.domain.entity.Mod1;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Mod1Service {
    Mod1 findOne(long id);
    List<Mod1> findAll();
    Page<Mod1> findPage(Pageable pageable);
    Mod1 save(Mod1 entity);
    int delete(long id);

}
