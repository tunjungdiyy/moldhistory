package com.anoman.machinehistory.service.squence;


import com.anoman.machinehistory.model.squence.SquenceRead;
import com.anoman.machinehistory.model.squence.SquenceRegister;
import com.anoman.machinehistory.model.squence.SquenceUpdate;
import com.anoman.machinehistory.repository.squence.SquenceRepository;
import com.anoman.machinehistory.repository.squence.SquenceRepositoryImpl;
import com.anoman.machinehistory.utility.ConvertionMilistoDate;

import java.time.LocalDate;

public class SquenceServiceImpl implements SquenceService{

    SquenceRepository squenceRepository = new SquenceRepositoryImpl();

    @Override
    public String generatorid(String entityName, String prefix, Long periode, String desc) {

        SquenceRead squenceRead = squenceRepository.findByEntityWithPeriode(entityName, periode);

        String id = null;

        if (squenceRead.getId() == null) {

            if (periode == 0L) {
                id = prefix.toUpperCase()+ "-" + String.format("%07d", 1);
            } else {
                id = prefix.toUpperCase() + "-" + ConvertionMilistoDate.milistoLocalDate(periode) + "-" + String.format("%07d", 1);
            }

            squenceRepository.create(SquenceRegister.builder()
                    .entityName(entityName)
                    .prefix(prefix)
                    .lastNumber(1)
                    .periode(periode)
                    .description(desc).build());
        } else {

            if (periode == 0L) {
                id = squenceRead.getPrefix() + "-" + String.format("%07d", squenceRead.getLastNumber() + 1);
            } else {
                id = squenceRead.getPrefix() + "-" + ConvertionMilistoDate.milistoLocalDate(periode) + "-" + String.format("%07d", squenceRead.getLastNumber() + 1);
            }

            squenceRepository.update(SquenceUpdate.builder()
                    .id(squenceRead.getId())
                    .lastNumber(squenceRead.getLastNumber() + 1)
                    .periode(squenceRead.getPeriode())
                    .build());
        }

        return id;
    }
}
