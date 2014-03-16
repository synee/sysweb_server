package com.abillist.sysweb.interceptor

import com.abillist.sysweb.controller.FileSystemController
import com.abillist.sysweb.interceptor.anno.PathCheck
import com.jfinal.core.ActionInvocation

/**
 * Created by shao on 14-3-15.
 */
public class PathCheckInterceptor implements com.jfinal.aop.Interceptor {
    @Override
    void intercept(ActionInvocation ai) {
        boolean allRight = true
        FileSystemController controller = ai.controller as FileSystemController
        PathCheck check = ai.method.getAnnotation(PathCheck.class)
        if (check) {
            def existsRight = [true]
            def notExistsRight = [true]
            def beFileRight = [true]
            def beDireRight = [true]
            def file
            check.exists().each {
                file = new File(controller.getParaPath(it))
                if (!file.exists()) {
                    existsRight = [false, controller.getAbsolutePath(file.absolutePath)]
                }
            }
            check.notExists().each {
                file = new File(controller.getParaPath(it))
                if (file.exists()) {
                    notExistsRight = [false, controller.getAbsolutePath(file.absolutePath)]
                }
            }
            check.beFile().each {
                file = new File(controller.getParaPath(it))
                if (file.isFile()) {
                    beFileRight = [false, controller.getAbsolutePath(file.absolutePath)]
                }
            }
            check.beDirectory().each {
                file = new File(controller.getParaPath(it))
                if (file.isDirectory()) {
                    beFileRight = [false, controller.getAbsolutePath(file.absolutePath)]
                }
            }
            allRight = existsRight[0] && notExistsRight[0] && beFileRight[0] && beDireRight[0]
            if (!allRight) {
                def msg = ""
                if (!existsRight[0]) {
                    msg += "${existsRight[1]} is not exist. "
                }
                if (!notExistsRight[0]) {
                    msg += "${notExistsRight[1]} has already been exist. "
                }
                if (!beFileRight[0]) {
                    msg += "${beFileRight[1]} is not a file. "
                }
                if (!beDireRight[0]) {
                    msg += "${beDireRight[1]} is not a directory. "
                }
                controller.renderError400WithMessage(msg)
            }
        }
        if (allRight) {
            ai.invoke()
        }
    }
}

