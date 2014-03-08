package com.abillist.sysweb.model

import static com.abillist.sysweb.model.Boot.*

/**
 * Created by shao on 14-3-3.
 */
class Boot extends AbstractModel<Boot>{
    public static Boot dao = new Boot()

    private int id
    private User user
    private String tag
    private String path
    private String attrName
    private String otherAttrs

    User getUser() {
        return user
    }

    void setUser(User user) {
        this.set("user_id", user.getId())
        this.user = user
    }

    String getTag() {
        return tag
    }

    void setTag(String tag) {
        this.tag = tag
    }

    String getPath() {
        this.path = getStr("path")
        return this.path
    }

    void setPath(String path) {
        this.path = path
    }

    String getAttrName() {
        return attrName
    }

    void setAttrName(String attrName) {
        this.attrName = attrName
    }

    String getOtherAttrs() {
        return otherAttrs
    }

    void setOtherAttrs(String otherAttrs) {
        this.otherAttrs = otherAttrs
    }

    public List<Boot> findByUser(User master){
        if (!master || !master.getId())
            return []
        else
            dao.find("SELECT * FROM boot WHERE boot.user_id=?", master.getId())
    }
}
