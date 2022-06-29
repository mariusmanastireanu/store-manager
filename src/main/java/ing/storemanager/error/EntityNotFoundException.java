package ing.storemanager.error;

import ing.storemanager.utils.ExceptionUtils;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class clazz, String... searchParamsMap) {
        super(clazz.getSimpleName()
                + (searchParamsMap == null || searchParamsMap.length == 0 ? "" : (" was not found for parameters "
                + ExceptionUtils.toMap(String.class, String.class, (Object[]) searchParamsMap))));
    }

}
