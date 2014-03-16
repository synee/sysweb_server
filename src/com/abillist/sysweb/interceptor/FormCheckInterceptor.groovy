package com.abillist.sysweb.interceptor

import com.jfinal.core.ActionInvocation

/**
 * Created by shao on 14-3-15.
 */
class FormCheckInterceptor implements com.jfinal.aop.Interceptor{
    @Override
    void intercept(ActionInvocation ai) {
        ai.invoke()
    }
}
