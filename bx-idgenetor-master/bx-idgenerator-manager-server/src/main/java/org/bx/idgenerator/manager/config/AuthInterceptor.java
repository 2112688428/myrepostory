package org.bx.idgenerator.manager.config;

import org.bx.idgenerator.bean.LoginUserBean;
import org.bx.idgenerator.exception.BizException;
import org.bx.idgenerator.manager.CurrentContextHolder;
import org.bx.idgenerator.manager.JwtProperty;
import org.bx.idgenerator.manager.JwtTokenUtil;
import org.bx.idgenerator.manager.service.impl.IdGeneratorUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月13日 14时49分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private IdGeneratorUserServiceImpl idGeneratorUserService;
    @Autowired
    private JwtProperty jwtProperty;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {

        //  如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        // 检查 LoginAuthToken
        if (method.isAnnotationPresent(LoginAuthToken.class)) {
            LoginAuthToken loginAuthToken = method.getAnnotation(LoginAuthToken.class);
            // 需要登录权限
            if (loginAuthToken.required()) {
                String token = httpServletRequest.getHeader(jwtProperty.getTokenHeader());
                // 执行认证
                if (token == null) {
                    throw new BizException("请求头参数丢失或未登录");
                }
                // 去除Head
                if(token.startsWith(jwtProperty.getTokenHead())){
                    token = token.substring(token.indexOf(jwtProperty.getTokenHead())+7);
                }
                if(jwtTokenUtil.isTokenExpired(token)){
                    throw new BizException("token失效或未登录");
                }
                // 获取 token 中的 user id
                LoginUserBean loginUser = jwtTokenUtil.getLoginUserFromToken(token);
                if (loginUser == null) {
                    throw new BizException("用户不存在，请重新登录");
                }
                CurrentContextHolder.setUser(loginUser);
                return true;
            }
            return true;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
        CurrentContextHolder.clearContext();
    }
}
