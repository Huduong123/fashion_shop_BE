package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.product.ProductCreateDTO;
import e_commerce.monolithic.dto.admin.product.ProductResponseDTO;
import e_commerce.monolithic.dto.admin.product.ProductUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.Category;
import e_commerce.monolithic.entity.Product;

import java.util.List;

public interface ProductService {

     List<ProductResponseDTO> findAll();

     ProductResponseDTO findById(Long productId);

     ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO);

    ProductResponseDTO updateProduct(Long productId, ProductUpdateDTO productUpdateDTO);

    ResponseMessageDTO deleteProduct(Long productId);
}
