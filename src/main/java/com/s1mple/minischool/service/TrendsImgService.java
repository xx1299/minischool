package com.s1mple.minischool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.s1mple.minischool.domain.po.Trendsimg;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface TrendsImgService extends IService<Trendsimg> {
    Long insertImg(MultipartFile file, HttpServletRequest request) throws IOException;
}
