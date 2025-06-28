package org.bx.idgenerator.lifecycle;

/**
 * 统一组件生命周期接口
 *
 * @author bx
 */
public interface IIDGeneratorLifecycle {
    /**
     * 生命周期状态--
     */
    enum GeneratorState {
        STOP_STATE("停止状态", 3),
        START_STATE("开始状态", 2),
        INIT_STATE("初始状态", 1),
        DEFAULT_STATE("默认状态", 0);

        public String value() {
            return value;
        }

        public int state() {
            return state;
        }

        private String value;
        private int state;

        GeneratorState(String value, int state) {
            this.value = value;
            this.state = state;
        }
    }

    /**
     * 获取当前组件状态
     *
     * @return
     */
    GeneratorState getState();

    /**
     * 初始化
     */
    void init();

    /**
     * 开始
     */
    void start();

    /**
     * 停止
     */
    void stop();

    /**
     * 组件名
     *
     * @return
     */
    String name();

}
