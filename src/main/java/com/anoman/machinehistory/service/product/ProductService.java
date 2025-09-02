package com.anoman.machinehistory.service.product;

import com.anoman.machinehistory.model.produk.ProductUpdateAndRead;

import java.util.List;

public interface ProductService {

    Boolean register(String name, String desc);

    Boolean update(Integer id, String name , String desc);

    List<ProductUpdateAndRead> findAll();

    List<ProductUpdateAndRead> findByContaint(String keyword);

    ProductUpdateAndRead findByName(String name);

}
