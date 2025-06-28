package org.bx.test;

import org.bx.idgenerator.base.segment.ILeafInfoService;
import org.bx.idgenerator.base.segment.entity.LeafInfo;

import java.util.HashMap;

public class TestLeafInfoService implements ILeafInfoService {
    HashMap<String, Long> map = new HashMap<>(8);

    @Override
    public synchronized LeafInfo getLeafInfo(String tag) {
        if (map.get(tag) == null) {
            map.put(tag, 0L);
        }
        LeafInfo leafInfo = new LeafInfo();
        leafInfo.setCurId(map.get(tag));
        map.put(tag, map.get(tag));
        leafInfo.setMaxId(map.get(tag));
        return leafInfo;
    }
}