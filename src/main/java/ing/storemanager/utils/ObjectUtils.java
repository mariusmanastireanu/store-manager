package ing.storemanager.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectUtils {

    public static <T> T firstNotNull(T first, T second) {
        if (first != null) {
            return first;
        }
        return second;
    }
}
