package net.datasa.web5.domain.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
public class MemberEntity {

    @Id
    @Column(name = "member_id", length = 30)
    private String memberId;

    @Column(name = "member_password", length = 100, nullable = false)
    private String memberPassword;


    @Column(name = "member_name", length = 30, nullable = false)
    private String memberName;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "enabled")
    @ColumnDefault("1")
    private boolean enabled;

    @Column(name = "rolename", length = 20)
    @ColumnDefault("'ROLE_USER'")
    private String rolename;
}
