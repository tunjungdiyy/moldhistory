package com.anoman.machinehistory.repository.productmold;

import com.anoman.machinehistory.model.mold.ProductMold;
import com.anoman.machinehistory.model.mold.ProductMoldReview;

import java.util.List;

public interface ProductMoldRepository {

    void create (ProductMold productMold);

    List<ProductMold> findByProduct(String productName);

    List<ProductMold> findByCodeMold(String codeMold);

    List<ProductMold> findByCodeProduct(String codeProduct);

    List<ProductMold> findALl();

    ProductMold findByCode(String codeKeyword);

    List<ProductMold> findByProductNameContaint(String keyword);

    ProductMold findById(String idFind);

    List<ProductMoldReview> findReviewMold();
}
