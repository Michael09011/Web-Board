package net.datasa.web5.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datasa.web5.domain.entity.MemberEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private String memberId;
    private String memberPassword;
    private String memberName;
    private String email;
    private String phone;
    private String address;
    private Boolean enabled;
    private String rolename;

    // Entity → DTO 변환
    public static MemberDTO fromEntity(MemberEntity memberEntity) {
        return MemberDTO.builder()
                .memberId(memberEntity.getMemberId())
                .memberPassword(memberEntity.getMemberPassword())
                .memberName(memberEntity.getMemberName())
                .email(memberEntity.getEmail())
                .phone(memberEntity.getPhone())
                .address(memberEntity.getAddress())
				.enabled(memberEntity.isEnabled())
                .rolename(memberEntity.getRolename())
                .build();
    }

    // DTO → Entity 변환
    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .memberId(this.memberId)
                .memberPassword(this.memberPassword)
                .memberName(this.memberName)
                .email(this.email)
                .phone(this.phone)
                .address(this.address)
                .enabled(Boolean.TRUE.equals(this.enabled)) // 기본값을 true로 설정
                .rolename(this.rolename)
                .build();
    }
}
