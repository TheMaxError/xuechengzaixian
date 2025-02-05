package com.xuecheng.checkcode.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPswDto {
    String cellphone;

    String email;

    String checkcodekey;

    String checkcode;

    String password;

    String confirmpwd;
}
