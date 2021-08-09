package closer.exceptions;

import closer.main.VCSType;

import java.util.Arrays;

/**
 * @author : Jordan
 * @created : 04/01/2021, Monday
 * @description : This is the description
 **/
public enum CloserErrorCode implements Error
{
    CLOSER_TYPE_PARSING_ERROR(1002, "Type argument does not match one of the supported VCS types. Supported types are "+ Arrays.toString(VCSType.values())),
    INTERNAL_CLOSER_EXCEPTION(1007, "Internal Error Occurred, Please contact tool owner for assistance."),
    DATE_PARSING_ERROR(1008, "Date format in VCS logging output does not meet the required format.");


    private final int errorCode;
    private final String errorReason;

    CloserErrorCode(int errorCode, String errorReason) {
        this.errorCode = errorCode;
        this.errorReason = errorReason;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "{errorCode=" + errorCode + ", errorReason='" + errorReason + "'}";
    }
}

