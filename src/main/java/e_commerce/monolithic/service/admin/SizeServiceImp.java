package e_commerce.monolithic.service.admin;

import e_commerce.monolithic.dto.admin.size.SizeCreateDTO;
import e_commerce.monolithic.dto.admin.size.SizeResponseDTO;
import e_commerce.monolithic.dto.admin.size.SizeUpdateDTO;
import e_commerce.monolithic.dto.common.ResponseMessageDTO;
import e_commerce.monolithic.entity.Size;
import e_commerce.monolithic.mapper.SizeMapper;
import e_commerce.monolithic.repository.SizeRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SizeServiceImp implements SizeService {

    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;

    public SizeServiceImp(SizeRepository sizeRepository, SizeMapper sizeMapper) {
        this.sizeRepository = sizeRepository;
        this.sizeMapper = sizeMapper;
    }

    @Override
    public List<SizeResponseDTO> getAllSize() {
        return sizeRepository.findAll()
                .stream()
                .map(sizeMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SizeResponseDTO getSizeById(Long id) {
        Size size = checkSizeById(id);
        return sizeMapper.convertToDTO(size);
    }

    @Override
    @Transactional
    public SizeResponseDTO create(SizeCreateDTO createDTO) {
        checkSizeExistsByName(createDTO.getName());

        Size newSize = sizeMapper.convertCreateDTOToEntity(createDTO);

        Size savedSize = sizeRepository.save(newSize);

        return sizeMapper.convertToDTO(savedSize);
    }

    @Override
    @Transactional
    public SizeResponseDTO update(SizeUpdateDTO updateDTO, Long id) {
        Size size = checkSizeById(id);

        checkSizeNameExistsForUpdate(updateDTO.getName(), size.getId());

        sizeMapper.convertUpdateDTOToEntity(size, updateDTO);

        Size savedSize = sizeRepository.save(size);
        return sizeMapper.convertToDTO(savedSize);
    }

    @Override
    @Transactional
    public ResponseMessageDTO delete(Long id) {
        checkSizeExistsById(id);
        sizeRepository.deleteById(id);
        return new ResponseMessageDTO(HttpStatus.OK, "Size deleted successfully");
    }
    // hàm
    private Size checkSizeById(Long sizeId) {
        return sizeRepository.findById(sizeId).orElseThrow(() -> new IllegalArgumentException("Size not found!"));
    }

    private void checkSizeExistsByName(String name) {
        if (sizeRepository.existsByName(name)) {
            throw new IllegalArgumentException("Size "+  name +"already exists!");
        }
    }

    private void checkSizeNameExistsForUpdate(String sizeName, Long sizeId) {
        if (sizeRepository.existsByNameAndIdNot(sizeName, sizeId)) {
            throw new IllegalArgumentException("Size " + sizeName+ " already exists!");
        }
    }


    private void checkSizeExistsById (Long sizeId) {
        if (!sizeRepository.existsById(sizeId)) {
            throw new IllegalArgumentException("Size " + sizeId + " already exists!");
        }
    }
}
