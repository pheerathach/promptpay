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
import java.math.RoundingMode;
import java.text.DecimalFormat;

import static com.github.pheerathach.Constants.*;
import static com.github.pheerathach.Helper.*;

public class ThaiQRPromptPay {
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("0.00");

    private final Integer paymentField;
    private final String usageType;
    private final String acquirerId;
    private final BigDecimal amount;
    private final String currencyCode;
    private final String countryCode;
    private String billerId;
    private String mobileNumber;
    private String nationalId;
    private String eWalletId;
    private String ref1;
    private String ref2;
    private String ref3;
    private OutputType outputType;

    private ThaiQRPromptPay(Builder builder) {
        if (builder.selectPromptPayTypeBuilder.selectPromptPayType instanceof Builder.SelectPromptPayTypeBuilder.CreditTransferBuilder) {
            this.paymentField = CREDIT_TRANSFER_DATA_FIELD_ID;
            this.acquirerId = CREDIT_TRANSFER_ACQUIRER_ID;
            Builder.SelectPromptPayTypeBuilder.CreditTransferBuilder creditTransferBuilder = (Builder.SelectPromptPayTypeBuilder.CreditTransferBuilder) builder.selectPromptPayTypeBuilder.selectPromptPayType;
            this.mobileNumber = creditTransferBuilder.mobileNumber;
            this.nationalId = creditTransferBuilder.nationalId;
            this.eWalletId = creditTransferBuilder.eWalletId;
            this.amount = creditTransferBuilder.amount;
            this.outputType = OutputType.TAG30;
        } else {
            this.paymentField = BILL_PAYMENT_DATA_FIELD_ID;
            this.acquirerId = BILL_PAYMENT_DATA_ACQUIRER_ID;
            Builder.SelectPromptPayTypeBuilder.BillPaymentBuilder billPaymentBuilder = (Builder.SelectPromptPayTypeBuilder.BillPaymentBuilder) builder.selectPromptPayTypeBuilder.selectPromptPayType;
            this.billerId = billPaymentBuilder.billerId;
            this.ref1 = billPaymentBuilder.ref1;
            this.ref2 = billPaymentBuilder.ref2;
            this.ref3 = billPaymentBuilder.ref3;
            this.amount = billPaymentBuilder.amount;
            this.outputType = builder.outputType;
        }
        this.usageType = builder.usageType;
        this.currencyCode = builder.currencyCode;
        this.countryCode = builder.countryCode;
    }

    /**
     * Returns the content for later QR generation
     *
     * @return The content of generated QR.
     */
    public String generateContent() {
        switch (outputType) {
            case BOT3:
                return generateBOT();
            case TAG30:
            default:
                return generatePromptPayQR();
        }
    }

