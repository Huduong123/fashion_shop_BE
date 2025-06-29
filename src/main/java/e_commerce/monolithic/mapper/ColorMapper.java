package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.admin.color.ColorCreateDTO;
import e_commerce.monolithic.dto.admin.color.ColorResponseDTO;
import e_commerce.monolithic.dto.admin.color.ColorUpdateDTO;
import e_commerce.monolithic.entity.Color;
import org.springframework.stereotype.Component;

@Component
public class ColorMapper {

    public ColorResponseDTO convertToDTO(Color color) {
        if (color == null) return null;

        return new ColorResponseDTO(
                color.getId(),
                color.getName(),
                color.getCreatedAt(),
                color.getUpdatedAt()
        );
    }

    public Color convertCreateDtoToEntity(ColorCreateDTO colorCreateDTO) {
        if (colorCreateDTO == null) return null;

        return Color.builder()
                .name(colorCreateDTO.getName())
                .build();
    }

    public void convertUpdateDtoToEntity(ColorUpdateDTO colorUpdateDTO, Color existingColer) {
        if (existingColer == null || colorUpdateDTO  == null) return ;


        existingColer.setName(colorUpdateDTO.getName());

    };
}
