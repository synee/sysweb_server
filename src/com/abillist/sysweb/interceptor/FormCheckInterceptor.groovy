package com.abillist.sysweb.interceptor

import com.abillist.sysweb.interceptor.anno.Form
import com.jfinal.core.ActionInvocation

/**
 * Created by shao on 14-3-15.
 */
class FormCheckInterceptor implements com.jfinal.aop.Interceptor{
    @Override
    void intercept(ActionInvocation ai) {
        Form form = ai.method.getAnnotation(Form.class)
        ai.invoke()
    }
}
