package br.com.samuellfa.resources;

import br.com.samuellfa.ProductRequest;
import br.com.samuellfa.ProductResponse;
import br.com.samuellfa.ProductServiceGrpc;
import br.com.samuellfa.RequestByIdRequest;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext
class ProductResourceTest {

    @GrpcClient("inProcess")
    private ProductServiceGrpc.ProductServiceBlockingStub serviceBlockingStub;

    @Test
    @DisplayName("when valid data is provided a product is created")
    public void createProductSuccessTest() {
        var productRequest = ProductRequest.newBuilder()
                .setName("product name")
                .setPrice(5.5)
                .setQuantityInStock(2)
                .build();
        ProductResponse productResponse = serviceBlockingStub.create(productRequest);
        Assertions.assertThat(productRequest)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "price", "quantityInStock")
                .isEqualTo(productResponse);
    }

    @Test
    @DisplayName("when duplicated name is provided throw an AlreadyExistException")
    public void createProductAlreadyExistTest() {
        var productRequest = ProductRequest.newBuilder()
                .setName("Product A")
                .setPrice(5.5)
                .setQuantityInStock(2)
                .build();
        Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> serviceBlockingStub.create(productRequest))
                .withMessage("ALREADY_EXISTS: Product A already exist.");
    }

    @Test
    @DisplayName("when product exist a product is returned")
    public void findByIdSuccessTest() {
        var request = RequestByIdRequest.newBuilder()
                .setId(1L)
                .build();
        ProductResponse productResponse = serviceBlockingStub.findById(request);
        Assertions.assertThat(productResponse.getId())
                .isEqualTo(request.getId());
        Assertions.assertThat(productResponse.getName())
                .isEqualTo("Product A");
        Assertions.assertThat(productResponse.getPrice())
                .isEqualTo(10.99);
        Assertions.assertThat(productResponse.getQuantityInStock())
                .isEqualTo(10);
    }

    @Test
    @DisplayName("when product does not exist is thrown NotFoundException")
    public void findByIdExceptionTest() {
        var request = RequestByIdRequest.newBuilder()
                .setId(100L)
                .build();
        Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
                .isThrownBy(() -> serviceBlockingStub.findById(request))
                .withMessage("NOT_FOUND: 100 does not exist.");
    }
}