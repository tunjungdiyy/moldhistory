package com.anoman.machinehistory.model.squence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SquenceRead {

    Integer id;

    String prefix;

    Integer lastNumber;

    Long periode;
}
