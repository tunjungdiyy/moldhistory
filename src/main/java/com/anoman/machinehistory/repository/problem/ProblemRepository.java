package com.anoman.machinehistory.repository.problem;

import com.anoman.machinehistory.model.problem.Problem;
import com.anoman.machinehistory.model.problem.ProblemUpdate;

import java.util.List;

public interface ProblemRepository {

    void create(Problem problem);

    void update(ProblemUpdate problemUpdate);

    List<Problem> findALl();

    List<Problem> findByStatus(String statusfind);

    List<Problem> findByCodeProductMold(String codeProductMold);

    Problem findbyCodeProblem(String codeProblem);

    Problem findById(Integer idProblem);

    List<Problem> findByDateAndStatus(Long milisfrom, Long milisto, String statusFind);

    List<Problem> findByDate(Long milisfrom, Long milisTo);

    List<Problem> findByProductName(String productNameKeyword);

    List<Problem> findByCodeContain(String keyword);
}
