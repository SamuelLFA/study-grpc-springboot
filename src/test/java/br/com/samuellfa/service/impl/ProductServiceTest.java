package br.com.samuellfa.service.impl;

import br.com.samuellfa.domain.Product;
import br.com.samuellfa.dto.ProductInputDTO;
import br.com.samuellfa.exception.AlreadyExistException;
import br.com.samuellfa.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("when create product service is called with valid data a product is returned")
    void createProductSuccessTest() {
        var product = new Product(1L, "product name", 10.00, 10);

        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        var inputDTO = new ProductInputDTO("product name", 10.00, 10);
        var outputDTO = productService.create(inputDTO);

        Assertions.assertThat(outputDTO)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    @DisplayName("when create product service is called with duplicated name throw AlreadyExistException")
    void createProductExceptionTest() {
        var product = new Product(1L, "product name", 10.00, 10);

        Mockito.when(productRepository.findByNameIgnoreCase(Mockito.any())).thenReturn(Optional.of(product));
        var inputDTO = new ProductInputDTO("product name", 10.00, 10);

        Assertions.assertThatExceptionOfType(AlreadyExistException.class)
                .isThrownBy(() -> productService.create(inputDTO));
    }
}