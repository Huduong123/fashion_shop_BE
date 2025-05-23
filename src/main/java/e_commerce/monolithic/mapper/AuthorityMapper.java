package e_commerce.monolithic.mapper;

import e_commerce.monolithic.dto.admin.authorities.AuthorityCreateDTO;
import e_commerce.monolithic.dto.admin.authorities.AuthorityDTO;
import e_commerce.monolithic.dto.admin.authorities.AuthorityResponseDTO;
import e_commerce.monolithic.dto.admin.authorities.AuthorityUpdateDTO;
import e_commerce.monolithic.entity.Authority;
import e_commerce.monolithic.entity.User;

public class AuthorityMapper {
    public static AuthorityResponseDTO toResAuthorityDTO(Authority authority) {
        if (authority == null) {
            throw new IllegalArgumentException("Authority entity cannot be null for mapping for DTO");
        }
        if (authority.getUser() == null) {
            throw new IllegalArgumentException("Authority user cannot be null for mapping for DTO");
        }
        return AuthorityResponseDTO.builder()
                .id(authority.getId())
                .username(authority.getUser().getUsername())
                .authority(authority.getAuthority())
                .createdAt(authority.getCreatedAt())
                .updatedAt(authority.getUpdatedAt())
                .build();
    }
    /**
     * Chuyển đổi từ AuthorityCreateDTO sang Authority Entity.
     * Cần đối tượng User đã được tìm thấy từ database vì DTO chỉ chứa userId.
     *
     * @param authorityCreateDTO DTO chứa thông tin để tạo Authority mới.
     * @param user Đối tượng User được liên kết với Authority này.
     * @return Một đối tượng Authority mới.
     */
    public static Authority toCreateEntity(AuthorityCreateDTO authorityCreateDTO, User user) {
        if (authorityCreateDTO == null) {
            throw new IllegalArgumentException("AuthorityCreateDTO cannot be null for creating an Authority entity");
        }
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null for creating an Authority entity");
        }
        Authority authority = new Authority();
        authority.setAuthority(authorityCreateDTO.getAuthority());
        authority.setUser(user);
        return authority;
    }

    public static void toUpdateEntity(AuthorityUpdateDTO authorityUpdateDTO, Authority existingAuthority) {
        if (authorityUpdateDTO == null) {
            throw new IllegalArgumentException("AuthorityUpdateDTO cannot be null for updating an Authority entity.");
        }
        // Ném ngoại lệ nếu existingAuthority là null, vì không có Entity nào để cập nhật.
        if (existingAuthority == null) {
            throw new IllegalArgumentException("Existing Authority entity cannot be null for update operation.");
        }

        existingAuthority.setAuthority(authorityUpdateDTO.getAuthority());
    }

}
