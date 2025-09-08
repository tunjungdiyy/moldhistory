package com.anoman.machinehistory.service.productmold;

import com.anoman.machinehistory.model.mold.ProductMold;

import java.util.List;

public interface ProductMoldService {

    Boolean create (ProductMold productMold);

    List<ProductMold> findbyProductName(String productName);

    List<ProductMold> findbyCodeMold(String codeMold);

    List<ProductMold> findAll();

    List<ProductMold> findByKeyword(String keyword);

    ProductMold findById(String productMoldId);
}
