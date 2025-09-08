package com.anoman.machinehistory.service.problem;

import com.anoman.machinehistory.model.problem.Problem;
import com.anoman.machinehistory.model.problem.ProblemUpdate;

import java.util.List;

public interface ProblemService {

    Boolean create(Problem problem);

    Boolean update(ProblemUpdate problemUpdate);

    List<Problem> findAll();

    List<Problem> findByStatus(String status);

    List<Problem> findByStatusAndDate(String status, Long datefrom, Long dateto);

    List<Problem> findByDate(Long datefrom, Long dateto);

    List<Problem> findByProductName(String productName);

    List<Problem> findbyCodeContain(String keyword);


}
