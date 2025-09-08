package com.anoman.machinehistory.model.repair;

import com.anoman.machinehistory.model.problem.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairMold {

    Integer id;

    Problem problem;

    String codeRepair;

    String action;

    Long repairStart;

    Long reapirEnd;

    String teamRepair;

    String noteRepair;
}
