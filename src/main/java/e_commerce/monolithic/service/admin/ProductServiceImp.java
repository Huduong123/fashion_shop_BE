package e_commerce.monolithic.service.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import e_commerce.monolithic.dto.admin.product.ProductCreateDTO;
import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;
import e_commerce.monolithic.dto.admin.product.ProductUpdateDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantCreateDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantImageDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantSizeUpdateDTO;
import e_commerce.monolithic.dto.admin.product.ProductVariantUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.BaseEntity;
import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.entity.Color;
import e_commerce.monolithic.entity.Product;
import e_commerce.monolithic.entity.ProductVariant;
import e_commerce.monolithic.entity.ProductVariantImage;
import e_commerce.monolithic.entity.ProductVariantSize;
import e_commerce.monolithic.entity.Size;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.ProductMapper;
import e_commerce.monolithic.mapper.ProductVariantMapper;
import e_commerce.monolithic.mapper.ProductVariantSizeMapper;
import e_commerce.monolithic.repository.CategoryRepository;
import e_commerce.monolithic.repository.ColorRepository;
import e_commerce.monolithic.repository.ProductRepository;
import e_commerce.monolithic.repository.SizeRepository;
import e_commerce.monolithic.specification.ProductSpecification;
import jakarta.transaction.Transactional;

