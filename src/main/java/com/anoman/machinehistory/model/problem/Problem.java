package com.anoman.machinehistory.model.problem;

import com.anoman.machinehistory.model.mold.ProductMold;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {

    Integer id;

    String codeProblem;

    ProductMold productMold;

    String part;

    String problem;

    Long problemDate;

    String status;

    String descriptionProblem;

}
