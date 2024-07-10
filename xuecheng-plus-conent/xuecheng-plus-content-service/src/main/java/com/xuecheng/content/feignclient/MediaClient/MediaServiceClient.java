package com.xuecheng.content.feignclient.MediaClient;

import com.xuecheng.content.config.MultipartSupportConfig;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 媒资管理服务远程调用接口
 */
//方式一：fallback。定义一个fallback类MediaServiceClientFallback，此类实现了MediaServiceClient接口
//@FeignClient(value = "media-api",configuration = MultipartSupportConfig.class,fallback = MediaServiceClientFallback.class)
//方式2:方式二：fallbackFactory。由于方式一无法取出熔断所抛出的异常，而方式二定义MediaServiceClientFallbackFactory可以解决这个问题
@FeignClient(value = "media-api", configuration = MultipartSupportConfig.class,fallbackFactory = MediaServiceClientFallbackFactory.class)
public interface MediaServiceClient {
    @RequestMapping(value = "/media/upload/coursefile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String upload(@RequestPart("filedata") MultipartFile upload,
                  @RequestParam(value = "folder", required = false) String folder,
                  @RequestParam(value = "objectName", required = false) String objectName);

}
