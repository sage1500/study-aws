package com.example.demo.web.domain.service;

import java.util.Collections;
import java.util.List;

import com.example.demo.web.domain.entity.Mod1;
import com.example.demo.web.domain.repository.Mod1Repository;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class Mod1ServiceImpl implements Mod1Service {
    private final Mod1Repository mod1Repository;

    @Override
    public Mod1 findOne(long id) {
        return mod1Repository.findOne(id);
    }

    @Override
    public List<Mod1> findAll() {
        return mod1Repository.findAll();
    }

    @Override
    public Page<Mod1> findPage(Pageable pageable) {
        List<Mod1> mod1s = Collections.emptyList();
        long total = mod1Repository.count();
        if (0 < total) {
            mod1s = mod1Repository.findPage(pageable);
        }
        return new PageImpl<Mod1>(mod1s, pageable, total);
    }

    @Override
    public Mod1 save(Mod1 entity) {
        if (entity.getVersion() == 0) {
            mod1Repository.insert(entity);
            entity.setVersion(1);
        } else {
            int result = mod1Repository.update(entity);
            if (result == 0) {
                // 更新競合
                throw new OptimisticLockingFailureException("conflicted.");
            }
        }
        return entity;
    }

    @Override
    public int delete(long id) {
        return mod1Repository.deleteById(id);
    }

}
