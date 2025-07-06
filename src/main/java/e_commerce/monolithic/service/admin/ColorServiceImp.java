package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.color.ColorCreateDTO;
import e_commerce.monolithic.dto.admin.color.ColorResponseDTO;
import e_commerce.monolithic.dto.admin.color.ColorUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.Color;
import e_commerce.monolithic.mapper.ColorMapper;
import e_commerce.monolithic.repository.ColorRepository;
import e_commerce.monolithic.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorServiceImp implements ColorService {

    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;
    private final ProductVariantRepository productVariantRepository;

    public ColorServiceImp(ColorRepository colorRepository, ColorMapper colorMapper,
            ProductVariantRepository productVariantRepository) {
        this.colorRepository = colorRepository;
        this.colorMapper = colorMapper;
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public List<ColorResponseDTO> findAllColors() {
        return colorRepository.findAll()
                .stream()
                .map(colorMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ColorResponseDTO findColorById(Long colorId) {
        Color color = checkColorById(colorId);
        return colorMapper.convertToDTO(color);
    }

    @Override
    @Transactional
    public ColorResponseDTO createColor(ColorCreateDTO colorCreateDTO) {

        checkColorNameExists(colorCreateDTO.getName());

        Color newColor = colorMapper.convertCreateDtoToEntity(colorCreateDTO);

        Color savedColor = colorRepository.save(newColor);

        return colorMapper.convertToDTO(savedColor);
    }

    @Override
    @Transactional
    public ColorResponseDTO updateColorById(ColorUpdateDTO colorUpdateDTO, Long colorId) {
        Color color = checkColorById(colorId);

        checkColorNameExistsForUpdate(colorUpdateDTO.getName(), color.getId());

        colorMapper.convertUpdateDtoToEntity(colorUpdateDTO, color);

        Color savedColor = colorRepository.save(color);

        return colorMapper.convertToDTO(savedColor);
    }

    @Override
    @Transactional
    public ResponseMessageDTO deleteColorById(Long colorId) {
        checkColorExists(colorId);
        checkColorNotInUse(colorId);
        colorRepository.deleteById(colorId);
        return new ResponseMessageDTO(HttpStatus.OK, "Color deleted successfully");
    }

    // hàm
    private Color checkColorById(Long colorId) {
        return colorRepository.findById(colorId)
                .orElseThrow(() -> new IllegalArgumentException("No color found with id: " + colorId));
    }

    private void checkColorNameExists(String colorName) {
        if (colorRepository.existsByName(colorName)) {
            throw new IllegalArgumentException("Color " + colorName + " already exists");
        }
    }

    private void checkColorNameExistsForUpdate(String colorName, Long colorId) {
        if (colorRepository.existsByNameAndIdNot(colorName, colorId)) {
            throw new IllegalArgumentException("Color " + colorName + " already exists");
        }
    }

    private void checkColorExists(Long colorId) {
        if (!colorRepository.existsById(colorId)) {
            throw new IllegalArgumentException("Color " + colorId + " already exists");
        }
    }

    private void checkColorNotInUse(Long colorId) {
        if (productVariantRepository.existsByColorId(colorId)) {
            long productCount = productVariantRepository.countProductsByColorId(colorId);
            throw new IllegalArgumentException("Không thể xóa màu này vì đang có " + productCount
                    + " sản phẩm sử dụng. Vui lòng xóa các sản phẩm liên quan trước.");
        }
    }
}
