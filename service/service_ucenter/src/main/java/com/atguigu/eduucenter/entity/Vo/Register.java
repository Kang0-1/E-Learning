package com.atguigu.eduucenter.entity.Vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Register {
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "验证码")
    private String code;
}
