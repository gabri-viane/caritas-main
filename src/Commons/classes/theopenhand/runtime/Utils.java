/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theopenhand.runtime;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabri
 */
public final class Utils {

    public static boolean isPrimitive(Class<?> type) {
        return (type == int.class || type == long.class || type == double.class || type == float.class
                || type == boolean.class || type == byte.class || type == char.class || type == short.class);
    }

    public static Class<?> boxPrimitiveClass(Class<?> type) {
        if (type == int.class) {
            return Integer.class;
        } else if (type == long.class) {
            return Long.class;
        } else if (type == double.class) {
            return Double.class;
        } else if (type == float.class) {
            return Float.class;
        } else if (type == boolean.class) {
            return Boolean.class;
        } else if (type == byte.class) {
            return Byte.class;
        } else if (type == char.class) {
            return Character.class;
        } else if (type == short.class) {
            return Short.class;
        } else {
            String string = "class '" + type.getName() + "' is not a primitive";
            throw new IllegalArgumentException(string);
        }
    }

    public static <T, Y> T castTo(Y o, Class<T> ct) {
        if (isPrimitive(o.getClass())) {
            if (boxPrimitiveClass(o.getClass()).equals(ct)) {
                return ct.cast(o);
            }
        }
        if (ct.equals(int.class) || ct.equals(Integer.class)) {
            return ct.cast(Integer.parseInt(o.toString()));
        } else if (ct.equals(long.class) || ct.equals(Long.class)) {
            return ct.cast(Long.parseLong(o.toString()));
        } else if (ct.equals(boolean.class) || ct.equals(Boolean.class)) {
            return ct.cast(Boolean.getBoolean(o.toString()));
        } else if (ct.equals(Date.class)) {
            Class<?> bpc = boxPrimitiveClass(o.getClass());
            if (bpc.equals(Long.class)) {
                return ct.cast(new Date((Long) o));
            }
            return ct.cast(o);
        } else if (ct.equals(String.class)) {
            return ct.cast(o.toString());
        } else if (ct.equals(BigInteger.class)) {
            if (isPrimitive(o.getClass())) {
                Class<?> bpc = boxPrimitiveClass(o.getClass());
                if (bpc.equals(Long.class)) {
                    return ct.cast(BigInteger.valueOf((Long) o));
                } else if (bpc.equals(Integer.class)) {
                    return ct.cast(BigInteger.valueOf((Integer) (o)));
                }
            }
            return ct.cast(BigInteger.valueOf(Long.parseLong(o.toString())));
        }
        return null;
    }

    public static boolean contains(int[] arr, int elem) {
        for (int el : arr) {
            if (elem == el) {
                return true;
            }
        }
        return false;
    }

    public static <T> T newInstance(Class<T> ct) {
        try {
            Constructor<T> constructor = ct.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
