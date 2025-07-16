package e_commerce.monolithic.service.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import e_commerce.monolithic.dto.admin.size.SizeResponseDTO;
import e_commerce.monolithic.mapper.SizeMapper;
import e_commerce.monolithic.repository.SizeRepository;

@Service
public class UserSizeServiceImp implements UserSizeService{

    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;

    public UserSizeServiceImp(SizeRepository sizeRepository, SizeMapper sizeMapper) {
        this.sizeRepository = sizeRepository;
        this.sizeMapper = sizeMapper;
    }
    @Override
    public List<SizeResponseDTO> getAllSizes() {
        return sizeRepository.findAll().stream()
                .sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName()))
                .map(sizeMapper::convertToDTO)
                .collect(Collectors.toList());
    }
    
}
