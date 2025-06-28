package org.bx.idgenerator.util;

import org.bx.idgenerator.exception.BizException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020/7/9/16:22
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public class MD5Utils {
    public static String md5(String pwd) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(pwd.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new BizException(e.getMessage());
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
}
