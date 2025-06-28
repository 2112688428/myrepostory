package org.bx.idgenerator.base.segment.bus;

import lombok.Data;
import org.bx.idgenerator.base.segment.ILeafInfoService;
import org.bx.idgenerator.base.segment.buffer.SegmentBuffer;

/**
 * 填充segment事件
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020年07月06日 11时19分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Data
public class FillSegmentEvent {
    /**
     * 段缓存
     */
    private SegmentBuffer segmentBuffer;
    /**
     * 节点信息相关服务
     */
    private ILeafInfoService leafInfoService;
    /**
     * ???
     */
    private int num;
    /**
     * 业务主键
     */
    private String tag;

    public FillSegmentEvent(SegmentBuffer segmentBuffer, ILeafInfoService leafInfoService, int num, String tag) {
        this.segmentBuffer = segmentBuffer;
        this.leafInfoService = leafInfoService;
        this.num = num;
        this.tag = tag;
    }
}
