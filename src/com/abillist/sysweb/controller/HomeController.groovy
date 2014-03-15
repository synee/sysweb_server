package com.abillist.sysweb.controller

import com.jfinal.aop.Before
import com.jfinal.ext.interceptor.SessionInViewInterceptor;

@Before(SessionInViewInterceptor.class)
public class HomeController extends AbstractController{
    public void index(){
        render("/index.ftl");
    }
}
