package com.s1mple.minischool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.s1mple.minischool.dao.TrendsImgMapper;
import com.s1mple.minischool.domain.po.Trendsimg;
import com.s1mple.minischool.service.TrendsImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
@Transactional
public class TrendsImgServiceImpl extends ServiceImpl<TrendsImgMapper, Trendsimg> implements TrendsImgService {

    @Value("${web.upload-path}")
    private String uploadPath;


    @Autowired
    private TrendsImgMapper trendsImgMapper;

    @Override
    public Long insertImg(MultipartFile file, HttpServletRequest request) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String format = "trendsImg/"+sdf.format(new Date());
        File folder = new File(uploadPath + format);
        if (!folder.exists()){
            /*判断文件夹是否存在 不存在新建一个*/
            folder.mkdirs();
        }
        String filename = System.currentTimeMillis() + Math.round(Math.random() * 1000)+file.getOriginalFilename();
        file.transferTo(new File(folder,filename));
        String filePath = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + "/" + format + filename;
        Trendsimg img = Trendsimg.builder().imgUrl(filePath).build();
        trendsImgMapper.insert(img);
        return img.getImg_id();
    }
}
