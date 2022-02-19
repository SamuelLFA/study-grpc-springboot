package br.com.samuellfa.service;

import br.com.samuellfa.dto.ProductInputDTO;
import br.com.samuellfa.dto.ProductOutputDTO;

import java.util.List;

public interface IProductService {

    ProductOutputDTO create(ProductInputDTO inputDTO);
    ProductOutputDTO findById(Long id);
    void delete(Long id);
    List<ProductOutputDTO> findAll();

}
