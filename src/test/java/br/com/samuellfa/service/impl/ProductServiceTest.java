package br.com.samuellfa.service.impl;

import br.com.samuellfa.domain.Product;
import br.com.samuellfa.dto.ProductInputDTO;
import br.com.samuellfa.exception.AlreadyExistException;
import br.com.samuellfa.exception.NotFoundException;
import br.com.samuellfa.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.tuple;

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

    @Test
    @DisplayName("when call findById with valid id a product is returned")
    void findByIdProductSuccessTest() {
        var product = new Product(1L, "product name", 10.00, 10);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        var outputDTO = productService.findById(1L);

        Assertions.assertThat(outputDTO)
                .usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    @DisplayName("when call findById with invalid id throws NotFoundException")
    void findByIdProductNotFoundTest() {
        Long id = 1L;

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> productService.findById(id));
    }

    @Test
    @DisplayName("when call delete with valid id, the product is deleted")
    void deleteProductSuccessTest() {
        Long id = 1L;
        var product = new Product(id, "product name", 10.00, 10);

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Assertions.assertThatNoException()
                .isThrownBy(() -> productService.delete(id));
    }

    @Test
    @DisplayName("when call delete with invalid id, throws NotFoundException")
    void deleteProductExceptionTest() {
        Long id = 1L;

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> productService.delete(id));
    }

    @Test
    @DisplayName("when call findAll return a list of products")
    void findAllProductSuccessTest() {
        var list = List.of(
                new Product(1L, "product name", 10.00, 10),
                new Product(2L, "other product name", 20.00, 20));

        Mockito.when(productRepository.findAll()).thenReturn(list);
        var productOutputDTOList = productService.findAll();

        Assertions.assertThat(productOutputDTOList)
                .extracting("id", "name", "price", "quantityInStock")
                .contains(
                        tuple(1L, "product name", 10.00, 10),
                        tuple(2L, "other product name", 20.00, 20)
                );
    }
}