package com.anoman.machinehistory.service.mold;

import com.anoman.machinehistory.model.mold.Mold;
import com.anoman.machinehistory.repository.mold.MoldRepository;
import com.anoman.machinehistory.repository.mold.MoldRepositoryImpl;
import com.anoman.machinehistory.service.squence.SquenceService;
import com.anoman.machinehistory.service.squence.SquenceServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MoldServiceImpl implements MoldService{

    MoldRepository moldRepository = new MoldRepositoryImpl();

    SquenceService squenceService = new SquenceServiceImpl();

    @Override
    public Integer create(Mold mold) {
        String id = squenceService.generatorid("mold", "MOLD", 0L, "");

        mold.setCodeMold(id);

        moldRepository.create(mold);


        log.info("success register mold ", getClass());
        return moldRepository.findByCode(id).getId();

    }

    @Override
    public List<Mold> findAll() {
        return moldRepository.findAll();
    }

    @Override
    public Mold findById(Integer id) {
        return moldRepository.findById(id);
    }
}
