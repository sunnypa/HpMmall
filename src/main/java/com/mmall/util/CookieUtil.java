package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
public class CookieUtil {
    private final static String COOKIE_DOMAIN = ".happymmall.com";
    private final static String COOKIE_NAME = "mmall_login_token";
    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            for (Cookie cookie:cookies
                 ) {
                log.info("cookiename:{},cookievalue:{}",cookie.getName(),cookie.getValue());
                if (StringUtils.equals(cookie.getName(),COOKIE_NAME)){
                    log.info("cookiename:{},cookievalue:{}",cookie.getName(),cookie.getValue());
                    return cookie.getValue();

                }
            }
        }
        return null;
    }
    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie ck = new Cookie(COOKIE_NAME,token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");
        ck.setHttpOnly(true);
        //单位是秒
        //如果这个maxage不设置的话，cookie就不会写入硬盘，而是写在内存，只在当前页面有效
        ck.setMaxAge(60*60*24*365);
        log.info("write cookiename:{},cookievalue:{}",ck.getName(),ck.getValue());
        response.addCookie(ck);

    }
    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            for (Cookie cookie:cookies
                 ) {
                if (StringUtils.equals(cookie.getName(),COOKIE_NAME))
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);//设置成零 代表删除此cookie
                    log.info("cookiename:{},cookievalue:{}",cookie.getName(),cookie.getValue());
                    response.addCookie(cookie);
                    return;

            }
        }

    }
}
