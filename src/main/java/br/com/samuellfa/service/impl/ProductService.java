package br.com.samuellfa.service.impl;

import br.com.samuellfa.domain.Product;
import br.com.samuellfa.dto.ProductInputDTO;
import br.com.samuellfa.dto.ProductOutputDTO;
import br.com.samuellfa.exception.AlreadyExistException;
import br.com.samuellfa.exception.NotFoundException;
import br.com.samuellfa.repository.ProductRepository;
import br.com.samuellfa.service.IProductService;
import br.com.samuellfa.util.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductOutputDTO create(ProductInputDTO inputDTO) {
        checkDuplicity(inputDTO.getName());
        Product product = ProductMapper.inputDTOtoEntity(inputDTO);
        var productSaved = this.productRepository.save(product);

        return ProductMapper.entityToOutputDTO(productSaved);
    }

    @Override
    public ProductOutputDTO findById(Long id) {
        var product = this.productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        return ProductMapper.entityToOutputDTO(product);
    }

    @Override
    public void delete(Long id) {
        var product = this.productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        this.productRepository.delete(product);
    }

    @Override
    public List<ProductOutputDTO> findAll() {
        return null;
    }

    private void checkDuplicity(String name) {
        this.productRepository.findByNameIgnoreCase(name)
                .ifPresent(e -> {
                    throw new AlreadyExistException(name);
                });
    }
}
