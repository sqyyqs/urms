package com.sqy.urms.dadataclient.util;

public final class DadataClientUtils {

    private DadataClientUtils() {
        throw new RuntimeException();
    }

    public static String preparePhoneValue(String phone) {
        return "[\"%s\"]".formatted(phone);
    }

}
