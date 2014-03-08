package com.abillist.sysweb.interceptor

/**
 * Created by shao on 14-3-7.
 */
class TestInterceptor implements Interceptor{
    @Override
    Object beforeInvoke(Object o, String s, Object[] objects) {
        return null
    }

    @Override
    Object afterInvoke(Object o, String s, Object[] objects, Object o2) {
        return null
    }

    @Override
    boolean doInvoke() {
        return false
    }
}
