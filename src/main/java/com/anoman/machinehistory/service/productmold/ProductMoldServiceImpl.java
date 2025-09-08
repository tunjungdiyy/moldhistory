package com.anoman.machinehistory.service.productmold;

import com.anoman.machinehistory.model.mold.ProductMold;
import com.anoman.machinehistory.repository.productmold.ProductMoldRepository;
import com.anoman.machinehistory.repository.productmold.ProductMoldRepositoryImpl;
import com.anoman.machinehistory.service.mold.MoldService;
import com.anoman.machinehistory.service.mold.MoldServiceImpl;
import com.anoman.machinehistory.service.squence.SquenceService;
import com.anoman.machinehistory.service.squence.SquenceServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ProductMoldServiceImpl implements ProductMoldService{

    ProductMoldRepository productMoldRepository = new ProductMoldRepositoryImpl();
    SquenceService squenceService = new SquenceServiceImpl();
    MoldService moldService = new MoldServiceImpl();

    @Override
    public Boolean create(ProductMold productMold) {

        Integer valueMold = moldService.create(productMold.getMold());
        log.info(String.valueOf(valueMold));

        if (valueMold > 0) {

            String id = squenceService.generatorid("product_mold", "PMOLD", 0L, "");

            productMold.setCode(id);
            productMold.setMold(moldService.findById(valueMold));

            productMoldRepository.create(productMold);

            log.info("success register product mold", getClass());

            return true;

        } else {
            return false;
        }


    }

    @Override
    public List<ProductMold> findbyProductName(String productName) {
        return productMoldRepository.findByProduct(productName);
    }

    @Override
    public List<ProductMold> findbyCodeMold(String codeMold) {
        return productMoldRepository.findByCodeMold(codeMold);
    }

    @Override
    public List<ProductMold> findAll() {
        return productMoldRepository.findALl();
    }

    @Override
    public List<ProductMold> findByKeyword(String keyword) {
        return productMoldRepository.findByProductNameContaint(keyword);
    }

    @Override
    public ProductMold findById(String productMoldId) {
        return productMoldRepository.findById(productMoldId);
    }
}
