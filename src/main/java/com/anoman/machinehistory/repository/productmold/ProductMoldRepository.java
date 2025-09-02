package com.anoman.machinehistory.repository.productmold;

import com.anoman.machinehistory.model.mold.ProductMold;

import java.util.List;

public interface ProductMoldRepository {

    void create (ProductMold productMold);

    List<ProductMold> findByProduct(String productName);

    List<ProductMold> findByCodeMold(String codeMold);

    List<ProductMold> findByCodeProduct(String codeProduct);

    List<ProductMold> findALl();
}