    private static ByteArrayOutputStream generateQRCodeImage(String text, int width, int height)
            throws IOException, WriterException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream;
    }

    private String generateBOT() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("|");
        stringBuilder.append(billerId);
        stringBuilder.append("\n");
        stringBuilder.append(ref1);
        stringBuilder.append("\n");
        if (ref2 != null) {
            stringBuilder.append(ref2);
        }
        stringBuilder.append("\n");
        if (amount != null) {
            stringBuilder.append(amount.multiply(oneHundred).setScale(0, RoundingMode.DOWN));
        } else {
            stringBuilder.append(0);
        }
        return stringBuilder.toString();
    }

    private String generatePromptPayQR() {
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
            stringBuilder.append(generateField(54, MONEY_FORMAT.format(amount)));
        }

        stringBuilder.append(generateField(58, countryCode));
        if (ref3 != null) {
            stringBuilder.append(generateField(62, generateField(7, ref3)));
        }

        stringBuilder.append("6304");
        stringBuilder.append(Helper.crc16((stringBuilder.toString()).getBytes()));
        return stringBuilder.toString();
    }

    public enum OutputType {
        BOT3, TAG30
    }

    /**
     * Return the content for later QR generation
     *
     * @return The content of generated QR.
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
     *
     * @param width  the width of QR code in pixels
     * @param height the height of QR code in pixels
     * @param file   the path which QR code image would be written to (PNG format)
     * @throws IOException     if the path to write QR code is invalid.
     * @throws WriterException if the content of QR code is malformed.
     */
    public void draw(int width, int height, File file) throws IOException, WriterException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(generateQRCodeImage(generateContent(), width, height).toByteArray());
        }
    }

    /**
     * Draw the QR code image to Base64 string.
     * @param width the width of QR code in pixels
     * @param height the height of QR code in pixels
     * @return the base64 string of QR code image
     * @throws IOException if the path to write QR code is invalid.
     * @throws WriterException if the content of QR code is malformed.
     */
    public String drawToBase64(int width, int height) throws IOException, WriterException {
        byte[] imageData = generateQRCodeImage(generateContent(), width, height).toByteArray();
        return new String(Base64.encodeBase64(imageData));
    }

    /**
     * Draw the QR code image to byte array.
     * @param width the width of QR code in pixels
     * @param height the height of QR code in pixels
     * @return the byte array of QR code image
     * @throws IOException if the path to write QR code is invalid.
     * @throws WriterException if the content of QR code is malformed.
     */
    public byte[] drawToByteArray(int width, int height) throws IOException, WriterException {
        return generateQRCodeImage(generateContent(), width, height).toByteArray();
    }

    public static class Builder {
        private String usageType;
        protected String currencyCode = DEFAULT_CURRENCY_CODE;
        protected String countryCode = DEFAULT_COUNTRY_CODE;
        protected SelectPromptPayTypeBuilder selectPromptPayTypeBuilder = new SelectPromptPayTypeBuilder();
        protected OutputType outputType = OutputType.TAG30;

        /**
         * Specify currency code
         * Default is Thai Baht (764)
         * @param currencyCode Currency Code
         * @return This builder.
         */
        public Builder currencyCode(String currencyCode) {
            validateNumeric("Currency Code", currencyCode);
            validateLength("Currency Code", currencyCode, 3);
            this.currencyCode = currencyCode;
            return this;
        }

        /**
         * Specify country code
         * Default is Thailand (TH)
         * @param countryCode Country Code
         * @return This builder.
         */
        public Builder countryCode(String countryCode) {
            validateLength("Country Code", countryCode, 2);
            this.countryCode = countryCode.toUpperCase();
            return this;
        }

        public SelectPromptPayTypeBuilder staticQR() {
            this.usageType = STATIC_QR_CODE;
            this.outputType = OutputType.TAG30;
            return selectPromptPayTypeBuilder;
        }

        public SelectPromptPayTypeBuilder dynamicQR() {
            this.usageType = DYNAMIC_QR_CODE;
            this.outputType = OutputType.TAG30;
            return selectPromptPayTypeBuilder;
        }

        public SelectPromptPayTypeBuilder bot() {
            this.outputType = OutputType.BOT3;
            return selectPromptPayTypeBuilder;
        }

        public interface BillPaymentBuilderBillerId {
            BillPaymentBuilderRef1 billerId(String billerId);
        }

        public interface BillPaymentBuilderRef1 {
            BillPaymentBuilderOptionalDetail ref1(String ref1);
        }

        public interface BillPaymentBuilderOptionalDetail extends BuildReady {
            BillPaymentBuilderOptionalDetail amount(BigDecimal amount);

            BillPaymentBuilderOptionalDetail ref2(String ref2);

            BillPaymentBuilderOptionalDetail ref3(String ref3);
        }

        public interface CreditTransferBuilderIdentifier {
            CreditTransferBuilderAmount mobileNumber(String mobileNumber);

            CreditTransferBuilderAmount nationalId(String nationalId);

            CreditTransferBuilderAmount eWalletId(String eWalletId);
        }

        public interface CreditTransferBuilderAmount extends BuildReady {
            BuildReady amount(BigDecimal amount);
        }

        public interface BuildReady {
            ThaiQRPromptPay build();
        }

        interface SelectPromptPayType {

        }

        public class SelectPromptPayTypeBuilder {
            protected SelectPromptPayType selectPromptPayType;

            public BillPaymentBuilderBillerId billPayment() {
                BillPaymentBuilder billPaymentBuilder = new BillPaymentBuilder();
                this.selectPromptPayType = billPaymentBuilder;
                return billPaymentBuilder;
            }

            public CreditTransferBuilderIdentifier creditTransfer() {
                CreditTransferBuilder creditTransferBuilder = new CreditTransferBuilder();
                this.selectPromptPayType = creditTransferBuilder;
                return creditTransferBuilder;
            }

            private class CreditTransferBuilder implements SelectPromptPayType, CreditTransferBuilderIdentifier, CreditTransferBuilderAmount {
                private String mobileNumber;
                private String nationalId;
                private String eWalletId;
                private BigDecimal amount;

                @Override
                public CreditTransferBuilderAmount mobileNumber(String mobileNumber) {
                    validateNumeric("Mobile Number", mobileNumber);
                    validateLength("Mobile Number", mobileNumber, 10);
                    this.mobileNumber = mobileNumber;
                    return this;
                }

                /**
                 * Specify Thai National ID
                 *
                 * @param nationalId Thai National ID
                 * @return This builder.
                 */
                @Override
                public CreditTransferBuilderAmount nationalId(String nationalId) {
                    validateNumeric("National ID/Tax ID", nationalId);
                    validateLength("National ID/Tax ID", nationalId, 13);
                    this.nationalId = nationalId;
                    return this;
                }

                /**
                 * Specify E-Wallet ID
                 *
                 * @param eWalletId E-Wallet ID
                 * @return This builder.
                 */
                @Override
                public CreditTransferBuilderAmount eWalletId(String eWalletId) {
                    validateNumeric("E-Wallet ID", eWalletId);
                    validateLength("E-Wallet ID", eWalletId, 15);
                    this.eWalletId = eWalletId;
                    return this;
                }

                /**
                 * Specify amount in BigDecimal
                 * @param amount Transaction amount
                 * @return This builder.
                 */
                @Override
                public BuildReady amount(BigDecimal amount) {
                    validateAmount(amount);
                    this.amount = amount;
                    return this;
                }

                /**
                 * Construct ThaiQRPromptPay object
                 * @return Returns an instance of ThaiQRPromptPay created from the fields set on this builder.
                 */
                @Override
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
                 * @param billerId Biller ID of payee
                 * @return This builder.
                 */
                @Override
                public BillPaymentBuilderRef1 billerId(String billerId) {
                    validateNumeric("Biller ID", billerId);
                    validateLength("Biller ID", billerId, 15);
                    this.billerId = billerId;
                    return this;
                }

                /**
                 * Specify Reference 1
                 * @param ref1 Reference No. 1
                 * @return This builder.
                 */
                @Override
                public BillPaymentBuilderOptionalDetail ref1(String ref1) {
                    validateAlphanumeric("Reference 1", ref1);
                    validateLength("Reference 1", ref1, 15);
                    this.ref1 = ref1;
                    return this;
                }

                /**
                 * Specify Reference 2
                 * @param ref2 Reference No. 2
                 * @return This builder.
                 */
                @Override
                public BillPaymentBuilderOptionalDetail ref2(String ref2) {
                    validateAlphanumeric("Reference 2", ref2);
                    validateLength("Reference 2", ref2, 20);
                    this.ref2 = ref2;
                    return this;
                }

                /**
                 * Specify Terminal ID or Reference 3
                 * @param ref3 Reference No. 3
                 * @return This builder.
                 */
                @Override
                public BillPaymentBuilderOptionalDetail ref3(String ref3) {
                    validateAlphanumeric("Reference 3", ref3);
                    validateLength("Terminal ID/Reference 3", ref3, 26);
                    this.ref3 = ref3;
                    return this;
                }

                /**
                 * Specify amount in BigDecimal
                 * @param amount Transaction amount
                 * @return This builder.
                 */
                @Override
                public BillPaymentBuilderOptionalDetail amount(BigDecimal amount) {
                    validateLength("Amount", MONEY_FORMAT.format(amount), 13);
                    validateAmount(amount);
                    this.amount = amount;
                    return this;
                }

                /**
                 * Construct ThaiQRPromptPay object
                 * @return Returns an instance of ThaiQRPromptPay created from the fields set on this builder.
                 */
                @Override
                public ThaiQRPromptPay build() {
                    return new ThaiQRPromptPay(Builder.this);
                }
            }
        }
    }


}