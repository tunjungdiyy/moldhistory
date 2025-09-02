package com.anoman.machinehistory.service.product;

import com.anoman.machinehistory.model.produk.ProductRegister;
import com.anoman.machinehistory.model.produk.ProductUpdateAndRead;
import com.anoman.machinehistory.repository.product.ProductRepository;
import com.anoman.machinehistory.repository.product.ProductRepositoryImpl;
import com.anoman.machinehistory.service.squence.SquenceService;
import com.anoman.machinehistory.service.squence.SquenceServiceImpl;

import java.util.List;

public class ProductServiceImpl implements ProductService{

    ProductRepository productRepository = new ProductRepositoryImpl();
    SquenceService squenceService = new SquenceServiceImpl();


    @Override
    public Boolean register(String name, String desc) {

        if (productRepository.findbyName(name).getId() == null) {

            String id = squenceService.generatorid("product", "P", 0L, "for product");

            if (!id.isEmpty()) {

                ProductRegister register = new ProductRegister();

                register.setName(name);
                register.setCodeProduk(id);
                register.setDescription(desc);

                productRepository.create(register);

                return  true;

            } else {
                return false;
            }


        } else {
            return false;
        }

    }

    @Override
    public Boolean update(Integer id, String name, String desc) {

        ProductUpdateAndRead productUpdateAndRead = productRepository.findById(id);

        if (productUpdateAndRead.getId() == null) {
            return  false;
        } else {

            ProductUpdateAndRead update = new ProductUpdateAndRead();
            update.setId(productUpdateAndRead.getId());
            update.setName(name);
            update.setDescription(desc);
            update.setCodeProduct(productUpdateAndRead.getCodeProduct());

            productRepository.update(update);

            return true;

        }

    }

    @Override
    public List<ProductUpdateAndRead> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductUpdateAndRead> findByContaint(String keyword) {
        return productRepository.findByContain(keyword);
    }

    @Override
    public ProductUpdateAndRead findByName(String name) {
        return productRepository.findbyName(name);
    }
}
