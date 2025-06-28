package org.bx.idgenerator.lifecycle;

import org.bx.idgenerator.exception.BizException;

/**
 * 抽象生命周期，状态不能重复
 *
 * @author bx
 */
public abstract class AbstractGeneratorLifecycle implements IIDGeneratorLifecycle {
    public static final String MSG_FORMAT = "前一个状态必须为: %s,当前为: %s";
    protected GeneratorState state = GeneratorState.DEFAULT_STATE;

    @Override
    public GeneratorState getState() {
        return state;
    }

    @Override
    public void init() {
        if (getState() != GeneratorState.DEFAULT_STATE) {
            throw new BizException(String.format(MSG_FORMAT, GeneratorState.DEFAULT_STATE.value(), getState().value()));
        }
        doInit();
        state = GeneratorState.INIT_STATE;
    }


    @Override
    public void start() {
        if (getState() != GeneratorState.INIT_STATE) {
            throw new BizException(String.format(MSG_FORMAT, GeneratorState.INIT_STATE.value(), getState().value()));
        }
        doStart();
        state = GeneratorState.START_STATE;
    }

    @Override
    public void stop() {
        if (getState() != GeneratorState.START_STATE) {
            throw new BizException(String.format(MSG_FORMAT, GeneratorState.START_STATE.value(), getState().value()));
        }
        doStop();
        state = GeneratorState.STOP_STATE;
    }

    public void doInit() {
    }


    public void doStart() {
    }


    public void doStop() {
    }

}
