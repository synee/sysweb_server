package com.abillist.sysweb.controller

import com.abillist.sysweb.model.User
import com.jfinal.aop.Before
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller
import com.jfinal.ext.interceptor.SessionInViewInterceptor;

@Before(SessionInViewInterceptor.class)
public class HomeController extends AbstractController{
    public void index(){
        render("/index.ftl");
    }

}
