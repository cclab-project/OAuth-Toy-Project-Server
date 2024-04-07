package com.cclab.oauth.commom.error;

public interface ErrorCodeIfs {

    Integer getHttpStatusCode();
    Integer getErrorCode();
    String getErrorDescription();
}
