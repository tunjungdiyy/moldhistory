package com.anoman.machinehistory.repository.repair;

import com.anoman.machinehistory.model.repair.RepairMold;

import java.util.List;

public interface RepairMoldRepository {

    Boolean create(RepairMold repairMold);

    void update(RepairMold repairMold);

    List<RepairMold> findByProductMold(Integer idProductMoldFind);

    List<RepairMold> findByTeamRepairContaint(String keyword);

    List<RepairMold> findByDateEndRepair(Long datefrom, Long dateto);

    List<RepairMold> findByProductName(String nameProductFind);

    List<RepairMold> findByNameProductAndDateRepair(String keywordname, Long start, Long end);
}
