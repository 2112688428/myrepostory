package org.bx.idgenerator.base.segment.bus;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.bx.idgenerator.base.segment.Segment;
import org.bx.idgenerator.base.segment.buffer.SegmentBuffer;
import org.bx.idgenerator.base.segment.entity.LeafInfo;

/**
 * 填充segment事件 监听器
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020年07月06日 11时19分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Slf4j
public class FillSegmentListener {

    /**
     * 填充 nextSegment
     *
     * @param event 消费填充缓冲区事件
     */
    @Subscribe
    public void fill(FillSegmentEvent event) {
        log.info("FillSegmentListener接收到的事件:{}", event);
        SegmentBuffer segmentBuffer = event.getSegmentBuffer();
        Segment segment = null;
        Throwable e = null;
        try {
            //获得节点信息
            LeafInfo leafInfo = event.getLeafInfoService().getLeafInfo(event.getTag());
            log.info("FillSegmentListener  处理结果:{}", leafInfo);
            segment = leafInfoToSegment(leafInfo);
        } catch (Exception exception) {
            e = exception;
            log.error("FillSegmentListener处理出现异常", e);
        }

        segmentBuffer.setNextSegment(segment);
        // 设置发生异常
        segmentBuffer.fillComplete(e);
    }


    private Segment leafInfoToSegment(LeafInfo leafInfo) {
        Segment segment = new Segment();
        long curId = leafInfo.getCurId();
        segment.setCurId(curId);
        segment.setMinId(curId);
        segment.setMaxId(leafInfo.getMaxId());
        return segment;
    }
}
