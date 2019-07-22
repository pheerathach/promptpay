package com.github.pheerathach;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import static com.github.pheerathach.Constants.*;
import static com.github.pheerathach.Helper.*;

/**
 * @author pheerathach
 */
public class ThaiQRPromptPay {
    private static final DecimalFormat moneyFormat = new DecimalFormat("0.00");

    private Integer paymentField;
    private String usageType;
    private String acquirerId;
    private String billerId;
    private String mobileNumber;
    private String nationalId;
    private String eWalletId;
    private String bankAccount;
    private String ref1;
    private String ref2;
    private String ref3;
    private BigDecimal amount;
    private String currencyCode;
    private String countryCode;

    private ThaiQRPromptPay(Builder builder) {
        if (builder.selectPromptPayTypeBuilder.selectPromptPayType instanceof Builder.SelectPromptPayTypeBuilder.CreditTransferBuilder) {
            this.paymentField = CREDIT_TRANSFER_DATA_FIELD_ID;
            this.acquirerId = CREDIT_TRANSFER_ACQUIRER_ID;
            Builder.SelectPromptPayTypeBuilder.CreditTransferBuilder creditTransferBuilder = (Builder.SelectPromptPayTypeBuilder.CreditTransferBuilder) builder.selectPromptPayTypeBuilder.selectPromptPayType;
            this.mobileNumber = creditTransferBuilder.mobileNumber;
            this.nationalId = creditTransferBuilder.nationalId;
            this.eWalletId = creditTransferBuilder.eWalletId;
            this.bankAccount = creditTransferBuilder.bankAccount;
            this.amount = creditTransferBuilder.amount;
        } else {
            this.paymentField = BILL_PAYMENT_DATA_FIELD_ID;
            this.acquirerId = BILL_PAYMENT_DATA_ACQUIRER_ID;
            Builder.SelectPromptPayTypeBuilder.BillPaymentBuilder billPaymentBuilder = (Builder.SelectPromptPayTypeBuilder.BillPaymentBuilder) builder.selectPromptPayTypeBuilder.selectPromptPayType;
            this.billerId = billPaymentBuilder.billerId;
            this.ref1 = billPaymentBuilder.ref1;
            this.ref2 = billPaymentBuilder.ref2;
            this.ref3 = billPaymentBuilder.ref3;
            this.amount = billPaymentBuilder.amount;

        }
        this.usageType = builder.usageType;
        this.currencyCode = builder.currencyCode;
        this.countryCode = builder.countryCode;
    }

