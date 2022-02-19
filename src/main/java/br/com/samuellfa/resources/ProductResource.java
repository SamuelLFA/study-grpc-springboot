package br.com.samuellfa.resources;

import br.com.samuellfa.*;
import br.com.samuellfa.dto.ProductInputDTO;
import br.com.samuellfa.dto.ProductOutputDTO;
import br.com.samuellfa.service.IProductService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class ProductResource extends ProductServiceGrpc.ProductServiceImplBase {

    private final IProductService productService;

    public ProductResource(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public void create(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        var productInputDTO = new ProductInputDTO(
                request.getName(),
                request.getPrice(),
                request.getQuantityInStock()
        );

        var productOutputDTO = productService.create(productInputDTO);
        var productResponse = ProductResponse.newBuilder()
                .setId(productOutputDTO.getId())
                .setName(productOutputDTO.getName())
                .setPrice(productOutputDTO.getPrice())
                .setQuantityInStock(productOutputDTO.getQuantityInStock())
                .build();

        responseObserver.onNext(productResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(RequestByIdRequest request, StreamObserver<ProductResponse> responseObserver) {
        ProductOutputDTO productOutputDTO = productService.findById(request.getId());
        var productResponse = ProductResponse.newBuilder()
                .setId(productOutputDTO.getId())
                .setName(productOutputDTO.getName())
                .setPrice(productOutputDTO.getPrice())
                .setQuantityInStock(productOutputDTO.getQuantityInStock())
                .build();

        responseObserver.onNext(productResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(RequestByIdRequest request, StreamObserver<EmptyResponse> responseObserver) {
        productService.delete(request.getId());
        responseObserver.onNext(EmptyResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void findAll(EmptyRequest request, StreamObserver<ProductResponseList> responseObserver) {
        List<ProductOutputDTO> outputDTOs = productService.findAll();
        List<ProductResponse> productResponseList = outputDTOs.stream()
                .map(productOutputDTO ->
                        ProductResponse.newBuilder()
                                .setId(productOutputDTO.getId())
                                .setName(productOutputDTO.getName())
                                .setPrice(productOutputDTO.getPrice())
                                .setQuantityInStock(productOutputDTO.getQuantityInStock())
                                .build())
                .collect(Collectors.toList());

        ProductResponseList responseList = ProductResponseList.newBuilder()
                .addAllProducts(productResponseList)
                .build();

        responseObserver.onNext(responseList);
        responseObserver.onCompleted();
    }
}
