package e_commerce.monolithic.service.user;

import e_commerce.monolithic.dto.admin.color.ColorResponseDTO;
import e_commerce.monolithic.mapper.ColorMapper;
import e_commerce.monolithic.repository.ColorRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UserColorServiceImp implements UserColorService{

    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;

    public UserColorServiceImp(ColorRepository colorRepository, ColorMapper colorMapper) {
        this.colorRepository = colorRepository;
        this.colorMapper = colorMapper;
    }

    @Override
    public List<ColorResponseDTO> getAllColors() {
          // Sắp xếp theo tên để hiển thị nhất quán
    return colorRepository.findAll().stream()
            .sorted((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()))
            .map(colorMapper::convertToDTO)
            .collect(Collectors.toList());
    }
    
}
