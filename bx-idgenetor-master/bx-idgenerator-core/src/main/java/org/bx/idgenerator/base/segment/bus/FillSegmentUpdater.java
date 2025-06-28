package org.bx.idgenerator.base.segment.bus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 填充segment事件 动作类
 *
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @date: 2020年07月06日 11时19分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public class FillSegmentUpdater {

    private final EventBus eventBus = new AsyncEventBus("Segment-Updater-Event-Bus", new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadPoolExecutor.CallerRunsPolicy()));

    public FillSegmentUpdater() {
        // 将事件注册到事件总线中
        eventBus.register(new FillSegmentListener());
    }

    /**
     * 发送事件
     * @param event
     */
    public void notifyFill(FillSegmentEvent event) {
        eventBus.post(event);
    }
}
