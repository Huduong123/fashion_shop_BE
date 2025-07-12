package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.product.ProductVariantSizeCreateDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantSizeResponseDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantSizeUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.ProductVariant;
import e_commerce.monolithic.entity.ProductVariantSize;
import e_commerce.monolithic.entity.Size;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.ProductVariantSizeMapper;
import e_commerce.monolithic.repository.ProductVariantRepository;
import e_commerce.monolithic.repository.ProductVariantSizeRepository;
import e_commerce.monolithic.repository.SizeRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantSizeServiceImpl implements ProductVariantSizeService {

    private final ProductVariantSizeRepository productVariantSizeRepository;
    private final ProductVariantRepository productVariantRepository;
    private final SizeRepository sizeRepository;
    private final ProductVariantSizeMapper productVariantSizeMapper;

    public ProductVariantSizeServiceImpl(
            ProductVariantSizeRepository productVariantSizeRepository,
            ProductVariantRepository productVariantRepository,
            SizeRepository sizeRepository,
            ProductVariantSizeMapper productVariantSizeMapper) {
        this.productVariantSizeRepository = productVariantSizeRepository;
        this.productVariantRepository = productVariantRepository;
        this.sizeRepository = sizeRepository;
        this.productVariantSizeMapper = productVariantSizeMapper;
    }

    @Override
    @Transactional
    public List<ProductVariantSizeResponseDTO> getSizesByProductVariantId(Long productVariantId) {
        List<ProductVariantSize> productVariantSizes = productVariantSizeRepository
                .findByProductVariantId(productVariantId);
        return productVariantSizeMapper.convertToDTO(productVariantSizes);
    }

    @Override
    @Transactional
    public ProductVariantSizeResponseDTO createSize(Long productVariantId, ProductVariantSizeCreateDTO createDTO) {
        // Validate product variant exists
        ProductVariant productVariant = findProductVariantById(productVariantId);
        
        // Validate size exists
        Size size = findSizeById(createDTO.getSizeId());
        
        // Check if size already exists for this product variant
        if (existsByProductVariantIdAndSizeId(productVariantId, createDTO.getSizeId())) {
            throw new IllegalArgumentException("Size '" + size.getName() + "' already exists for this product variant");
        }

        // Create new ProductVariantSize
        ProductVariantSize newProductVariantSize = productVariantSizeMapper
                .convertCreateDtoToEntity(createDTO, productVariant, size);
        
        ProductVariantSize savedProductVariantSize = productVariantSizeRepository.save(newProductVariantSize);
        
        return productVariantSizeMapper.convertToDTO(savedProductVariantSize);
    }

    @Override
    @Transactional
    public ProductVariantSizeResponseDTO updateSize(Long id, ProductVariantSizeUpdateDTO updateDTO) {
        // Find existing ProductVariantSize
        ProductVariantSize existingProductVariantSize = findProductVariantSizeById(id);
        
        // Validate size exists
        Size size = findSizeById(updateDTO.getSizeId());
        
        // Check if size already exists for this product variant (excluding current record)
        if (!existingProductVariantSize.getSize().getId().equals(updateDTO.getSizeId()) &&
            existsByProductVariantIdAndSizeId(existingProductVariantSize.getProductVariant().getId(), 
                                            updateDTO.getSizeId())) {
            throw new IllegalArgumentException("Size '" + size.getName() + "' already exists for this product variant");
        }

        // Update the existing entity
        productVariantSizeMapper.convertUpdateDtoToEntity(updateDTO, existingProductVariantSize, size);
        
        ProductVariantSize updatedProductVariantSize = productVariantSizeRepository.save(existingProductVariantSize);
        
        return productVariantSizeMapper.convertToDTO(updatedProductVariantSize);
    }

    @Override
    @Transactional
    public ResponseMessageDTO deleteSize(Long id) {
        ProductVariantSize productVariantSize = findProductVariantSizeById(id);
        
        productVariantSizeRepository.delete(productVariantSize);
        
        return new ResponseMessageDTO(HttpStatus.OK, "Product variant size deleted successfully");
    }

    @Override
    @Transactional
    public ProductVariantSizeResponseDTO getSizeById(Long id) {
        ProductVariantSize productVariantSize = findProductVariantSizeById(id);
        return productVariantSizeMapper.convertToDTO(productVariantSize);
    }

    @Override
    public boolean existsByProductVariantIdAndSizeId(Long productVariantId, Long sizeId) {
        return productVariantSizeRepository.existsByProductVariantIdAndSizeId(productVariantId, sizeId);
    }

    // Helper methods
    private ProductVariant findProductVariantById(Long productVariantId) {
        return productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new NotFoundException("Product variant not found with ID: " + productVariantId));
    }

    private Size findSizeById(Long sizeId) {
        return sizeRepository.findById(sizeId)
                .orElseThrow(() -> new NotFoundException("Size not found with ID: " + sizeId));
    }

    private ProductVariantSize findProductVariantSizeById(Long id) {
        return productVariantSizeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product variant size not found with ID: " + id));
    }
} 