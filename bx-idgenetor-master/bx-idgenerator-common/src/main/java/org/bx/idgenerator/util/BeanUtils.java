package org.bx.idgenerator.util;

import net.sf.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean 工具类
 * @author 马士兵 · 项目架构部
 * @version V1.0
 * @contact zeroming@163.com
 * @date: 2020年06月09日 14时51分
 * @company 马士兵（北京）教育科技有限公司 (http://www.mashibing.com/)
 * @copyright 马士兵（北京）教育科技有限公司 · 项目架构部
 */
public class BeanUtils<S,T>{

    public static Map<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<>();

    /**
     * 转换单个实体
     * @param source
     * @param modelClass
     * @param <S>
     * @param <T>
     * @return
     */
    public static<S,T>  T  copyBean(S source,Class<T> modelClass) throws IllegalAccessException, InstantiationException {
        if(source == null){
            return null;
        }
        String beanKey = generateKey(source.getClass(),modelClass);
        BeanCopier copier = null;
        if (!beanCopierMap.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), modelClass,false);
            beanCopierMap.put(beanKey, copier);
        }else {
            copier = beanCopierMap.get(beanKey);
        }
        Object model = modelClass.newInstance();
        copier.copy(source,model,null);
        return (T)model;
    }

    public static<S,T>  T  copyBeanNoException(S source,Class<T> modelClass) {
        Object model = null;
        try {
            model = copyBean(source,modelClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return (T)model;
    }


    /**
     * 复制对象
     * @param source
     * @param to
     * @param <S>
     * @param <T>
     * @return
     */
//    public static<S,T>  T  copyBean(S source,T to) {
//        if(source == null){
//            return null;
//        }
//        Class<?> sClass = source.getClass();
//        Class<?> tClass = to.getClass();
//
//        String beanKey = generateKey(source.getClass(),to.getClass());
//        BeanCopier copier = null;
//        if (!beanCopierMap.containsKey(beanKey)) {
//            copier = BeanCopier.create(sClass, tClass,false);
//            beanCopierMap.put(beanKey, copier);
//        }else {
//            copier = beanCopierMap.get(beanKey);
//        }
//        copier.copy(source,to,null);
//        return to;
//    }


    /**
     * List集合对象转换
     * @param fromList
     * @param tClass
     * @param <F>
     * @param <T>
     * @return
     */
    public static<F,T> List<T> copyListBean(List<F> fromList, Class<T> tClass) throws InstantiationException, IllegalAccessException {
        if(fromList.isEmpty() || fromList == null){
            return null;
        }
        List<T> tList = new ArrayList<>();
        for(F f : fromList){
            T t = copyBean(f, tClass);
            tList.add(t);
        }
        return tList;
    }


    private static String generateKey(Class<?>class1,Class<?>class2){
        return class1.toString() + class2.toString();
    }

}

