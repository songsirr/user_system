package com.mj.user.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestCertificatePhone {

    @ApiModelProperty(notes = "연락처")
    private String phone;

    @ApiModelProperty(notes = "인증번호")
    private String code;
}
