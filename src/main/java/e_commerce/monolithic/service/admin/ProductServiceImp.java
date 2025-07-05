package e_commerce.monolithic.service.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import e_commerce.monolithic.dto.admin.product.ProductVariantUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.BaseEntity;
import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.entity.Color;
import e_commerce.monolithic.entity.Product;
import e_commerce.monolithic.entity.ProductVariant;
import e_commerce.monolithic.entity.Size;
import e_commerce.monolithic.exeption.NotFoundException;
import e_commerce.monolithic.mapper.ProductMapper;
import e_commerce.monolithic.mapper.ProductVariantMapper;
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

    public ProductServiceImp(ProductRepository productRepository, CategoryRepository categoryRepository,
            ColorRepository colorRepository, SizeRepository sizeRepository, ProductMapper productMapper,
            ProductVariantMapper productVariantMapper, ProductSpecification productSpecification) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.productMapper = productMapper;
        this.productVariantMapper = productVariantMapper;
        this.productSpecification = productSpecification;
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

        // chuyển DTO sang entity
        Product newProduct = productMapper.convertCreateDtoToEntity(productCreateDTO, category);

        // xử lí biến tể
        Set<ProductVariant> variants = productCreateDTO.getProductVariants()
                .stream()
                .map(this::createVariantFromDTO)
                .collect(Collectors.toSet());

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
        Size size = productVariantCreateDTO.getSizeId() != null ? findSizeById(productVariantCreateDTO.getSizeId())
                : null;
        return productVariantMapper.convertCreateDtoToEntity(productVariantCreateDTO, color, size);
    }

    private void updateProductVariants(Product product, List<ProductVariantUpdateDTO> variantDTOs) {
        // Map các biến thể hiện có bằng ID để dễ truy cập
        Map<Long, ProductVariant> existingVariantsMap = product.getProductVariants().stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));

        // Set để lưu các biến thể cuối cùng (đã cập nhật, mới, hoặc giữ nguyên)
        Set<ProductVariant> finalVariants = new HashSet<>();

        for (ProductVariantUpdateDTO dto : variantDTOs) {
            // Trường hợp 1: Cập nhật biến thể đã có (DTO có ID)
            if (dto.getId() != null) {
                // Lấy biến thể gốc từ map, nếu không có thì lỗi
                ProductVariant existingVariant = Optional.ofNullable(existingVariantsMap.get(dto.getId()))
                        .orElseThrow(() -> new NotFoundException(
                                "Không tìm thấy biến thể với ID: " + dto.getId() + " để cập nhật."));

                // Kiểm tra xem có cần cập nhật không
                if (isVariantModified(existingVariant, dto)) {
                    // Chỉ cập nhật nếu có sự thay đổi
                    Color color = findColorById(dto.getColorId());
                    Size size = dto.getSizeId() != null ? findSizeById(dto.getSizeId()) : null;

                    // Logic kiểm tra trùng lặp khi thay đổi color/size
                    String newAttributeKey = color.getId() + "-" + (size != null ? size.getId() : "null");
                    String oldAttributeKey = existingVariant.getColor().getId() + "-"
                            + (existingVariant.getSize() != null ? existingVariant.getSize().getId() : "null");

                    if (!newAttributeKey.equals(oldAttributeKey)) {
                        // Nếu thay đổi, kiểm tra xem key mới có bị một biến thể khác chiếm giữ không
                        boolean isDuplicate = product.getProductVariants().stream()
                                .anyMatch(v -> !v.getId().equals(dto.getId())
                                        && (v.getColor().getId() + "-"
                                                + (v.getSize() != null ? v.getSize().getId() : "null"))
                                                .equals(newAttributeKey));
                        if (isDuplicate) {
                            throw new IllegalArgumentException("Không thể cập nhật. Biến thể với Màu '"
                                    + color.getName() + "' và Size '" + (size != null ? size.getName() : "Không có")
                                    + "' đã tồn tại.");
                        }
                    }

                    existingVariant.setColor(color);
                    existingVariant.setSize(size);
                    existingVariant.setPrice(dto.getPrice());
                    existingVariant.setQuantity(dto.getQuantity());
                    existingVariant.setImageUrl(dto.getImageUrl());
                }

                finalVariants.add(existingVariant); // Thêm biến thể (dù có sửa hay không) vào danh sách cuối cùng
                existingVariantsMap.remove(dto.getId()); // Xóa khỏi map để theo dõi các biến thể cần xóa
            }
            // Trường hợp 2: Thêm biến thể mới (DTO không có ID)
            else {
                Color color = findColorById(dto.getColorId());
                Size size = dto.getSizeId() != null ? findSizeById(dto.getSizeId()) : null;
                String attributeKey = color.getId() + "-" + (size != null ? size.getId() : "null");

                // Kiểm tra trùng lặp với tất cả các biến thể đã có của sản phẩm
                boolean isDuplicate = product.getProductVariants().stream()
                        .anyMatch(
                                v -> (v.getColor().getId() + "-" + (v.getSize() != null ? v.getSize().getId() : "null"))
                                        .equals(attributeKey));
                if (isDuplicate) {
                    throw new IllegalArgumentException(
                            "Biến thể với Màu '" + color.getName() + "' và Size '"
                                    + (size != null ? size.getName() : "Không có") + "' đã tồn tại.");
                }

                ProductVariant newVariant = productVariantMapper.convertUpdateDtoToNewEntity(dto, color, size);
                newVariant.setProduct(product);
                finalVariants.add(newVariant);
            }
        }

        // Cập nhật lại danh sách biến thể của Product.
        // Thao tác này sẽ tự động xóa các biến thể còn lại trong `existingVariantsMap`
        // nhờ `orphanRemoval=true`.
        product.getProductVariants().clear();
        product.getProductVariants().addAll(finalVariants);
    }

    /**
     * Hàm helper để kiểm tra xem một biến thể có bị thay đổi so với DTO không.
     */
    private boolean isVariantModified(ProductVariant variant, ProductVariantUpdateDTO dto) {
        // So sánh từng trường. Nếu có bất kỳ trường nào khác nhau, trả về true.
        if (!variant.getColor().getId().equals(dto.getColorId()))
            return true;
        // Handle null size comparison
        Long variantSizeId = variant.getSize() != null ? variant.getSize().getId() : null;
        if (!java.util.Objects.equals(variantSizeId, dto.getSizeId()))
            return true;
        if (variant.getPrice().compareTo(dto.getPrice()) != 0)
            return true;
        if (variant.getQuantity() != dto.getQuantity())
            return true;
        // So sánh imageUrl, cần xử lý null
        return !java.util.Objects.equals(variant.getImageUrl(), dto.getImageUrl());
    }

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
        long distinctCount = variantCreateDTOS.stream()
                .map(v -> v.getColorId() + "-" + (v.getSizeId() != null ? v.getSizeId() : "null"))
                .distinct()
                .count();
        if (distinctCount < variantCreateDTOS.size()) {
            throw new IllegalArgumentException(
                    " Không tể tạo sản phẩm với các biến thể (màu sắc , kích thước) trùng lặp");
        }
    }

    private void checkDuplicateVariantsOnUpdate(List<ProductVariantUpdateDTO> variantUpdateDTOList) {
        long distinctCount = variantUpdateDTOList.stream()
                .map(v -> v.getColorId() + "-" + (v.getSizeId() != null ? v.getSizeId() : "null"))
                .distinct()
                .count();
        if (distinctCount < variantUpdateDTOList.size()) {
            throw new IllegalArgumentException("Không thể cập nhật với các biến thể ( màu sắc, kích thước) trùng lặp");
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

}
