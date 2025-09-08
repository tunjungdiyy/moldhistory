package com.anoman.machinehistory.service.problem;

import com.anoman.machinehistory.model.problem.Problem;
import com.anoman.machinehistory.model.problem.ProblemUpdate;
import com.anoman.machinehistory.repository.problem.ProblemRepository;
import com.anoman.machinehistory.repository.problem.ProblemRepositoryImpl;
import com.anoman.machinehistory.repository.productmold.ProductMoldRepository;
import com.anoman.machinehistory.repository.productmold.ProductMoldRepositoryImpl;
import com.anoman.machinehistory.service.squence.SquenceService;
import com.anoman.machinehistory.service.squence.SquenceServiceImpl;
import com.anoman.machinehistory.utility.ConvertionMilistoDate;

import java.time.LocalDate;
import java.util.List;

public class ProblemServiceImpl implements ProblemService{

    ProblemRepository problemRepository = new ProblemRepositoryImpl();
    SquenceService squenceService = new SquenceServiceImpl();
    ProductMoldRepository productMoldRepository = new ProductMoldRepositoryImpl();

    @Override
    public Boolean create(Problem problem) {
        String id = squenceService.generatorid("problem", "PM", problem.getProblemDate(), "");

        problem.setCodeProblem(id);

        problemRepository.create(problem);

        return true;
    }

    @Override
    public Boolean update(ProblemUpdate problemUpdate) {
        Problem problem = problemRepository.findById(problemUpdate.getId());

        if (problem.getId() == null) {
            return  false;
        } else {
            problemRepository.update(problemUpdate);

            return true;
        }

    }

    @Override
    public List<Problem> findAll() {
        return problemRepository.findALl();
    }

    @Override
    public List<Problem> findByStatus(String status) {
        return problemRepository.findByStatus(status);
    }

    @Override
    public List<Problem> findByStatusAndDate(String status, Long datefrom, Long dateto) {
        return problemRepository.findByDateAndStatus(datefrom, dateto, status);
    }

    @Override
    public List<Problem> findByDate(Long datefrom, Long dateto) {
        return problemRepository.findByDate(datefrom, dateto);
    }

    @Override
    public List<Problem> findByProductName(String productName) {
        return problemRepository.findByProductName(productName);
    }

    @Override
    public List<Problem> findbyCodeContain(String keyword) {
        return problemRepository.findByCodeContain(keyword);
    }
}
