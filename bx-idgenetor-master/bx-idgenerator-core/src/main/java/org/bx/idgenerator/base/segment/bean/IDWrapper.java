package org.bx.idgenerator.base.segment.bean;

import lombok.Data;

@Data
public class IDWrapper {
    private long minId;
    private long curId;
    private long maxId;
}
