package com.anoman.machinehistory.repository.mold;

import com.anoman.machinehistory.model.mold.Mold;

import java.util.List;

public interface MoldRepository {

    void create(Mold mold);

    void update(Mold mold);

    List<Mold> findAll();

    Mold findById(Integer id);

    Mold findByCode(String code);

}
