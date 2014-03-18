package com.abillist.sysweb.model

import com.jfinal.plugin.activerecord.Model

/**
 * Created by shao on 14-3-3.
 */
abstract class AbstractModel<M extends Model> extends Model<M> {
    public boolean save() {
        if (this.getInt("id") && this.getInt("id") > 0) {
            return super.update()
        } else {
            return super.save()
        }
    }
}
