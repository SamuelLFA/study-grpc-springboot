package br.com.samuellfa.util;

import br.com.samuellfa.domain.Product;
import br.com.samuellfa.dto.ProductInputDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductMapperTest {

    @Test
    public void productToOutputDTOTest() {
        var product = new Product(
                1L,
                "product name",
                12.1,
                5
        );
        var productOutputDTO = ProductMapper.entityToOutputDTO(product);

        Assertions.assertThat(product)
                .usingRecursiveComparison()
                .isEqualTo(productOutputDTO);
    }

    @Test
    public void InputProductDTOToEntity() {
        var inputDTO = new ProductInputDTO(
                "product name",
                12.1,
                5
        );
        var entity = ProductMapper.inputDTOtoEntity(inputDTO);

        Assertions.assertThat(inputDTO)
                .usingRecursiveComparison()
                .isEqualTo(entity);
    }

}