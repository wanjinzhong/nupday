//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.nupday.util;

/**
 * ResponseHelper
 * @author Neil Wan
 * @create 18-8-4
 */
public class ResponseHelper {
    public ResponseHelper() {
    }

    public static <T> JsonEntity<T> createInstance(T object) {
        JsonEntity<T> response = new JsonEntity(object);
        return response;
    }

    public static <T> JsonEntity<T> of(T object) {
        return createInstance(object);
    }

    public static <T> JsonEntity<T> ofNothing() {
        return createInstance(null);
    }
}
