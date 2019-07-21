package com.github.pheerathach;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class ThaiQRPromptPayTest {

    @Test
    public void testCreditTransferMobileNumberWithAmount() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("100"))
                .build();
        Assert.assertEquals(qr.generateContent(), "00020101021129370016A0000006770101110113006600000000053037645406100.005802TH6304D19E");
    }

    @Test
    public void testCreditTransferMobileNumberWithoutAmount() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .build();
        Assert.assertEquals(qr.generateContent(), "00020101021129370016A0000006770101110113006600000000053037645802TH630456EA");
    }

    @Test
    public void testCreditTransferMobileNumberWithAmountZero() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("0"))
                .build();
        Assert.assertEquals(qr.generateContent(), "00020101021129370016A00000067701011101130066000000000530376454040.005802TH6304FB56");
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferMobileNumberWithAmountBelowZero1() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("-0.01"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferMobileNumberWithAmountBelowZero2() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("-10000000"))
                .build();
    }

}
