package com.anoman.machinehistory.service.mold;

import com.anoman.machinehistory.model.mold.Mold;

import java.util.List;

public interface MoldService {

    Integer create (Mold mold);

    List<Mold> findAll();

    Mold findById(Integer id);
}
