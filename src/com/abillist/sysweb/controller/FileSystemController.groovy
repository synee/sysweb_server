package com.abillist.sysweb.controller


import com.abillist.sysweb.interceptor.SecureInterceptor
import com.jfinal.aop.Before
import com.jfinal.core.ActionInvocation
import org.apache.commons.io.FileUtils

import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target;

@Before([SecureInterceptor.class, PathCheckInterceptor.class])
public class FileSystemController extends AbstractController {


    public void index() {
        String path = getPara("path");
        if (path == null) {
            path = "/";
        } else if (!path.startsWith("/")) {
            path = "/" + path;
        }
        File file = new File(getMyDir() + path);
        renderText(String.valueOf(file.exists()));
    }

    private String getAbsolutePath(String abpath) {
        return abpath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", "")
    }

    def fileToInfo(File file) {
        [
            name: file.name,
            directory: file.isDirectory(),
            file: file.isFile(),
            absolutePath: this.getAbsolutePath(file.absolutePath),
            exists: file.exists(),
            parent: file.getParent().replace("$FS_ROOT/${getCurrentUser().getUsername()}", "/").replaceAll("//", "/"),
            modify: file.lastModified()
        ]
    }


    @PathCheck()
    public void ls() {
        File file = new File(getParaPath());
        renderJson(file.listFiles().collect { this.fileToInfo(it) });
    }

    @PathCheck(beDirectory = ["path"])
    public void cd() {
        renderJson(this.fileToInfo(new File(getParaPath())))
    }

//    查看文件信息
    @PathCheck()
    public void stat() {
        File file = new File(getParaPath());
        renderJson(this.fileToInfo(file))
    }

    @PathCheck(exists = [], notExists = ["path"])
    public void touch() {
        File file = new File(getParaPath());
        file.createNewFile();
        renderJson(this.fileToInfo(file));
    }


    @PathCheck(exists = [], notExists = ["path"])
    public void mkdir() {
        File file = new File(getParaPath());
        file.mkdirs()
        renderJson(this.fileToInfo(file))
    }

    @PathCheck(beDirectory = ["path"])
    public void isDir() {
        File file = new File(getParaPath())
        renderJson([error: true, isDir: file.exists() && file.isDirectory()])
    }

    @PathCheck()
    public void rm() {
        File file = new File(getParaPath());
        String relativePath = file.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", "")
        if (relativePath == "/__sys.js") {
            renderError400WithMessage("This file cannot be removed.");
        } else {
            if (file.isFile()) file.delete();
            if (file.isDirectory()) file.deleteDir();
            if (file.exists()) {
                renderErrorWithMessage(500, "Remove failed.");
            } else {
                renderJson(this.fileToInfo(file))
            }
        }
    }

    @PathCheck(exists = ["source"], notExists = ["dest"])
    public void cp() {
        File source = new File(getParaPath("source"))
        File dest = new File(getParaPath("dest"));
        if (source.isDirectory()) {
            FileUtils.copyDirectory(source, dest)
        } else if (source.isFile()) {
            FileUtils.copyFile(source, dest)
        }
        renderJson([
            source: this.fileToInfo(source),
            dest: this.fileToInfo(dest)
        ])

    }

    @PathCheck(exists = ["source"], notExists = ["dest"])
    public void mv() {
        File source = new File(getParaPath("source"))
        File dest = new File(getParaPath("dest"));
        String relativePath = source.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", "")
        if (relativePath == "/__sys.js") {
            renderError400WithMessage("This file cannot be removed.");
        } else {
            if (source.isDirectory()) {
                FileUtils.moveDirectory(source, dest)
            } else if (source.isFile()) {
                FileUtils.moveFile(source, dest)
            }
            renderJson([
                source: this.fileToInfo(source),
                dest: this.fileToInfo(dest)
            ])
        }
    }

    @PathCheck(beFile = ["path"])
    public void head() {
        File file = new File(getParaPath("path"))
        int start = getPara("start") ? getParaToInt("start") : 0
        int stop = getPara("stop") ? getParaToInt("stop") : 10
        BufferedReader reader = new BufferedReader(new FileReader(file))
        StringBuffer buffer = new StringBuffer()
        String line
        for (int i; (line = reader.readLine()) != null && i < stop; i++) {
            if (i >= start) {
                buffer.append(line).append("\n")
            }
        }
        def jsonFile = this.fileToInfo(file)
        jsonFile.text = buffer.toString()
        renderJson(jsonFile)
    }

    @PathCheck(beFile = ["path"])
    public void tail() {
        File file = new File(getParaPath("path"))
        int start = getPara("start") ? getParaToInt("start") : 0
        int stop = getPara("stop") ? getParaToInt("stop") : 10
        BufferedReader reader = new BufferedReader(new FileReader(file))
        String line
        def lines = []
        def len = stop - start
        while ((line = reader.readLine()) != null) {
            lines.push(line)
            if (lines.size() > len) {
                lines.pop()
            }
        }
        def jsonFile = this.fileToInfo(file)
        jsonFile.text = lines.join("\n")
        renderJson(jsonFile)
    }

    @PathCheck(beFile = ["path"])
    public void read() {
        File file = new File(getParaPath());
        def jsonFile = this.fileToInfo(file)
        jsonFile.text = file.text
        renderJson(jsonFile)
    }

    @PathCheck(beFile = ["path"])
    public void write() {
        File file = new File(getParaPath());
        file.write(getPara("text"))
        def jsonFile = this.fileToInfo(file)
        jsonFile.text = file.text
        renderJson(jsonFile)
    }

    @PathCheck(beFile = ["path"])
    public void append() {
        File file = new File(getParaPath());
        file.append(getPara("text"))
        def jsonFile = this.fileToInfo(file)
        jsonFile.text = file.text
        renderJson(jsonFile)
    }

    @PathCheck(beFile = ["path"])
    public void echo() {
        File file = new File(getParaPath());
        file.append(getPara("text") + "\n")
        def jsonFile = this.fileToInfo(file)
        jsonFile.text = file.text
        renderJson(jsonFile)
    }

    public static class PathCheckInterceptor implements com.jfinal.aop.Interceptor {
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
}


@Retention(RetentionPolicy.RUNTIME)
@Target([ElementType.TYPE, ElementType.METHOD])
@Documented
@interface PathCheck {
    String[] exists() default ["path"];

    String[] notExists() default [];

    String[] beFile() default [];

    String[] beDirectory() default [];
}
