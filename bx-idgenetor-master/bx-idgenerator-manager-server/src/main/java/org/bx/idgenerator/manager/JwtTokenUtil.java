package org.bx.idgenerator.manager;


import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bx.idgenerator.bean.LoginUserBean;
import org.bx.idgenerator.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JWT 工具类
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月13日 14时48分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Component
public class JwtTokenUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Autowired
    private JwtProperty jwtProperty;

    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, jwtProperty.getSecret())
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtProperty.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.error("JWT格式验证失败:{}", token);
            throw new BizException("JWT格式验证失败",e);
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + jwtProperty.getExpiration() * 1000);
    }

    /**
     * 从token中获取用户信息
     */
    public LoginUserBean getLoginUserFromToken(String token) {
        Claims claims = null;
        try {
            claims = getClaimsFromToken(token);
            LinkedHashMap<String, Object> sub = (LinkedHashMap<String, Object>) claims.get(CLAIM_KEY_USERNAME);
            Gson gson = new Gson();
            LoginUserBean loginUser = gson.fromJson(gson.toJson(sub), LoginUserBean.class);
            return loginUser;
        } catch (Exception e) {
            LOGGER.info("JWT信息转换获取失败，token{},用户信息:{}", token, claims);
            throw new BizException(String.format("JWT信息转换获取失败,token:%s,用户信息:%s",token,claims),e);
        }
    }

    /**
     * 验证 token 是否还有效
     *
     * @param token     客户端传入的token
     * @param dbLoginUser 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, LoginUserBean dbLoginUser) {
        LoginUserBean tokenUser = getLoginUserFromToken(token);
        return tokenUser.getId().equals(dbLoginUser.getId()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否已经失效
     */
    public boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     */
    public String generateToken(LoginUserBean loginUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, loginUser);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 根据负责生成JWT的token
     * 'Bear token121212'
     */
    public String generateTokenWithHead(LoginUserBean loginUser) {
        String token = generateToken(loginUser);
        return jwtProperty.getTokenHead() + token;
    }




}
