package com.github.pheerathach;

import com.google.zxing.WriterException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

public class ThaiQRPromptPayTest {

    // Credit Transfer
    // Static QR
    // with Mobile Number
    @Test(expected = IllegalStateException.class)
    public void testCreditTransferStaticQRMobileNumberWithCurrencyCodeNotANumericAndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .currencyCode("ABC")
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferStaticQRMobileNumberWithCurrencyCodeMoreThanThreeDigitsAndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .currencyCode("21211")
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferStaticQRMobileNumberWithCountryCodeMoreThanThreeCharsAndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .countryCode("ABC")
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test
    public void testCreditTransferStaticQRMobileNumberWithCurrencyCodeAndAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .currencyCode("777")
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("100"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29370016A00000067701011101130066000000000", result.substring(12, 53));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303777", result.substring(53, 60));
        // Assert Transaction Amount
        Assert.assertEquals("5406100.00", result.substring(60, 70));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(70, 76));
        // Assert Checksum
        Assert.assertEquals("6304D27D", result.substring(76, 84));
    }

    @Test
    public void testCreditTransferStaticQRMobileNumberWithCountryCodeAndAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .countryCode("AB")
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("100"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29370016A00000067701011101130066000000000", result.substring(12, 53));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(53, 60));
        // Assert Transaction Amount
        Assert.assertEquals("5406100.00", result.substring(60, 70));
        // Assert Country Code
        Assert.assertEquals("5802AB", result.substring(70, 76));
        // Assert Checksum
        Assert.assertEquals("6304CEB5", result.substring(76, 84));
    }

    @Test
    public void testCreditTransferStaticQRMobileNumberWithAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("100"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29370016A00000067701011101130066000000000", result.substring(12, 53));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(53, 60));
        // Assert Transaction Amount
        Assert.assertEquals("5406100.00", result.substring(60, 70));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(70, 76));
        // Assert Checksum
        Assert.assertEquals("6304D19E", result.substring(76, 84));
    }

    @Test
    public void testCreditTransferStaticQRMobileNumberWithoutAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29370016A00000067701011101130066000000000", result.substring(12, 53));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(53, 60));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(60, 66));
        // Assert Checksum
        Assert.assertEquals("630456EA", result.substring(66, 74));
    }

    @Test
    public void testCreditTransferStaticQRMobileNumberWithAmountZero_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("0"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29370016A00000067701011101130066000000000", result.substring(12, 53));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(53, 60));
        // Assert Transaction Amount
        Assert.assertEquals("54040.00", result.substring(60, 68));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(68, 74));
        // Assert Checksum
        Assert.assertEquals("6304FB56", result.substring(74, 82));
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferStaticQRMobileNumberWithAmountThreeDecimalPlace_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("100.365"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferStaticQRMobileNumberWithAmountBelowZero1_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("-0.01"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferStaticQRMobileNumberWithAmountBelowZero2_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("-10000000"))
                .build();
    }

    // Credit Transfer
    // Dynamic QR
    // with Mobile Number
    @Test
    public void testCreditTransferDynamicQRMobileNumberWithAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("100"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29370016A00000067701011101130066000000000", result.substring(12, 53));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(53, 60));
        // Assert Transaction Amount
        Assert.assertEquals("5406100.00", result.substring(60, 70));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(70, 76));
        // Assert Checksum
        Assert.assertEquals("630471B5", result.substring(76, 84));
    }

    @Test
    public void testCreditTransferDynamicQRMobileNumberWithoutAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29370016A00000067701011101130066000000000", result.substring(12, 53));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(53, 60));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(60, 66));
        // Assert Checksum
        Assert.assertEquals("6304A929", result.substring(66, 74));
    }

    @Test
    public void testCreditTransferDynamicQRMobileNumberWithAmountZero_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("0"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29370016A00000067701011101130066000000000", result.substring(12, 53));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(53, 60));
        // Assert Transaction Amount
        Assert.assertEquals("54040.00", result.substring(60, 68));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(68, 74));
        // Assert Checksum
        Assert.assertEquals("6304C18E", result.substring(74, 82));
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferDynamicQRMobileNumberWithAmountThreeDecimalPlace_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("100.365"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferDynamicQRMobileNumberWithAmountBelowZero1_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("-0.01"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferDynamicQRMobileNumberWithAmountBelowZero2_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("-10000000"))
                .build();
    }

    // Credit Transfer
    // Static QR
    // with National Id
    @Test
    public void testCreditTransferStaticQRNationalIdWithAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .nationalId("0000000000001")
                .amount(new BigDecimal("100"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29370016A00000067701011102130000000000001", result.substring(12, 53));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(53, 60));
        // Assert Transaction Amount
        Assert.assertEquals("5406100.00", result.substring(60, 70));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(70, 76));
        // Assert Checksum
        Assert.assertEquals("63042874", result.substring(76, 84));
    }

    @Test
    public void testCreditTransferStaticQRNationalIdWithoutAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .nationalId("0000000000001")
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29370016A00000067701011102130000000000001", result.substring(12, 53));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(53, 60));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(60, 66));
        // Assert Checksum
        Assert.assertEquals("630484B2", result.substring(66, 74));
    }

    @Test
    public void testCreditTransferStaticQRNationalIdWithAmountZero_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .nationalId("0000000000001")
                .amount(new BigDecimal("0"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29370016A00000067701011102130000000000001", result.substring(12, 53));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(53, 60));
        // Assert Transaction Amount
        Assert.assertEquals("54040.00", result.substring(60, 68));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(68, 74));
        // Assert Checksum
        Assert.assertEquals("63043B2F", result.substring(74, 82));
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferStaticQRNationalIdWithAmountBelowZero1_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .nationalId("0000000000001")
                .amount(new BigDecimal("-0.01"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferStaticQRNationalIdWithAmountBelowZero2_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .nationalId("0000000000001")
                .amount(new BigDecimal("-10000000"))
                .build();
    }

    // Credit Transfer
    // Dynamic QR
    // with National Id
    @Test
    public void testCreditTransferDynamicQRNationalIdWithAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .nationalId("0000000000001")
                .amount(new BigDecimal("100"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29370016A00000067701011102130000000000001", result.substring(12, 53));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(53, 60));
        // Assert Transaction Amount
        Assert.assertEquals("5406100.00", result.substring(60, 70));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(70, 76));
        // Assert Checksum
        Assert.assertEquals("6304885F", result.substring(76, 84));
    }

    @Test
    public void testCreditTransferDynamicQRNationalIdWithoutAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .nationalId("0000000000001")
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29370016A00000067701011102130000000000001", result.substring(12, 53));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(53, 60));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(60, 66));
        // Assert Checksum
        Assert.assertEquals("63047B71", result.substring(66, 74));
    }

    @Test
    public void testCreditTransferDynamicQRNationalIdWithAmountZero_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .nationalId("0000000000001")
                .amount(new BigDecimal("0"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29370016A00000067701011102130000000000001", result.substring(12, 53));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(53, 60));
        // Assert Transaction Amount
        Assert.assertEquals("54040.00", result.substring(60, 68));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(68, 74));
        // Assert Checksum
        Assert.assertEquals("630401F7", result.substring(74, 82));
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferDynamicQRNationalIdWithAmountBelowZero1_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .nationalId("0000000000001")
                .amount(new BigDecimal("-0.01"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferDynamicQRNationalIdWithAmountBelowZero2_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .nationalId("0000000000001")
                .amount(new BigDecimal("-10000000"))
                .build();
    }

    // Credit Transfer
    // Static QR
    // with E-Wallet ID
    @Test
    public void testCreditTransferStaticQREWalletIdWithAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .eWalletId("012345678912345")
                .amount(new BigDecimal("100"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29390016A0000006770101110315012345678912345", result.substring(12, 55));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(55, 62));
        // Assert Transaction Amount
        Assert.assertEquals("5406100.00", result.substring(62, 72));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(72, 78));
        // Assert Checksum
        Assert.assertEquals("63047AA5", result.substring(78, 86));
    }

    @Test
    public void testCreditTransferStaticQREWalletIdWithoutAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .eWalletId("012345678912345")
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29390016A0000006770101110315012345678912345", result.substring(12, 55));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(55, 62));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(62, 68));
        // Assert Checksum
        Assert.assertEquals("6304B443", result.substring(68, 76));
    }

    @Test
    public void testCreditTransferStaticQREWalletIdWithAmountZero_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .eWalletId("012345678912345")
                .amount(new BigDecimal("0"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29390016A0000006770101110315012345678912345", result.substring(12, 55));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(55, 62));
        // Assert Transaction Amount
        Assert.assertEquals("54040.00", result.substring(62, 70));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(70, 76));
        // Assert Checksum
        Assert.assertEquals("6304EA0A", result.substring(76, 84));
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferStaticQREWalletIdWithAmountBelowZero1_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .eWalletId("012345678912345")
                .amount(new BigDecimal("-0.01"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferStaticQREWalletIdWithAmountBelowZero2_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .eWalletId("012345678912345")
                .amount(new BigDecimal("-10000000"))
                .build();
    }

    // Credit Transfer
    // Dynamic QR
    // with E-Wallet ID
    @Test
    public void testCreditTransferDynamicQREWalletIdWithAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .eWalletId("012345678912345")
                .amount(new BigDecimal("100"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29390016A0000006770101110315012345678912345", result.substring(12, 55));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(55, 62));
        // Assert Transaction Amount
        Assert.assertEquals("5406100.00", result.substring(62, 72));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(72, 78));
        // Assert Checksum
        Assert.assertEquals("6304F2D2", result.substring(78, 86));
    }

    @Test
    public void testCreditTransferDynamicQREWalletIdWithoutAmount_thenSucess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .eWalletId("012345678912345")
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29390016A0000006770101110315012345678912345", result.substring(12, 55));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(55, 62));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(62, 68));
        // Assert Checksum
        Assert.assertEquals("63045E93", result.substring(68, 76));
    }

    @Test
    public void testCreditTransferDynamicQREWalletIdWithAmountZero_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .eWalletId("012345678912345")
                .amount(new BigDecimal("0"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("29390016A0000006770101110315012345678912345", result.substring(12, 55));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(55, 62));
        // Assert Transaction Amount
        Assert.assertEquals("54040.00", result.substring(62, 70));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(70, 76));
        // Assert Checksum
        Assert.assertEquals("63044A21", result.substring(76, 84));
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferDynamicQREWalletIdWithAmountBelowZero1_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .eWalletId("012345678912345")
                .amount(new BigDecimal("-0.01"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferDynamicQREWalletIdWithAmountBelowZero2_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .creditTransfer()
                .eWalletId("012345678912345")
                .amount(new BigDecimal("-10000000"))
                .build();
    }

    @Test
    public void testTheResultOfToStringEqualsGenerateContent_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0123456789")
                .amount(new BigDecimal("2.36"))
                .build();

        Assert.assertEquals(qr.generateContent(), qr.toString());
    }

    @Test
    public void testDrawToBase64Image_thenSuccess() throws IOException, WriterException {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0123456789")
                .amount(new BigDecimal("2.36"))
                .build();

        Assert.assertNotNull(qr.drawToBase64(10, 10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawToBase64WithWidthLessThanZero_thenFailure() throws IOException, WriterException {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0123456789")
                .amount(new BigDecimal("2.36"))
                .build();

        qr.drawToBase64(-10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawToBase64WithHeightLessThanZero_thenFailure() throws IOException, WriterException {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0123456789")
                .amount(new BigDecimal("2.36"))
                .build();

        qr.drawToBase64(0, -10);
    }

    @Test
    public void testDraw_thenSuccess() throws IOException, WriterException {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0123456789")
                .amount(new BigDecimal("2.36"))
                .build();

        File tmpFile = new File("/tmp/qr-tmp-file.png");
        qr.draw(10, 10, tmpFile);
        Assert.assertTrue(tmpFile.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testDrawInCaseNoFilePermission_thenFailure() throws IOException, WriterException {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0123456789")
                .amount(new BigDecimal("2.36"))
                .build();

        File tmpFile = new File("/ppppp");
        qr.draw(10, 10, tmpFile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawWidthIsLessThanZero_thenFailure() throws IOException, WriterException {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0123456789")
                .amount(new BigDecimal("2.36"))
                .build();

        File tmpFile = new File("/tmp/qr-tmp-file.png");
        qr.draw(-10, 0, tmpFile);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawHeightIsLessThanZero_thenFailure() throws IOException, WriterException {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0123456789")
                .amount(new BigDecimal("2.36"))
                .build();

        File tmpFile = new File("/tmp/qr-tmp-file.png");
        qr.draw(0, -10, tmpFile);
    }

    @Test
    public void testDrawToByteArray_thenSuccess() throws IOException, WriterException {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0123456789")
                .amount(new BigDecimal("2.36"))
                .build();

        byte[] result = qr.drawToByteArray(50, 11);
        Assert.assertNotNull(result);
        Assert.assertNotSame(0, result.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawToByteArrayWithWidthLessThanZero_thenFailure() throws IOException, WriterException {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0123456789")
                .amount(new BigDecimal("2.36"))
                .build();

        qr.drawToByteArray(-10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDrawToByteArrayWithHeightLessThanZero_thenFailure() throws IOException, WriterException {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .creditTransfer()
                .mobileNumber("0123456789")
                .amount(new BigDecimal("2.36"))
                .build();

        qr.drawToByteArray(0, -10);
    }

    // Bill Payment
    // Static QR
    @Test
    public void testBillPaymentStaticQRWithRef1AndRef2WithoutAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("1234")
                .ref2("2345")
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30530016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("02041234", result.substring(53, 61));
        // Assert Ref 2
        Assert.assertEquals("03042345", result.substring(61, 69));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(69, 76));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(76, 82));
        // Assert Checksum
        Assert.assertEquals("6304E5DA", result.substring(82, 90));
    }

    @Test
    public void testBillPaymentStaticQRWithRef1AndRef2AndAmountZero_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("1234")
                .ref2("2345")
                .amount(new BigDecimal("0.00"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30530016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("02041234", result.substring(53, 61));
        // Assert Ref 2
        Assert.assertEquals("03042345", result.substring(61, 69));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(69, 76));
        // Assert Transaction Amount
        Assert.assertEquals("54040.00", result.substring(76, 84));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(84, 90));
        // Assert Checksum
        Assert.assertEquals("6304D1F3", result.substring(90, 98));
    }

    @Test
    public void testBillPaymentStaticQRWithRef1AndRef2AndAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("6300002")
                .ref2("121214564541")
                .amount(new BigDecimal("100.35"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30640016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("02076300002", result.substring(53, 64));
        // Assert Ref 2
        Assert.assertEquals("0312121214564541", result.substring(64, 80));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(80, 87));
        // Assert Transaction Amount
        Assert.assertEquals("5406100.35", result.substring(87, 97));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(97, 103));
        // Assert Checksum
        Assert.assertEquals("63043ACE", result.substring(103, 111));
    }

    @Test
    public void testBillPaymentStaticQRWithRef1AndRef2AndRef3AndAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("1234")
                .ref2("2345")
                .ref3("67890")
                .amount(new BigDecimal("100.35"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30530016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("02041234", result.substring(53, 61));
        // Assert Ref 2
        Assert.assertEquals("03042345", result.substring(61, 69));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(69, 76));
        // Assert Transaction Amount
        Assert.assertEquals("5406100.35", result.substring(76, 86));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(86, 92));
        // Assert Ref3
        Assert.assertEquals("6209070567890", result.substring(92, 105));
        // Assert Checksum
        Assert.assertEquals("6304FA87", result.substring(105, 113));
    }

    @Test
    public void testBillPaymentStaticQRWithRef1AndAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("6300002")
                .amount(new BigDecimal("100.35"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30480016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("02076300002", result.substring(53, 64));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(64, 71));
        // Assert Transaction Amount
        Assert.assertEquals("5406100.35", result.substring(71, 81));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(81, 87));
        // Assert Checksum
        Assert.assertEquals("6304A1D7", result.substring(87, 95));
    }

    @Test
    public void testBillPaymentStaticQRWithRef1WithoutAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("12698741")
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30490016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("020812698741", result.substring(53, 65));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(65, 72));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(72, 78));
        // Assert Checksum
        Assert.assertEquals("63045A34", result.substring(78, 86));
    }

    @Test
    public void testBillPaymentStaticQRWithRef1AndAmountZero_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("12698741")
                .amount(new BigDecimal("0.00"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30490016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("020812698741", result.substring(53, 65));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(65, 72));
        // Assert Transaction Amount
        Assert.assertEquals("54040.00", result.substring(72, 80));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(80, 86));
        // Assert Checksum
        Assert.assertEquals("63049907", result.substring(86, 94));
    }

    @Test
    public void testBillPaymentStaticQRWithRef1AndRef3AndAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("12698741")
                .ref3("111ABCDE222")
                .amount(new BigDecimal("3.99"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30490016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("020812698741", result.substring(53, 65));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(65, 72));
        // Assert Transaction Amount
        Assert.assertEquals("54043.99", result.substring(72, 80));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(80, 86));
        // Assert Ref3
        Assert.assertEquals("62150711111ABCDE222", result.substring(86, 105));
        // Assert Checksum
        Assert.assertEquals("63042721", result.substring(105, 113));
    }

    @Test
    public void testBillPaymentStaticQRWithRef1AndRef3AndAmountZero_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("12698741")
                .ref3("111ABCDE222")
                .amount(new BigDecimal("0.00"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30490016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("020812698741", result.substring(53, 65));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(65, 72));
        // Assert Transaction Amount
        Assert.assertEquals("54040.00", result.substring(72, 80));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(80, 86));
        // Assert Ref3
        Assert.assertEquals("62150711111ABCDE222", result.substring(86, 105));
        // Assert Checksum
        Assert.assertEquals("63045F66", result.substring(105, 113));
    }

    @Test
    public void testBillPaymentStaticQRWithRef1AndRef3WithoutAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("12698741")
                .ref3("111ABCDE222")
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010211", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30490016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("020812698741", result.substring(53, 65));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(65, 72));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(72, 78));
        // Assert Ref3
        Assert.assertEquals("62150711111ABCDE222", result.substring(78, 97));
        // Assert Checksum
        Assert.assertEquals("63047989", result.substring(97, 105));
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1AndAmountThreeDecimalPlace_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .amount(new BigDecimal("0.051"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1AndAmountBelowZero1_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .amount(new BigDecimal("-0.01"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1AndAmountBelowZero2_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .amount(new BigDecimal("-100000"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1ContainsSpecialCharacter1AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234ก")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1ContainsSpecialCharacter2AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234#")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1ContainsSpecialCharacter3AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234@")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1AndRef2ContainsSpecialCharacter1AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref2("122234ก")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1AndRef2ContainsSpecialCharacter2AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref2("122234#")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1AndRef2ContainsSpecialCharacter3AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref2("122234@")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1AndRef2AndRef3ContainsSpecialCharacter1AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref2("122234")
                .ref3("122234ก")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1AndRef2AndRef3ContainsSpecialCharacter2AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref2("122234")
                .ref3("122234#")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1AndRef2AndRef3ContainsSpecialCharacter3AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref2("122234")
                .ref3("122234@")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1AndRef3ContainsSpecialCharacter1AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref3("122234ก")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1AndRef3ContainsSpecialCharacter2AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref3("122234#")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentStaticQRWithRef1AndRef3ContainsSpecialCharacter3AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .staticQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref3("122234@")
                .amount(new BigDecimal("100"))
                .build();
    }


    // Bill Payment
    // Dynamic QR
    @Test
    public void testBillPaymentDynamicQRWithRef1AndRef2WithoutAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("1234")
                .ref2("2345")
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30530016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("02041234", result.substring(53, 61));
        // Assert Ref 2
        Assert.assertEquals("03042345", result.substring(61, 69));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(69, 76));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(76, 82));
        // Assert Checksum
        Assert.assertEquals("6304E0B9", result.substring(82, 90));
    }

    @Test
    public void testBillPaymentDynamicQRWithRef1AndRef2AndAmountZero_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("1234")
                .ref2("2345")
                .amount(new BigDecimal("0.00"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30530016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("02041234", result.substring(53, 61));
        // Assert Ref 2
        Assert.assertEquals("03042345", result.substring(61, 69));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(69, 76));
        // Assert Transaction Amount
        Assert.assertEquals("54040.00", result.substring(76, 84));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(84, 90));
        // Assert Checksum
        Assert.assertEquals("6304F2D5", result.substring(90, 98));
    }

    @Test
    public void testBillPaymentDynamicQRWithRef1AndRef2AndAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("6300002")
                .ref2("121214564541")
                .amount(new BigDecimal("100.35"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30640016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("02076300002", result.substring(53, 64));
        // Assert Ref 2
        Assert.assertEquals("0312121214564541", result.substring(64, 80));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(80, 87));
        // Assert Transaction Amount
        Assert.assertEquals("5406100.35", result.substring(87, 97));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(97, 103));
        // Assert Checksum
        Assert.assertEquals("6304BD9D", result.substring(103, 111));
    }

    @Test
    public void testBillPaymentDynamicQRWithRef1AndRef2AndRef3AndAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("1234")
                .ref2("2345")
                .ref3("67890")
                .amount(new BigDecimal("100.35"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30530016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("02041234", result.substring(53, 61));
        // Assert Ref 2
        Assert.assertEquals("03042345", result.substring(61, 69));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(69, 76));
        // Assert Transaction Amount
        Assert.assertEquals("5406100.35", result.substring(76, 86));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(86, 92));
        // Assert Ref3
        Assert.assertEquals("6209070567890", result.substring(92, 105));
        // Assert Checksum
        Assert.assertEquals("6304121E", result.substring(105, 113));
    }

    @Test
    public void testBillPaymentDynamicQRWithRef1AndAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("6300002")
                .amount(new BigDecimal("100.35"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30480016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("02076300002", result.substring(53, 64));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(64, 71));
        // Assert Transaction Amount
        Assert.assertEquals("5406100.35", result.substring(71, 81));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(81, 87));
        // Assert Checksum
        Assert.assertEquals("6304408E", result.substring(87, 95));
    }

    @Test
    public void testBillPaymentDynamicQRWithRef1WithoutAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("12698741")
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30490016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("020812698741", result.substring(53, 65));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(65, 72));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(72, 78));
        // Assert Checksum
        Assert.assertEquals("6304D243", result.substring(78, 86));
    }

    @Test
    public void testBillPaymentDynamicQRWithRef1AndAmountZero_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("12698741")
                .amount(new BigDecimal("0.00"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30490016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("020812698741", result.substring(53, 65));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(65, 72));
        // Assert Transaction Amount
        Assert.assertEquals("54040.00", result.substring(72, 80));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(80, 86));
        // Assert Checksum
        Assert.assertEquals("6304E779", result.substring(86, 94));
    }

    @Test
    public void testBillPaymentDynamicQRWithRef1AndRef3AndAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("12698741")
                .ref3("111ABCDE222")
                .amount(new BigDecimal("3.99"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30490016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("020812698741", result.substring(53, 65));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(65, 72));
        // Assert Transaction Amount
        Assert.assertEquals("54043.99", result.substring(72, 80));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(80, 86));
        // Assert Ref3
        Assert.assertEquals("62150711111ABCDE222", result.substring(86, 105));
        // Assert Checksum
        Assert.assertEquals("6304CFB8", result.substring(105, 113));
    }

    @Test
    public void testBillPaymentDynamicQRWithRef1AndRef3AndAmountZero_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("12698741")
                .ref3("111ABCDE222")
                .amount(new BigDecimal("0.00"))
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30490016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("020812698741", result.substring(53, 65));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(65, 72));
        // Assert Transaction Amount
        Assert.assertEquals("54040.00", result.substring(72, 80));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(80, 86));
        // Assert Ref3
        Assert.assertEquals("62150711111ABCDE222", result.substring(86, 105));
        // Assert Checksum
        Assert.assertEquals("6304B7FF", result.substring(105, 113));
    }

    @Test
    public void testBillPaymentDynamicQRWithRef1AndRef3WithoutAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0000000000001")
                .ref1("12698741")
                .ref3("111ABCDE222")
                .build();

        String result = qr.generateContent();
        // Assert Payload Format Indicator
        Assert.assertEquals("000201", result.substring(0, 6));
        // Assert Point of Initiation method
        Assert.assertEquals("010212", result.substring(6, 12));
        // Assert Merchant Identifier
        Assert.assertEquals("30490016A00000067701011201130000000000001", result.substring(12, 53));
        // Assert Ref 1
        Assert.assertEquals("020812698741", result.substring(53, 65));
        // Assert Transaction Currency Code
        Assert.assertEquals("5303764", result.substring(65, 72));
        // Assert Country Code
        Assert.assertEquals("5802TH", result.substring(72, 78));
        // Assert Ref3
        Assert.assertEquals("62150711111ABCDE222", result.substring(78, 97));
        // Assert Checksum
        Assert.assertEquals("63047714", result.substring(97, 105));
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1AndAmountThreeDecimalPlace_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .amount(new BigDecimal("0.051"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1AndAmountBelowZero1_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .amount(new BigDecimal("-0.01"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1AndAmountBelowZero2_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .amount(new BigDecimal("-100000"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1ContainsSpecialCharacter1AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234ก")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1ContainsSpecialCharacter2AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234#")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1ContainsSpecialCharacter3AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234@")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1AndRef2ContainsSpecialCharacter1AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref2("122234ก")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1AndRef2ContainsSpecialCharacter2AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref2("122234#")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1AndRef2ContainsSpecialCharacter3AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref2("122234@")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1AndRef2AndRef3ContainsSpecialCharacter1AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref2("122234")
                .ref3("122234ก")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1AndRef2AndRef3ContainsSpecialCharacter2AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref2("122234")
                .ref3("122234#")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1AndRef2AndRef3ContainsSpecialCharacter3AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref2("122234")
                .ref3("122234@")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1AndRef3ContainsSpecialCharacter1AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref3("122234ก")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1AndRef3ContainsSpecialCharacter2AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref3("122234#")
                .amount(new BigDecimal("100"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentDynamicQRWithRef1AndRef3ContainsSpecialCharacter3AndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .dynamicQR()
                .billPayment()
                .billerId("0123456789123")
                .ref1("122234")
                .ref3("122234@")
                .amount(new BigDecimal("100"))
                .build();
    }

    // Bill Payment
    // BOT
    @Test
    public void testBillPaymentBotWithRef1AndRef2AndAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .bot()
                .billPayment()
                .billerId("000000000009132")
                .ref1("321312312")
                .ref2("432542353245")
                .amount(new BigDecimal("11.00"))
                .build();

        String result = qr.generateContent();
        String[] resultSeparatedByCR = result.split("\n");
        // Assert Four Parts of Content
        Assert.assertEquals(4, resultSeparatedByCR.length);
        // Assert Biller ID
        Assert.assertEquals("|000000000009132", resultSeparatedByCR[0]);
        // Assert Ref 1
        Assert.assertEquals("321312312", resultSeparatedByCR[1]);
        // Assert Ref 2
        Assert.assertEquals("432542353245", resultSeparatedByCR[2]);
        // Assert Transaction Amount
        Assert.assertEquals("1100", resultSeparatedByCR[3]);
    }

    @Test
    public void testBillPaymentBotWithRef1AndRef2AndAmountZero_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .bot()
                .billPayment()
                .billerId("000000000009132")
                .ref1("321312312")
                .ref2("432542353245")
                .amount(new BigDecimal("0.00"))
                .build();

        String result = qr.generateContent();
        String[] resultSeparatedByCR = result.split("\n");
        // Assert Four Parts of Content
        Assert.assertEquals(4, resultSeparatedByCR.length);
        // Assert Biller ID
        Assert.assertEquals("|000000000009132", resultSeparatedByCR[0]);
        // Assert Ref 1
        Assert.assertEquals("321312312", resultSeparatedByCR[1]);
        // Assert Ref 2
        Assert.assertEquals("432542353245", resultSeparatedByCR[2]);
        // Assert Transaction Amount
        Assert.assertEquals("0", resultSeparatedByCR[3]);
    }

    @Test
    public void testBillPaymentBotWithRef1AndRef2WithoutAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .bot()
                .billPayment()
                .billerId("000000000009132")
                .ref1("321312312")
                .ref2("432542353245")
                .build();

        String result = qr.generateContent();
        String[] resultSeparatedByCR = result.split("\n");
        // Assert Four Parts of Content
        Assert.assertEquals(4, resultSeparatedByCR.length);
        // Assert Biller ID
        Assert.assertEquals("|000000000009132", resultSeparatedByCR[0]);
        // Assert Ref 1
        Assert.assertEquals("321312312", resultSeparatedByCR[1]);
        // Assert Ref 2
        Assert.assertEquals("432542353245", resultSeparatedByCR[2]);
        // Assert Transaction Amount
        Assert.assertEquals("0", resultSeparatedByCR[3]);
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentBotWithRef1AndRef2AndAmountThreeDecimalPlace_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .bot()
                .billPayment()
                .billerId("000000000009132")
                .ref1("321312312")
                .ref2("123231")
                .amount(new BigDecimal("2.349"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentBotWithRef1AndRef2AndAmountBelowZero_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .bot()
                .billPayment()
                .billerId("000000000009132")
                .ref1("321312312")
                .ref2("123231")
                .amount(new BigDecimal("-2.34"))
                .build();
    }

    @Test
    public void testBillPaymentBotWithRef1AndAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .bot()
                .billPayment()
                .billerId("000000000009132")
                .ref1("321312312")
                .amount(new BigDecimal("11.00"))
                .build();

        String result = qr.generateContent();
        String[] resultSeparatedByCR = result.split("\n");
        // Assert Four Parts of Content
        Assert.assertEquals(4, resultSeparatedByCR.length);
        // Assert Biller ID
        Assert.assertEquals("|000000000009132", resultSeparatedByCR[0]);
        // Assert Ref 1
        Assert.assertEquals("321312312", resultSeparatedByCR[1]);
        // Assert Ref 2
        Assert.assertEquals("", resultSeparatedByCR[2]);
        // Assert Transaction Amount
        Assert.assertEquals("1100", resultSeparatedByCR[3]);
    }

    @Test
    public void testBillPaymentBotWithRef1WithoutAmount_thenSuccess() {
        ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder()
                .bot()
                .billPayment()
                .billerId("000000000009132")
                .ref1("321312312")
                .build();

        String result = qr.generateContent();
        String[] resultSeparatedByCR = result.split("\n");
        // Assert Four Parts of Content
        Assert.assertEquals(4, resultSeparatedByCR.length);
        // Assert Biller ID
        Assert.assertEquals("|000000000009132", resultSeparatedByCR[0]);
        // Assert Ref 1
        Assert.assertEquals("321312312", resultSeparatedByCR[1]);
        // Assert Ref 2
        Assert.assertEquals("", resultSeparatedByCR[2]);
        // Assert Transaction Amount
        Assert.assertEquals("0", resultSeparatedByCR[3]);
    }

    @Test(expected = IllegalStateException.class)
    public void testBillPaymentBotWithRef1AndAmountBelowZero_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .bot()
                .billPayment()
                .billerId("000000000009132")
                .ref1("321312312")
                .amount(new BigDecimal("-2.34"))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferBotWithMobileNumber_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .bot()
                .creditTransfer()
                .mobileNumber("0000000000")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreditTransferBotWithMobileNumberAndAmount_thenFailure() {
        new ThaiQRPromptPay.Builder()
                .bot()
                .creditTransfer()
                .mobileNumber("0000000000")
                .amount(new BigDecimal("10.00"))
                .build();
    }
}
