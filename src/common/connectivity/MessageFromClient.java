package common.connectivity;

import java.io.Serializable;

public enum MessageFromClient implements Serializable {
    LOGIN,
    DISCONNECT_CLIENT,
    UPDATE_USER,
    ADD_USER,
    DELETE_USER,
    CHECK_USER_EXISTS,
    REQUEST_TABLE_USER,
    REQUEST_TABLE_ORDER,
    LOGIN_REQUEST;
}
