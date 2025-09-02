package com.anoman.machinehistory.model.produk;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRegister {

    String name;

    String codeProduk;

    String description;

}