@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final ProductMapper productMapper;
    private final ProductVariantMapper productVariantMapper;
    private final ProductSpecification productSpecification;
    private final ProductVariantSizeMapper productVariantSizeMapper;

    public ProductServiceImp(ProductRepository productRepository, CategoryRepository categoryRepository,
            ColorRepository colorRepository, SizeRepository sizeRepository, ProductMapper productMapper,
            ProductVariantMapper productVariantMapper, ProductSpecification productSpecification,
            ProductVariantSizeMapper productVariantSizeMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.productMapper = productMapper;
        this.productVariantMapper = productVariantMapper;
        this.productSpecification = productSpecification;
        this.productVariantSizeMapper = productVariantSizeMapper;
    }

    @Override
    @Transactional
    public List<ProductResponseDTO> findAll(String name, BigDecimal minPrice, BigDecimal maxPrice,
            Integer minQuantity, Integer maxQuantity,
            Boolean enabled, Long categoryId,
            LocalDate createdAt, LocalDate updatedAt) {
        Specification<Product> spec = productSpecification.findByCriteria(name, minPrice, maxPrice, minQuantity,
                maxQuantity, enabled, categoryId, createdAt, updatedAt);
        return productRepository.findAll(spec)
                .stream()
                .map(productMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductResponseDTO findById(Long productId) {
        Product product = findProductById(productId);
        return productMapper.convertToDTO(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO) {

        // Validation logic nghiệp vụ
        checkProductNameExists(productCreateDTO.getName());

        checkDuplicateVariantsOncreate(productCreateDTO.getProductVariants());

        // tìm categiry
        Category category = findCategoryById(productCreateDTO.getCategoryId());

        // Validation: Chỉ cho phép tạo product cho category có type LINK
        if (!category.canContainProducts()) {
            throw new IllegalArgumentException("Không thể tạo sản phẩm cho danh mục '" + category.getName() +
                    "' vì danh mục này có kiểu '" + category.getType().getDisplayName() + "'. " +
                    "Chỉ có thể tạo sản phẩm cho danh mục có kiểu 'Liên kết'.");
        }

        // chuyển DTO sang entity
        Product newProduct = productMapper.convertCreateDtoToEntity(productCreateDTO, category);

        // xử lí biến tể
        List<ProductVariant> variants = productCreateDTO.getProductVariants()
                .stream()
                .map(this::createVariantFromDTO)
                .collect(Collectors.toList());

        // thiết lập quan hệ hai chiều
        variants.forEach(variant -> variant.setProduct(newProduct));
        newProduct.setProductVariants(variants);

        // lưu vào db
        Product savedProduct = productRepository.save(newProduct);

        return productMapper.convertToDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long productId, ProductUpdateDTO productUpdateDTO) {

        Product existingProduct = findProductById(productId);

        // validation
        checkProductNameExistsForUpdate(productUpdateDTO.getName(), productId);
        checkDuplicateVariantsOnUpdate(productUpdateDTO.getProductVariants());

        // tìm danh mục theo categoryId
        Category category = findCategoryById(productUpdateDTO.getCategoryId());

        // Validation: Chỉ cho phép update product cho category có type LINK
        if (!category.canContainProducts()) {
            throw new IllegalArgumentException("Không thể cập nhật sản phẩm cho danh mục '" + category.getName() +
                    "' vì danh mục này có kiểu '" + category.getType().getDisplayName() + "'. " +
                    "Chỉ có thể cập nhật sản phẩm cho danh mục có kiểu 'Liên kết'.");
        }

        // chuyển DTO sang entiy
        productMapper.convertUpdateDtoToEntity(productUpdateDTO, existingProduct, category);

        // xử lý logic cập nhật các biến thể
        updateProductVariants(existingProduct, productUpdateDTO.getProductVariants());

        // lưu
        Product updatedProduct = productRepository.save(existingProduct);

        return productMapper.convertToDTO(updatedProduct);
    }

    @Override
    @Transactional
    public ResponseMessageDTO deleteProduct(Long productId) {
        Product product = findProductById(productId);

        productRepository.deleteById(productId);

        return new ResponseMessageDTO(HttpStatus.OK, "Xóa sản phẩm với ID: " + productId + " thành công");

    }

    @Override
    @Transactional
    public List<ProductResponseDTO> findProductsByCategory(Long categoryId) {
        // Validate category exists
        findCategoryById(categoryId);

        // Use existing specification with only categoryId filter
        Specification<Product> spec = productSpecification.findByCriteria(null, null, null, null, null, null,
                categoryId, null, null);
        return productRepository.findAll(spec)
                .stream()
                .map(productMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<ProductResponseDTO> findProductsByCategoryWithPagination(Long categoryId, Pageable pageable) {
        // Validate category exists
        findCategoryById(categoryId);

        // Use existing specification with only categoryId filter
        Specification<Product> spec = productSpecification.findByCriteria(null, null, null, null, null, null,
                categoryId, null, null);
        Page<Product> productPage = productRepository.findAll(spec, pageable);

        return productPage.map(productMapper::convertToDTO);
    }

    // hàm helper tạo 1 product variant
    private ProductVariant createVariantFromDTO(ProductVariantCreateDTO productVariantCreateDTO) {
        Color color = findColorById(productVariantCreateDTO.getColorId());

        // Create ProductVariant (without sizes for now)
        ProductVariant variant = productVariantMapper.convertCreateDtoToEntity(productVariantCreateDTO, color);

        // Create ProductVariantSizes
        if (productVariantCreateDTO.getSizes() != null && !productVariantCreateDTO.getSizes().isEmpty()) {
            for (var sizeCreateDTO : productVariantCreateDTO.getSizes()) {
                Size size = findSizeById(sizeCreateDTO.getSizeId());
                var productVariantSize = productVariantSizeMapper.convertCreateDtoToEntity(sizeCreateDTO, variant,
                        size);
                variant.addProductVariantSize(productVariantSize);
            }
        }

        return variant;
    }

    private void updateProductVariants(Product product, List<ProductVariantUpdateDTO> variantDTOs) {
        // Map các biến thể hiện có bằng ID để dễ truy cập
        Map<Long, ProductVariant> existingVariantsMap = product.getProductVariants().stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));

        // Set để lưu các biến thể cuối cùng (đã cập nhật, mới, hoặc giữ nguyên)
        List<ProductVariant> finalVariants = new ArrayList<>();

        for (ProductVariantUpdateDTO dto : variantDTOs) {
            // Trường hợp 1: Cập nhật biến thể đã có (DTO có ID)
            if (dto.getId() != null) {
                // Lấy biến thể gốc từ map, nếu không có thì lỗi
                ProductVariant existingVariant = Optional.ofNullable(existingVariantsMap.get(dto.getId()))
                        .orElseThrow(() -> new NotFoundException(
                                "Không tìm thấy biến thể với ID: " + dto.getId() + " để cập nhật."));

                // Update basic fields
                Color color = findColorById(dto.getColorId());
                existingVariant.setColor(color);
                existingVariant.setImageUrl(dto.getImageUrl());
                existingVariant.setStatus(dto.getStatus());

                // Update ProductVariantSizes
                updateProductVariantSizes(existingVariant, dto.getSizes());

                // Update ProductVariantImages
                updateProductVariantImages(existingVariant, dto.getImages());

                finalVariants.add(existingVariant);
                existingVariantsMap.remove(dto.getId());
            }
            // Trường hợp 2: Thêm biến thể mới (DTO không có ID)
            else {
                Color color = findColorById(dto.getColorId());

                // Check for duplicate color
                boolean isDuplicate = product.getProductVariants().stream()
                        .anyMatch(v -> v.getColor().getId().equals(color.getId()));
                if (isDuplicate) {
                    throw new IllegalArgumentException("Biến thể với Màu '" + color.getName() + "' đã tồn tại.");
                }

                // Create new variant
                ProductVariant newVariant = productVariantMapper.convertUpdateDtoToNewEntity(dto, color);
                newVariant.setProduct(product);

                // Add sizes to new variant
                if (dto.getSizes() != null && !dto.getSizes().isEmpty()) {
                    for (var sizeUpdateDTO : dto.getSizes()) {
                        Size size = findSizeById(sizeUpdateDTO.getSizeId());
                        var productVariantSize = productVariantSizeMapper.convertUpdateDtoToNewEntity(sizeUpdateDTO,
                                newVariant, size);
                        newVariant.addProductVariantSize(productVariantSize);
                    }
                }

                finalVariants.add(newVariant);
            }
        }

        // Cập nhật lại danh sách biến thể của Product.
        // Thao tác này sẽ tự động xóa các biến thể còn lại trong `existingVariantsMap`
        // nhờ `orphanRemoval=true`.
        product.getProductVariants().clear();
        product.getProductVariants().addAll(finalVariants);
    }

    private void updateProductVariantSizes(ProductVariant variant, List<ProductVariantSizeUpdateDTO> sizeUpdateDTOs) {
        // Map existing sizes by ID for easy access
        Map<Long, ProductVariantSize> existingSizesMap = variant.getProductVariantSizes().stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));

        // Set to store final sizes
        List<ProductVariantSize> finalSizes = new ArrayList<>();

        for (ProductVariantSizeUpdateDTO sizeDTO : sizeUpdateDTOs) {
            if (sizeDTO.getId() != null) {
                // Update existing size
                ProductVariantSize existingSize = Optional.ofNullable(existingSizesMap.get(sizeDTO.getId()))
                        .orElseThrow(() -> new NotFoundException(
                                "Không tìm thấy kích thước với ID: " + sizeDTO.getId() + " để cập nhật."));

                Size size = findSizeById(sizeDTO.getSizeId());
                productVariantSizeMapper.convertUpdateDtoToEntity(sizeDTO, existingSize, size);
                finalSizes.add(existingSize);
                existingSizesMap.remove(sizeDTO.getId());
            } else {
                // Create new size
                Size size = findSizeById(sizeDTO.getSizeId());

                // Check for duplicate size in this variant
                boolean isDuplicate = variant.getProductVariantSizes().stream()
                        .anyMatch(pvs -> pvs.getSize().getId().equals(size.getId()));
                if (isDuplicate) {
                    throw new IllegalArgumentException(
                            "Kích thước '" + size.getName() + "' đã tồn tại trong biến thể này.");
                }

                ProductVariantSize newSize = productVariantSizeMapper.convertUpdateDtoToNewEntity(sizeDTO, variant,
                        size);
                finalSizes.add(newSize);
            }
        }

        // Update variant sizes
        variant.getProductVariantSizes().clear();
        variant.getProductVariantSizes().addAll(finalSizes);
    }

    // Method removed - no longer needed with new ProductVariantSize structure

    // hàm check exist
    private void checkProductNameExists(String productName) {
        if (productRepository.existsByName(productName)) {
            throw new IllegalArgumentException("Tên sản phẩm đã tồn tại: " + productName);
        }
    }

    private void checkProductNameExistsForUpdate(String productName, Long productId) {
        if (productRepository.existsByNameAndIdNot(productName, productId)) {
            throw new IllegalArgumentException("Tên sản phẩm '" + productName + "' đã tồn tại cho một sản phẩm khác");
        }
    }

    private void checkProductExists(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException(" Không tìm thấy sản phẩm với ID: " + productId);
        }
    }

    private void checkDuplicateVariantsOncreate(List<ProductVariantCreateDTO> variantCreateDTOS) {
        // Check for duplicate colors (since each variant now represents a color with
        // multiple sizes)
        long distinctColorCount = variantCreateDTOS.stream()
                .map(ProductVariantCreateDTO::getColorId)
                .distinct()
                .count();
        if (distinctColorCount < variantCreateDTOS.size()) {
            throw new IllegalArgumentException(
                    "Không thể tạo sản phẩm với các biến thể màu sắc trùng lặp");
        }

        // Check for duplicate sizes within each variant
        for (ProductVariantCreateDTO variantDTO : variantCreateDTOS) {
            if (variantDTO.getSizes() != null && !variantDTO.getSizes().isEmpty()) {
                long distinctSizeCount = variantDTO.getSizes().stream()
                        .map(size -> size.getSizeId())
                        .distinct()
                        .count();
                if (distinctSizeCount < variantDTO.getSizes().size()) {
                    throw new IllegalArgumentException(
                            "Không thể tạo biến thể với các kích thước trùng lặp");
                }
            }
        }
    }

    private void checkDuplicateVariantsOnUpdate(List<ProductVariantUpdateDTO> variantUpdateDTOList) {
        // Check for duplicate colors
        long distinctColorCount = variantUpdateDTOList.stream()
                .map(ProductVariantUpdateDTO::getColorId)
                .distinct()
                .count();
        if (distinctColorCount < variantUpdateDTOList.size()) {
            throw new IllegalArgumentException("Không thể cập nhật với các biến thể màu sắc trùng lặp");
        }

        // Check for duplicate sizes within each variant
        for (ProductVariantUpdateDTO variantDTO : variantUpdateDTOList) {
            if (variantDTO.getSizes() != null && !variantDTO.getSizes().isEmpty()) {
                long distinctSizeCount = variantDTO.getSizes().stream()
                        .map(size -> size.getSizeId())
                        .distinct()
                        .count();
                if (distinctSizeCount < variantDTO.getSizes().size()) {
                    throw new IllegalArgumentException(
                            "Không thể cập nhật biến thể với các kích thước trùng lặp");
                }
            }
        }
    }

    // hàm find
    private Product findProductById(Long productId) {
        return productRepository.findByIdWithVariants(productId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + productId));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + categoryId));
    }

    private Color findColorById(Long colorId) {
        return colorRepository.findById(colorId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy màu sắc với ID: " + colorId));
    }

    private Size findSizeById(Long sizeId) {
        return sizeRepository.findById(sizeId)
                .orElseThrow(() -> new NotFoundException(" Không tìm thấy kích thước với ID: " + sizeId));
    }

    private void updateProductVariantImages(ProductVariant variant, List<ProductVariantImageDTO> imageDTOs) {
        if (imageDTOs == null) {
            return;
        }
    
        // --- BƯỚC 1: Lưu lại ID của ảnh chính hiện tại (nếu có) trước khi xóa ---
        Long currentPrimaryImageId = variant.getImages().stream()
                .filter(ProductVariantImage::isPrimary)
                .findFirst()
                .map(BaseEntity::getId)
                .orElse(null);
    
        // --- BƯỚC 2: Map các DTO mới gửi lên bằng ID của chúng (nếu có) ---
        Map<Long, ProductVariantImageDTO> dtoMap = imageDTOs.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.toMap(ProductVariantImageDTO::getId, Function.identity()));
    
        // --- BƯỚC 3: Xóa các ảnh cũ và thêm/cập nhật ảnh mới ---
        variant.getImages().clear(); // orphanRemoval sẽ xóa các ảnh không còn trong list
    
        List<ProductVariantImage> finalImages = new ArrayList<>();
        for (int i = 0; i < imageDTOs.size(); i++) {
            ProductVariantImageDTO imageDTO = imageDTOs.get(i);
            ProductVariantImage image = ProductVariantImage.builder()
                    .imageUrl(imageDTO.getImageUrl())
                    .altText(imageDTO.getAltText() != null ? imageDTO.getAltText() : "")
                    .isPrimary(imageDTO.isPrimary()) // Lấy trạng thái isPrimary từ DTO
                    .displayOrder(i)
                    .productVariant(variant)
                    .build();
            finalImages.add(image);
        }
        variant.getImages().addAll(finalImages);
    
    
        // --- BƯỚC 4: Logic đặt ảnh chính một cách "thông minh" ---
        if (!variant.getImages().isEmpty()) {
            // Kiểm tra xem frontend có chỉ định ảnh chính mới không
            boolean newPrimaryExists = variant.getImages().stream().anyMatch(ProductVariantImage::isPrimary);
    
            if (!newPrimaryExists) {
                // Nếu frontend không chỉ định, hãy thử khôi phục ảnh chính cũ
                boolean restored = false;
                if (currentPrimaryImageId != null) {
                    // Kiểm tra xem ảnh chính cũ có còn trong danh sách DTO mới không
                    if (dtoMap.containsKey(currentPrimaryImageId)) {
                         // Tìm lại ảnh tương ứng trong `finalImages` và đặt nó làm ảnh chính
                        variant.getImages().stream()
                            .filter(img -> img.getImageUrl().equals(dtoMap.get(currentPrimaryImageId).getImageUrl()))
                            .findFirst()
                            .ifPresent(img -> {
                                img.setPrimary(true);
                            });
                        restored = true;
                    }
                }
                
                // Nếu không thể khôi phục (ảnh chính cũ đã bị xóa), thì mới đặt ảnh đầu tiên làm ảnh chính
                if(!restored){
                    variant.getImages().get(0).setPrimary(true);
                }
            }
        }
    }

}