    private static ByteArrayOutputStream generateQRCodeImage(String text, int width, int height)
            throws WriterException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream;
    }

    /**
     * Returns the content for further QR generation
     * @return
     */
    public String generateContent() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(generateField(0, PAYLOAD_FORMAT_INDICATOR));
        stringBuilder.append(generateField(1, usageType));

        StringBuilder content = new StringBuilder(generateField(0, acquirerId));
        if (paymentField == 29) {
            if (mobileNumber != null) {
                if (mobileNumber.startsWith("0")) {
                    mobileNumber = mobileNumber.substring(1);
                }
                content.append(generateField(1, "00" + DEFAULT_COUNTRY_CODE_TEL + mobileNumber));
            } else if (nationalId != null) {
                content.append(generateField(2, nationalId));
            } else if (eWalletId != null) {
                content.append(generateField(3, eWalletId));
            } else if (bankAccount != null) {
                content.append(generateField(4, bankAccount));
            }
        } else if (paymentField == 30) {
            content.append(generateField(1, billerId));
            content.append(generateField(2, ref1));
            if (ref2 != null) {
                content.append(generateField(3, ref2));
            }
        }
        stringBuilder.append(generateField(paymentField, content.toString()));

        stringBuilder.append(generateField(53, currencyCode));
        if (amount != null) {
            stringBuilder.append(generateField(54, moneyFormat.format(amount)));
        }
        stringBuilder.append(generateField(58, countryCode));
        if (ref3 != null) {
            stringBuilder.append(generateField(62, generateField(7, ref3)));
        }
        stringBuilder.append("6304" + (Integer.toHexString(Helper.crc16((stringBuilder.toString() + "6304").getBytes())).toUpperCase()));
        return stringBuilder.toString();
    }

    /**
     * Return the content for further QR generation
     * @return
     */
    @Override
    public String toString() {
        return generateContent();
    }

    private String generateField(int fieldId, String content) {
        return String.format("%02d", fieldId) + String.format("%02d", content.length()) + content;
    }

    /**
     * Draw the QR code image to the specified path with specified width and height.
     * @param width
     * @param height
     * @param file
     * @throws IOException
     * @throws WriterException
     */
    public void draw(int width, int height, File file) throws IOException, WriterException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            fileOutputStream.write(generateQRCodeImage(generateContent(), width, height).toByteArray());
        } finally {
            fileOutputStream.close();
        }
    }

    /**
     * Draw the QR code image to Base64 string.
     * @param width
     * @param height
     * @return
     * @throws IOException
     * @throws WriterException
     */
    public String drawToBase64(int width, int height) throws IOException, WriterException {
        byte[] imageData = generateQRCodeImage(generateContent(), width, height).toByteArray();
        return new String(Base64.encodeBase64(imageData));
    }

    /**
     * Draw the QR code image to byte array.
     * @param width
     * @param height
     * @return
     * @throws IOException
     * @throws WriterException
     */
    public byte[] drawToByteArray(int width, int height) throws IOException, WriterException {
        return generateQRCodeImage(generateContent(), width, height).toByteArray();
    }

    public static class Builder {
        private String usageType;
        protected String currencyCode = DEFAULT_CURRENCY_CODE;
        protected String countryCode = DEFAULT_COUNTRY_CODE;
        protected SelectPromptPayTypeBuilder selectPromptPayTypeBuilder = new SelectPromptPayTypeBuilder();

        /**
         * Specify currency code
         * Default is Thai Baht (764)
         * @param currencyCode
         * @return
         */
        public Builder currencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        /**
         * Specify country code
         * Default is Thailand (TH)
         * @param countryCode
         * @return
         */
        public Builder countryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public SelectPromptPayTypeBuilder staticQR() {
            this.usageType = STATIC_QR_CODE;
            return selectPromptPayTypeBuilder;
        }

        public SelectPromptPayTypeBuilder dynamicQR() {
            this.usageType = DYNAMIC_QR_CODE;
            return selectPromptPayTypeBuilder;
        }

        private interface BillPaymentBuilderBillerId {
            BillPaymentBuilderRef1 billerId(String billerId);
        }

        private interface BillPaymentBuilderRef1 {
            BillPaymentBuilderOptionalDetail ref1(String ref1);
        }

        private interface BillPaymentBuilderOptionalDetail extends BuildReady {
            BillPaymentBuilderOptionalDetail amount(BigDecimal amount);

            BillPaymentBuilderOptionalDetail amount(String amount);

            BillPaymentBuilderOptionalDetail ref2(String ref2);

            BillPaymentBuilderOptionalDetail ref3(String ref3);
        }

        private interface CreditTransferBuilderIdenifier {
            CreditTransferBuilderAmount mobileNumber(String mobileNumber);

            CreditTransferBuilderAmount nationalId(String nationalId);

            CreditTransferBuilderAmount eWalletId(String eWalletId);

            CreditTransferBuilderAmount bankAccount(String bankAccount);
        }

        private interface CreditTransferBuilderAmount extends BuildReady {
            BuildReady amount(BigDecimal amount);
        }

        private interface BuildReady {
            ThaiQRPromptPay build();
        }

        private interface SelectPromptPayType {

        }

        public class SelectPromptPayTypeBuilder {
            protected SelectPromptPayType selectPromptPayType;

            /**
             * Specify PromptPay Type as Credit Transfer (Tag 29)
             * @return
             */
            private CreditTransferBuilderIdenifier creditTransfer() {
                CreditTransferBuilder creditTransferBuilder = new CreditTransferBuilder();
                this.selectPromptPayType = creditTransferBuilder;
                return creditTransferBuilder;
            }

            /**
             * Specify PromptPay Type as Bill Payment (Tag 30)
             * @return
             */
            public BillPaymentBuilderBillerId billPayment() {
                BillPaymentBuilder billPaymentBuilder = new BillPaymentBuilder();
                this.selectPromptPayType = billPaymentBuilder;
                return billPaymentBuilder;
            }

            private class CreditTransferBuilder implements SelectPromptPayType, CreditTransferBuilderIdenifier, CreditTransferBuilderAmount {
                private String mobileNumber;
                private String nationalId;
                private String eWalletId;
                private String bankAccount;
                private BigDecimal amount;

                /**
                 * Specify mobile number
                 * @param mobileNumber Mobile Number
                 * @return
                 */
                public CreditTransferBuilderAmount mobileNumber(String mobileNumber) {
                    validateNumeric("Mobile Number", mobileNumber);
                    validateLength("Mobile Number", mobileNumber, 10);
                    this.mobileNumber = mobileNumber;
                    return this;
                }

                /**
                 * Specify Thai National ID
                 * @param nationalId Thai National ID
                 * @return
                 */
                public CreditTransferBuilderAmount nationalId(String nationalId) {
                    validateNumeric("National ID/Tax ID", nationalId);
                    validateLength("National ID/Tax ID", nationalId, 13);
                    this.nationalId = nationalId;
                    return this;
                }

                /**
                 * Specify E-Wallet ID
                 * @param eWalletId E-Wallet ID
                 * @return
                 */
                public CreditTransferBuilderAmount eWalletId(String eWalletId) {
                    validateNumeric("E-Wallet ID", eWalletId);
                    validateLength("E-Wallet ID", eWalletId, 15);
                    this.eWalletId = eWalletId;
                    return this;
                }

                /**
                 * Specify bank account number
                 * @param bankAccount Bank Account Number
                 * @return
                 */
                public CreditTransferBuilderAmount bankAccount(String bankAccount) {
                    validateNumeric("Bank Account", bankAccount);
                    validateLength("Bank Account", bankAccount, 43);
                    this.bankAccount = bankAccount;
                    return this;
                }

                /**
                 * Specify amount in BigDecimal
                 * @param amount
                 * @return
                 */
                public BuildReady amount(BigDecimal amount) {
                    validateAmount(amount);
                    this.amount = amount;
                    return this;
                }

                /**
                 * Specify amount in String
                 * @param amount
                 * @return
                 */
                public BuildReady amount(String amount) {
                    return amount(new BigDecimal(amount));
                }

                /**
                 * Construct ThaiQRPromptPay object
                 * @return
                 */
                public ThaiQRPromptPay build() {
                    return new ThaiQRPromptPay(Builder.this);
                }
            }

            private class BillPaymentBuilder implements SelectPromptPayType, BillPaymentBuilderBillerId, BillPaymentBuilderRef1, BillPaymentBuilderOptionalDetail {
                private String billerId;
                private String ref1;
                private String ref2;
                private String ref3;
                private BigDecimal amount;

                /**
                 * Specify Tax ID (10 or 13 digits) + Suffix 2 digits
                 * @param billerId
                 * @return
                 */
                public BillPaymentBuilderRef1 billerId(String billerId) {
                    validateNumeric("Biller ID", billerId);
                    validateLength("Biller ID", billerId, 15);
                    this.billerId = billerId;
                    return this;
                }

                /**
                 * Specify Reference 1
                 * @param ref1
                 * @return
                 */
                public BillPaymentBuilderOptionalDetail ref1(String ref1) {
                    validateAlphanumeric("Reference 1", ref1);
                    validateLength("Reference 1", ref1, 15);
                    this.ref1 = ref1;
                    return this;
                }

                /**
                 * Specify Reference 2
                 * @param ref2
                 * @return
                 */
                public BillPaymentBuilderOptionalDetail ref2(String ref2) {
                    validateAlphanumeric("Reference 2", ref2);
                    validateLength("Reference 2", ref2, 20);
                    this.ref2 = ref2;
                    return this;
                }

                /**
                 * Specify Terminal ID or Reference 3
                 * @param ref3
                 * @return
                 */
                public BillPaymentBuilderOptionalDetail ref3(String ref3) {
                    validateAlphanumeric("Reference 3", ref3);
                    validateLength("Terminal ID/Reference 3", ref3, 26);
                    this.ref3 = ref3;
                    return this;
                }

                /**
                 * Specify amount in BigDecimal
                 * @param amount
                 * @return
                 */
                public BillPaymentBuilderOptionalDetail amount(BigDecimal amount) {
                    validateLength("Amount", moneyFormat.format(amount), 13);
                    validateAmount(amount);
                    this.amount = amount;
                    return this;
                }

                /**
                 * Specify amount in String
                 * @param amount
                 * @return
                 */
                public BillPaymentBuilderOptionalDetail amount(String amount) {
                    return amount(new BigDecimal(amount));
                }

                /**
                 * Construct ThaiQRPromptPay object
                 * @return
                 */
                public ThaiQRPromptPay build() {
                    return new ThaiQRPromptPay(Builder.this);
                }
            }
        }
    }


}