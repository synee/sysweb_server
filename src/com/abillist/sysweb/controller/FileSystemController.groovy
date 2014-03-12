package com.abillist.sysweb.controller

import com.abillist.sysweb.interceptor.SecureInterceptor
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit
import com.jfinal.render.TextRender
import org.apache.commons.io.FileUtils;

import java.nio.file.NoSuchFileException;

@Before(SecureInterceptor.class)
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


    public void ls() {
        File file = new File(getParaPath());
        renderJson(file.listFiles().collect {
            [
                name: it.name,
                directory: it.isDirectory(),
                file: it.isFile(),
                absolutePath: it.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", ""),
                exists: it.exists(),
                parent: it.getParent().replace("$FS_ROOT/${getCurrentUser().getUsername()}", "/").replaceAll("//", "/                              ")
            ]
        });
    }

    public void cd() {
        File file = new File(getParaPath());
        if (!file.exists() || !file.isDirectory()) {
            renderError(404)
            renderJson([
                error: true,
                message: "no such dir"
            ])
        } else {
            renderJson([
                name: file.name,
                directory: file.isDirectory(),
                file: file.isFile(),
                absolutePath: file.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", ""),
                exists: file.exists()
            ])
        }
    }

//    查看文件信息
    public void stat(){
        File file = new File(getParaPath());
        if (!file.exists()) {
            renderError(404)
            renderJson([
                error: true,
                message: "no such dir"
            ])
        }else {

            renderJson([
                name: file.name,
                directory: file.isDirectory(),
                file: file.isFile(),
                absolutePath: file.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", ""),
                exists: file.exists(),
                size: file.length(),
                modify: file.lastModified()
            ])
        }
    }

    public void touch() {
        File file = new File(getParaPath());
        if (file.exists()) {
            renderJson([
                error: true,
                message: "Same named file or dir has exist."
            ])
            return;
        }
        try {
            file.createNewFile();
            renderJson([
                name: file.name,
                directory: file.isDirectory(),
                file: file.isFile(),
                absolutePath: file.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", ""),
                exists: file.exists()
            ]);
        } catch (IOException e) {
            renderError(500, new TextRender(e.getMessage()));
        }
    }

    public void mkdir() {
        File file = new File(getParaPath());
        if (file.exists()) {
            renderJson([
                error: true,
                message: "Same named file or dir has exist"
            ])
        } else {
            file.mkdir();
            renderJson([
                name: file.name,
                directory: file.isDirectory(),
                file: file.isFile(),
                absolutePath: file.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", ""),
                exists: file.exists()
            ]);
        }
    }

    public void isDir() {
        File file = new File(getParaPath())

        renderJson([
            isDir: file.exists() && file.isDirectory()
        ])
    }

    public void mkdirs() {}

    public void rm() {
        File file = new File(getParaPath());
        if (file.exists()) {
            String relativePath = file.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", "")
            if (relativePath == "/__sys.js"){
                renderJson([
                    error: true,
                    message: "This file cannot be removed."
                ])
                return
            }
            if (file.isFile()) file.delete();
            if (file.isDirectory()) file.deleteDir();
            if (file.exists()) {
                renderJson([
                    error: true,
                    message: "Delete Failed."
                ])
            } else {
                renderJson([
                    name: file.name,
                    directory: file.isDirectory(),
                    file: file.isFile(),
                    absolutePath: relativePath,
                    exists: file.exists()
                ])
            }
        } else {
            renderJson([
                error: true,
                message: "No such file or dir."
            ])
        }

    }

    public void cp() {
        File source
        File dest
        try {
            source = new File(getParaPath("source"))
            dest = new File(getParaPath("dest"));
        } catch (NoSuchFileException e) {
            renderJson([
                error: true,
                message: e.getMessage()
            ])
            return
        }
        if (source.exists() && !dest.exists()) {
            if (source.isDirectory()) {
                FileUtils.copyDirectory(source, dest)
            } else if (source.isFile()) {
                FileUtils.copyFile(source, dest)
            }
            renderJson([
                source: [
                    name: source.name,
                    directory: source.isDirectory(),
                    file: source.isFile(),
                    absolutePath: source.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", ""),
                    exists: source.exists()
                ],
                dest: [
                    name: dest.name,
                    directory: dest.isDirectory(),
                    file: dest.isFile(),
                    absolutePath: dest.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", ""),
                    exists: dest.exists()
                ]
            ])
        } else {
            renderError(404)
            renderJson([
                error: true,
                message: "No such source or such dest is exist."
            ])
        }

    }

    public void mv() {
        File source
        File dest
        try {
            source = new File(getParaPath("source"))
            dest = new File(getParaPath("dest"))
            String relativePath = source.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", "")
            if (relativePath == "/__sys.js"){
                renderJson([
                    error: true,
                    message: "This file cannot be removed."
                ])
                return
            }
        } catch (NoSuchFileException e) {
            renderError(404)
            renderJson([
                error: true,
                message: e.getMessage()
            ])
            return
        }
        if (source.exists() && !dest.exists()) {
            if (source.isDirectory()) {
                FileUtils.moveDirectory(source, dest)
            } else if (source.isFile()) {
                FileUtils.moveFile(source, dest)
            }
            renderJson([
                source: [
                    name: source.name,
                    directory: source.isDirectory(),
                    file: source.isFile(),
                    absolutePath: source.absolutePath.replace(FS_ROOT + "/" + getCurrentUser().getUsername(), ""),
                    exists: source.exists()
                ],
                dest: [
                    name: dest.name,
                    directory: dest.isDirectory(),
                    file: dest.isFile(),
                    absolutePath: dest.absolutePath.replace(FS_ROOT + "/" + getCurrentUser().getUsername(), ""),
                    exists: dest.exists()
                ]
            ])
        } else {
            renderError(404)
            renderJson([
                error: true,
                message: "No such source or such dest is exist."
            ])
        }
    }

    public void head() {
        File file = new File(getParaPath("path"))

        if (!file.exists() || !file.isFile()) {
            renderJson([
                error: true,
                message: "file named [${getPara("path")}] not found."
            ])
            return
        }
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
        renderJson([
            name: file.name,
            directory: file.isDirectory(),
            file: file.isFile(),
            absolutePath: file.absolutePath.replace("$FS_ROOT/$USERNAME", ""),
            exists: file.exists(),
            text: buffer.toString()
        ])
    }

    public void tail() {
        File file = new File(getParaPath("path"))
        if (!file.exists() || !file.isFile()) {
            renderJson([
                error: true,
                message: "file named [${getPara("path")}] not found."
            ])
            return
        }
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
        renderJson([
            name: file.name,
            directory: file.isDirectory(),
            file: file.isFile(),
            absolutePath: file.absolutePath.replace("$FS_ROOT/$USERNAME", ""),
            exists: file.exists(),
            text: lines.join("\n")
        ])
    }

    public void read() {

        File file = new File(getParaPath());
        if (file.exists() && file.isFile()) {
            renderJson([
                name: file.name,
                directory: file.isDirectory(),
                file: file.isFile(),
                absolutePath: file.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", ""),
                exists: file.exists(),
                text: file.text
            ])
        } else {
            renderJson([
                error: true,
                message: "No such file."
            ])
        }

    }

    public void write() {

        File file = new File(getParaPath());
        file.write(getPara("text"))
        renderJson([
            name: file.name,
            directory: file.isDirectory(),
            file: file.isFile(),
            absolutePath: file.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", ""),
            exists: file.exists(),
            text: file.text
        ])

    }

    public void append() {
        File file = new File(getParaPath());
        if (file.exists() && file.isFile()) {
            file.append(getPara("text"))
            renderJson([
                name: file.name,
                directory: file.isDirectory(),
                file: file.isFile(),
                absolutePath: file.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", ""),
                exists: file.exists(),
                text: file.text
            ])
        } else {
            renderJson([
                error: true,
                message: "No such file."
            ])
        }
    }

    public void echo() {
        File file = new File(getParaPath());
        if (file.exists() && file.isFile()) {
            file.append(getPara("text") + "\n")
            renderJson([
                name: file.name,
                directory: file.isDirectory(),
                file: file.isFile(),
                absolutePath: file.absolutePath.replace("$FS_ROOT/${getCurrentUser().getUsername()}", ""),
                exists: file.exists(),
                text: file.text
            ])
        } else {
            renderJson([
                error: true,
                message: "No such file."
            ])
        }
    }

}
