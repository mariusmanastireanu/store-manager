package ing.storemanager.error;

import ing.storemanager.utils.ExceptionUtils;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message, String... searchParamsMap) {
        super(message
                + (searchParamsMap == null || searchParamsMap.length == 0 ? "" : (" Parameters "
                + ExceptionUtils.toMap(String.class, String.class, (Object[]) searchParamsMap))));
    }


}