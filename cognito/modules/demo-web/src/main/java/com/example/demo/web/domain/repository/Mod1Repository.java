package com.example.demo.web.domain.repository;

import java.util.List;

import com.example.demo.web.domain.entity.Mod1;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

public interface Mod1Repository {
    /** */
    long count();

    /** */
    Mod1 findOne(long id);

    /** */
    List<Mod1> findAll();

    /** */
    List<Mod1> findPage(@Param("pageable") Pageable pageable);

    /** */
    int insert(Mod1 entity);

    /** */
    int update(Mod1 entity);

    /** */
    int deleteById(long id);
}
