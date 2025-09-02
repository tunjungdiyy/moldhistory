package com.anoman.machinehistory.utility;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class ConvertionMilistoDate {

    public static LocalDate milistoLocalDate(Long milis) {

        return Instant.ofEpochMilli(milis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Long LocalDateToMilis(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

}
