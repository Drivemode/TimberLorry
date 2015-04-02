package com.drivemode.timberlorry.plug;

import io.realm.RealmObject;

/**
 * @author KeishinYokomaku
 */
public class RecordObject extends RealmObject {
    private String className;
    private String body;

    public void setClassName(String className) {
        this.className = className;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getClassName() {
        return className;
    }

    public String getBody() {
        return body;
    }
}