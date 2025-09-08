package com.anoman.machinehistory.service.repairmold;

import com.anoman.machinehistory.model.repair.RepairMold;

import java.util.List;

public interface RepairMoldService {

    Boolean save(RepairMold repairMold);

    Boolean update(RepairMold repairMold);

    List<RepairMold> findByDate(Long from, Long to);

    List<RepairMold> findByProductNameContaint(String keyword);

    List<RepairMold> findbyTeamContaint(String keyword);

}
