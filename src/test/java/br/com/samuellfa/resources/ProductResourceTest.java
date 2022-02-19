package br.com.samuellfa.resources;

import br.com.samuellfa.ProductRequest;
import br.com.samuellfa.ProductResponse;
import br.com.samuellfa.ProductServiceGrpc;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
}