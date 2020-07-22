package org.bigbee.zdm.annotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

@Component
public class AAA {

    /**
     * https://blog.csdn.net/qq_38366063/article/details/97382500
     * https://www.cnblogs.com/chen-lhx/p/5853947.html
     *  ^-^ https://blog.51cto.com/lavasoft/61002
     *
     * https://www.cnblogs.com/yufeng218/p/9911992.html
     */


    public AAA(){

        System.out.println("此时b还未被注入: b = " );
    }

    @PostConstruct
    private void init(){
        System.out.println("@PostConstruct将在依赖注入完成后被自动的调用:b=");
    }


    public static void main(String[] args) throws Exception {

        Class.forName("org.bigbee.zdm.annotations.AAA");
//        Class<?> clazz = AAA.class;
//        Method m1 = clazz.getDeclaredMethod("init");
//
//        m1.invoke(clazz);



        AAA a = new AAA();
        Class clz = a.getClass();
        Method et = clz.getDeclaredMethod("init");
        //et.invoke(clz);
        et.invoke(a);
    }



//    public static BasePayParamsInfo getPayInstance(String keytype, String payId,  Map<String, String> paramMap) {
//        Class<?> obj = null;
//        BasePayParamsInfo d = null;
//        try {
//            obj = Class.forName("com.dubbo.zookeeper.pay." + payId);
//            Class<?>[] params = { String.class, Map.class };
//            Object[] values = {keytype,  paramMap };
//            Constructor<?> constructor = obj.getDeclaredConstructor(params);
//            d = (BasePayParamsInfo) constructor.newInstance(values);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return d;
//    }

}


