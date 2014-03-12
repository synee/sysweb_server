package com.abillist.sysweb.interceptor

import com.abillist.sysweb.controller.AbstractController
import com.abillist.sysweb.model.User
import com.jfinal.aop.Interceptor
import com.jfinal.core.ActionInvocation

/**
 * Created by shao on 14-3-7.
 */
class SecureInterceptor implements Interceptor{
    @Override
    void intercept(ActionInvocation ai) {
        User user = (ai.controller as AbstractController).currentUser
        if (user && user.exists()){
            ai.invoke()
        }else {
            ai.controller.renderError(403)
        }
    }
}
