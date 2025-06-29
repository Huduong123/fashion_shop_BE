package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.admin.size.SizeCreateDTO;
import e_commerce.monolithic.dto.admin.size.SizeResponseDTO;
import e_commerce.monolithic.dto.admin.size.SizeUpdateDTO;
import e_commerce.monolithic.entity.Size;
import org.springframework.stereotype.Component;

@Component
public class SizeMapper {

    public SizeResponseDTO convertToDTO(Size size) {
        if (size == null) {
            return null;
        }
        return  new SizeResponseDTO(
                size.getId(),
                size.getName(),
                size.getCreatedAt(),
                size.getUpdatedAt()
        );
    }


    public Size convertCreateDTOToEntity(SizeCreateDTO sizeCreateDTO) {
        if (sizeCreateDTO == null) {
            return null;
        }


        return  Size.builder()
                .name(sizeCreateDTO.getName())
                .build();
    }

    public void convertUpdateDTOToEntity(Size existingSize, SizeUpdateDTO sizeUpdateDTO) {
        if (existingSize == null || sizeUpdateDTO == null) {
            return;
        }

        existingSize.setName(sizeUpdateDTO.getName());
    }
}
