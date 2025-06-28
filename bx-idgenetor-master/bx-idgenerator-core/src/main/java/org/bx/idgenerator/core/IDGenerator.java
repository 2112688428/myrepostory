package org.bx.idgenerator.core;

import org.bx.idgenerator.lifecycle.IIDGeneratorLifecycle;

/**
 * ID生成接口
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月06日 12时28分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public interface IDGenerator extends IIDGeneratorLifecycle {

    /**
     * 生成ID
     *
     * @param bizTag
     * @return
     */
    long genId(String bizTag);
}
