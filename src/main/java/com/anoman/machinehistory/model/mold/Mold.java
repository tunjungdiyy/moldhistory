package com.anoman.machinehistory.model.mold;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Mold {

    Integer id;

    String serialNumber;

    String codeMold;

    Double thickness;

    Double vertical;

    Double horizontal;

    Double tonase;

}
