package com.mj.user.domain.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 휴대전화 인증을 위한 테이블
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "certificate_phone")
@DynamicUpdate
@Builder
public class CertificatePhone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ApiModelProperty(notes = "요청 휴대전화 번호")
    @Column(name = "phone")
    private String phone;

    @ApiModelProperty(notes = "발송 코드")
    @Column(name = "code")
    private String code;

    @ApiModelProperty(notes = "성공 여부")
    @Column(name = "status")
    private String status;

    @ApiModelProperty(notes = "요청일시")
    @Column(name = "requested_at")
    private Date requestedAt;

    @ApiModelProperty(notes = "인증일시")
    @Column(name = "certificated_at")
    private Date certificatedAt;

}
