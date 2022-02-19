package br.com.samuellfa.util;

import br.com.samuellfa.domain.Product;
import br.com.samuellfa.dto.ProductInputDTO;
import br.com.samuellfa.dto.ProductOutputDTO;

public class ProductMapper {

    public static ProductOutputDTO entityToOutputDTO(Product product) {
        return new ProductOutputDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantityInStock()
        );
    }

    public static Product inputDTOtoEntity(ProductInputDTO inputDTO) {
        return new Product(
                null,
                inputDTO.getName(),
                inputDTO.getPrice(),
                inputDTO.getQuantityInStock()
        );
    }
}
