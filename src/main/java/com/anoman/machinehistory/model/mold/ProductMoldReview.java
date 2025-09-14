package com.anoman.machinehistory.model.mold;

import com.anoman.machinehistory.model.produk.ProductUpdateAndRead;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMoldReview {

    Integer id;

    ProductUpdateAndRead product;

    Mold mold;

    String code;

    Integer cvt;

    Double ct;

    Double grammage;

    String description;

    Integer countProblem;
}
