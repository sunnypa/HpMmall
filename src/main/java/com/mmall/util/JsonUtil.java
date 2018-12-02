package com.mmall.util;

import com.google.common.collect.Lists;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        //取消默认转换的timestamp形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空bean转Json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        //所有的日期格式都统一为一下的样式
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
        //忽略在Json字符串中存在，但是在Java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }
    public static <T>String obj2String(T obj){
        if (obj==null){
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse object to String error",e);
            return null;
        }
    }
    public static <T> String obj2Stringpretty(T obj){
        if (obj==null){
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);

        } catch (Exception e) {
            log.warn("Parse object to String error",e);
            return null;
        }
    }
    public static <T> T string2Obj(String str,Class<T> clazz){
        if (StringUtils.isEmpty(str)||clazz==null){
            return null;
        }
        try {
            return clazz.equals(String.class)?(T)str:objectMapper.readValue(str,clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }

    }

    /**
     * 针对Map<List<User>,String>这样的对象反序列化的方法
     * @param str
     * @param reference
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, TypeReference<T> reference){
            if (StringUtils.isEmpty(str)||reference == null){
                return null;
            }
        try {
            return  (T) (reference.getType().equals(String.class)?str:objectMapper.readValue(str,reference));
        } catch (IOException e) {
            log.error("shibai",e.getMessage());
        }
        return null;
    }
    public static <T> T string2Obj(String str,Class<?> collectionClass,Class<?>... elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);

        try {
            return  objectMapper.readValue(str,javaType);
        } catch (IOException e) {
            log.error("Parse String to Object Error",e.getMessage());
            return null;
        }

    }


    public static void main(String[] args) {
        User u1 = new User();
        User u2 = new User();
        u1.setId(1);
        u1.setEmail("sunkai.aa.com");
        String userjson = obj2String(u1);
        String userjsonPretty = obj2Stringpretty(u1);
        log.info("userjson:{}",userjson);
        log.info("userjsonPretty:{}",userjsonPretty);
        List<User> userList = Lists.newArrayList();
        userList.add(u1);
        userList.add(u2);
        String userliststr = JsonUtil.obj2String(userList);
        log.info(userliststr);

        List<User> userListObj = JsonUtil.string2Obj(userliststr, new TypeReference<List<User>>() {
        });
        List<User> userListobj = JsonUtil.string2Obj(userliststr,List.class,User.class);

        System.out.println("end");




    }
}
