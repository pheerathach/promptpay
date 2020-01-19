package com.github.pheerathach;

import java.math.BigDecimal;

class Constants {

    protected static final String PAYLOAD_FORMAT_INDICATOR = "01";
    protected static final String STATIC_QR_CODE = "11";
    protected static final String DYNAMIC_QR_CODE = "12";
    protected static final String DEFAULT_CURRENCY_CODE = "764";
    protected static final String DEFAULT_COUNTRY_CODE = "TH";
    protected static final String DEFAULT_COUNTRY_CODE_TEL = "66";
    protected static final int CREDIT_TRANSFER_DATA_FIELD_ID = 29;
    protected static final String CREDIT_TRANSFER_ACQUIRER_ID = "A000000677010111";
    protected static final int BILL_PAYMENT_DATA_FIELD_ID = 30;
    protected static final String BILL_PAYMENT_DATA_ACQUIRER_ID = "A000000677010112";
    protected static final BigDecimal oneHundred = new BigDecimal("100.00");

    private Constants() {

    }
}
