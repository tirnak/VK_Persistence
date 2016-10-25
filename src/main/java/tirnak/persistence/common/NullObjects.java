package tirnak.persistence.common;

import com.google.common.collect.ImmutableSet;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class NullObjects {
    private static Object[] EMPTY_ARRAY = new Object[0];
    private static List EMPTY_LIST = Collections.unmodifiableList(Collections.emptyList());
    private static Set EMPTY_SET = ImmutableSet.of();
    private static String EMPTY_STRING = "";

    public static <E> E[] getEmptyArray() {
        return (E[]) EMPTY_ARRAY;
    }

    public static <E> E getEmptySet() {
        return (E) EMPTY_SET;
    }

    public static <E> E getEmptyList() {
        return (E) EMPTY_LIST;
    }

    public static String getEmptyString() {
        return EMPTY_STRING;
    }

    public static <E> List<E> returnListOrEmpty(List<E> list) {
        if (list == null) {
            return EMPTY_LIST;
        } else {
            return list;
        }
    }


}
