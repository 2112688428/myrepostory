package org.bx.idgenerator.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

/**
 * 配置工厂
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年07月06日 17时18分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
@Deprecated
public class PropertyFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFactory.class);
    private static final Properties prop = new Properties();

    static {
        try {
            prop.load(PropertyFactory.class.getClassLoader().getResourceAsStream("bx-idgenerator.properties"));
        } catch (IOException e) {
            LOGGER.warn("Load Properties Error", e);
        }
    }

    public static Properties getProperties() {
        return prop;
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

    public static Long getLong(String key){
        String property = getProperty(key);
        return Long.parseLong(property);
    }

    public static int getInt(String key){
        String property = getProperty(key);
        return Integer.parseInt(property);
    }

    public static BigDecimal getBigDecimal(String key){
        String property = getProperty(key);
        return new BigDecimal(property);
    }

    public static boolean getBoolean(String key){
        String property = getProperty(key);
        return Boolean.getBoolean(property);
    }

}
