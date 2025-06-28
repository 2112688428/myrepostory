package org.bx.idgenerator.base.getter.snowflake.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotifyContext<T> {
    private String msg;
    private T data;
}
