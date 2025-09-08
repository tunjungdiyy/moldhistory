package com.anoman.machinehistory.service.repairmold;

import com.anoman.machinehistory.model.problem.Problem;
import com.anoman.machinehistory.model.problem.ProblemUpdate;
import com.anoman.machinehistory.model.repair.RepairMold;
import com.anoman.machinehistory.repository.problem.ProblemRepository;
import com.anoman.machinehistory.repository.problem.ProblemRepositoryImpl;
import com.anoman.machinehistory.repository.repair.RepairMoldRepository;
import com.anoman.machinehistory.repository.repair.RepairMoldRepositoryimpl;
import com.anoman.machinehistory.service.squence.SquenceService;
import com.anoman.machinehistory.service.squence.SquenceServiceImpl;

import java.util.List;

public class RepairMoldServiceImpl implements RepairMoldService{

    RepairMoldRepository repairMoldRepository = new RepairMoldRepositoryimpl();
    ProblemRepository problemRepository = new ProblemRepositoryImpl();
    SquenceService squenceService = new SquenceServiceImpl();

    @Override
    public Boolean save(RepairMold repairMold) {

        String code = squenceService.generatorid("repair_mold", "RM", repairMold.getReapirEnd(), "");

        repairMold.setCodeRepair(code);

        repairMoldRepository.create(repairMold);

        Problem problem = problemRepository.findById(repairMold.getProblem().getId());

        ProblemUpdate update = new ProblemUpdate();
        update.setId(problem.getId());
        update.setProductMold(problem.getProductMold());
        update.setStatus("SELESAI");
        update.setPart(problem.getPart());
        update.setProblem(problem.getProblem());
        update.setDescription(problem.getDescriptionProblem());
        update.setProblemDate(problem.getProblemDate());

        problemRepository.update(update);

        return true;
    }

    @Override
    public Boolean update(RepairMold repairMold) {
        repairMoldRepository.update(repairMold);

        return true;
    }

    @Override
    public List<RepairMold> findByDate(Long from, Long to) {
        return repairMoldRepository.findByDateEndRepair(from, to);
    }

    @Override
    public List<RepairMold> findByProductNameContaint(String keyword) {
        return repairMoldRepository.findByProductName(keyword);
    }

    @Override
    public List<RepairMold> findbyTeamContaint(String keyword) {
        return repairMoldRepository.findByTeamRepairContaint(keyword);
    }
}
