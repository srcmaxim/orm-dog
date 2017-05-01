package org.dog.util;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.dog.util.Primitives.wrap;

public final class Reflection {

    private Reflection() {
    }

    public static Method getSetXXXMethod(Class<?> clazz, Field field) throws NoSuchMethodException {
        String pfn = wrap(field.getType()).getSimpleName();
        String methodName = "set" + firstUppercase(pfn);
        return clazz.getDeclaredMethod(methodName, int.class, wrap(field.getType()));
    }

    public static Method getGetXXXMethod(Class<?> clazz, Field field) throws NoSuchMethodException {
        String pfn = wrap(field.getType()).getSimpleName();
        String methodName = "get" + firstUppercase(pfn);
        return clazz.getDeclaredMethod(methodName, int.class);
    }

    private static String firstUppercase(String pfn) {
        return Character.toUpperCase(pfn.charAt(0)) + pfn.substring(1);
    }

    public static int setXXXToStatement(Object t, PreparedStatement ps)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class psClass = ps.getClass().getSuperclass();
        Field[] fields = t.getClass().getDeclaredFields();
        int i = 1;
        Field id = getIdField(t);
        for (Field field : fields) {
            if (field.equals(id)) {
                continue;
            }
            Class<?> type = wrap(field.getType());
            Method method = getSetXXXMethod(psClass, field);
            Object data = field.get(t);
            invokeMethod(ps, i++, method, type, data);
        }
        return i;
    }

    @SuppressWarnings("unchecked")
    private static <T> void invokeMethod(
            PreparedStatement ps, int i, Method method, Class<T> type, Object objectData)
            throws InvocationTargetException, IllegalAccessException {
        T data = (T) objectData;
        method.invoke(ps, i, data);
    }

    public static Field getIdField(Object t) {
        Field idField = null;
        Field[] fields = t.getClass().getFields();
        for (Field field : fields) {
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                idField = field;
                break;
            }
        }
        return idField;
    }

    public static void setFields(Object t, ResultSet trs)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int i = 1;
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            Class<?> superclass = trs.getClass().getSuperclass();
            Method m = getGetXXXMethod(superclass, field);
            Object fieldData = m.invoke(trs, i++);
            field.set(t, fieldData);
        }
    }
}
