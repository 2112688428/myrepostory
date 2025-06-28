package org.bx.idgenerator.base.segment.bus;

/**
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月09日 15时54分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public interface Event {
    /**
     * 事件标识
     *
     * @return 返回事件标识
     */
    String getIdentity();
}
