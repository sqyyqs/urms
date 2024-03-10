package com.sqy.urms.dto.requestentity;

public enum RequestStatus {

    DRAFT(0), SENT(1), ACCEPTED(2), REJECTED(2);

    private final int sequence;

    RequestStatus(int sequence) {
        this.sequence = sequence;
    }

    public int getSequence() {
        return sequence;
    }
}
