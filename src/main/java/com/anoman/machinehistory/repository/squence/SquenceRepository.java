package com.anoman.machinehistory.repository.squence;

import com.anoman.machinehistory.model.squence.SquenceRead;
import com.anoman.machinehistory.model.squence.SquenceRegister;
import com.anoman.machinehistory.model.squence.SquenceUpdate;

public interface SquenceRepository {

    void create(SquenceRegister register);

    void update(SquenceUpdate update);

    SquenceRead findbyEntityWithoutPeriode(String nameEntity);

    SquenceRead findByEntityWithPeriode(String nameEntity, Long periode);

}
