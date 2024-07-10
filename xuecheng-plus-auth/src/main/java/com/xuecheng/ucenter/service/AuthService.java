package com.xuecheng.ucenter.service;

import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;

/**
 * @description 提供认证服务
 * */
public interface AuthService {

    XcUserExt execute(AuthParamsDto authParamsDto);
}
