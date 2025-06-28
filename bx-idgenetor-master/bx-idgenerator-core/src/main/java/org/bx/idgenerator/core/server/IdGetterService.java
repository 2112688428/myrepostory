package org.bx.idgenerator.core.server;


/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 13:53
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public interface IdGetterService {
    /**
     * 获得id
     *
     * @param key
     * @return
     */
    long getId(String key);


}
