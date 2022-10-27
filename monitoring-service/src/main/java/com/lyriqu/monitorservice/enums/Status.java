package com.lyriqu.monitorservice.enums;

public enum Status {
    DENIED("Denied, see reason"),
    WAITING("Under review"),
    SUCCESS("Success, song is listed");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
