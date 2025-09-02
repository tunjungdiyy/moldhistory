package com.anoman.machinehistory.repository.product;

import com.anoman.machinehistory.model.produk.ProductRegister;
import com.anoman.machinehistory.model.produk.ProductUpdateAndRead;

import java.util.List;

public interface ProductRepository {

    void create (ProductRegister productRegister);

    void update (ProductUpdateAndRead productUpdateAndRead);

    ProductUpdateAndRead findbyName(String name);

    ProductUpdateAndRead findById(Integer id);

    List<ProductUpdateAndRead> findAll();

    List<ProductUpdateAndRead> findByContain(String keyword);

}
