package com.anoman.machinehistory.model.squence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SquenceUpdate {

    Integer id;

    Integer lastNumber;

    Long periode;

}
