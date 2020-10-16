package com.s1mple.minischool.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Utils {

    public static String encode(String img) throws JsonProcessingException, UnsupportedEncodingException {
        String encode = Base64.encodeBase64String(img.getBytes("UTF-8"));
        return encode;
    }

    public static String decode(String base64Img) throws UnsupportedEncodingException {
        String decode = new String(Base64.decodeBase64(base64Img),"UTF-8");
        return decode;
    }

}
