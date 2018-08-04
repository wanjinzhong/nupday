package com.nupday.bo;

import com.nupday.constant.CommentTargetType;

/**
 * CommentObject
 * @author Neil Wan
 * @create 18-8-4
 */
public class CommentObject {
    private CommentTargetType targetType;
    private Object target;

    public CommentObject() {
    }

    public CommentObject(CommentTargetType targetType, Object target) {
        this.targetType = targetType;
        this.target = target;
    }

    public CommentTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(CommentTargetType targetType) {
        this.targetType = targetType;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
