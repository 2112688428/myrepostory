package org.bx.idgenerator.manager;

import org.bx.idgenerator.bean.LoginUserBean;

/**
 * 当前应用下文持有器
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月13日 18时24分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public class CurrentContextHolder {
    private static final ThreadLocal<LoginUserBean> localContext = new ThreadLocal<>();

    public static LoginUserBean getUser(){
        return localContext.get();
    }

    public static void setUser(LoginUserBean loginUser){
        localContext.set(loginUser);
    }

    public static void clearContext(){
        localContext.remove();
    }
}
