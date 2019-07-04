# Thai QR PromptPay Generator
ไลบรารีช่วยสร้าง QR สำหรับรับชำระเงินด้วย PromptPay โดยสามารถสร้าง
1. QR สำหรับบุคคลธรรมดารับโอนเงิน (Credit Transfer)
2. QR สำหรับธุรกิจรับชำระเงิน (Bill Payment)

# การใช้งาน
1. QR สำหรับบุคคลธรรมดารับโอนเงิน (Credit Transfer)

```java
// รับชำระผ่าน PromptPay เบอร์ 081-234-5678 จำนวนเงิน: <ให้ผู้ชำระระบุ>
ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().staticQR().creditTransfer().mobileNumber("0812345678").build();

// รับชำระผ่าน PromptPay เลขประจำตัวประชาชน 0-0000-00000-00-0 จำนวนเงิน: <ให้ผู้ชำระระบุ>
ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().staticQR().creditTransfer().nationalId("0000000000000").build();

// รับชำระผ่าน PromptPay E-Wallet ID 000000000000000 จำนวนเงิน: <ให้ผู้ชำระระบุ>
ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().staticQR().creditTransfer().eWalletId("000000000000000").build();

// รับชำระผ่าน PromptPay เบอร์ 081-234-5678 จำนวนเงิน: 10 บาท
ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().staticQR().creditTransfer().mobileNumber("0812345678").amount(new BigDecimal("10.00")).build();

// รับชำระผ่าน PromptPay เลขประจำตัวประชาชน 0-0000-00000-00-0 จำนวนเงิน: 136.25 บาท
ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().staticQR().creditTransfer().nationalId("0000000000000").amount(new BigDecimal("136.25")).build();

// รับชำระผ่าน PromptPay E-Wallet ID 000000000000000 จำนวนเงิน: 0.01 บาท
ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().staticQR().creditTransfer().eWalletId("000000000000000").amount(new BigDecimal("0.01")).build();
```

2. QR สำหรับธุรกิจรับชำระเงิน (Bill Payment)

```java
// รับชำระผ่าน PromptPay เลขประจำตัวผู้เสียภาษี 0000000000000 + 99 (SUFFIX 2 หลัก)
// รหัสอ้างอิง 1: 15123141 รหัสอ้างอิง 2: 3654112 จำนวนเงิน: <ให้ผู้ชำระระบุ>
ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().dynamicQR().billPayment().billerId("000000000000099").ref1("15123141").ref2("3654112").build();

// รับชำระผ่าน PromptPay เลขประจำตัวผู้เสียภาษี 0000000000000 + 99 (SUFFIX 2 หลัก)
// รหัสอ้างอิง 1: 15123141 รหัสอ้างอิง 2: 3654112 จำนวนเงิน: 200.00 บาท
ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().dynamicQR().billPayment().billerId("000000000000099").ref1("15123141").ref2("3654112").amount(new BigDecimal("200.00")).build();

// รับชำระผ่าน PromptPay เลขประจำตัวผู้เสียภาษี 0000000000000 + 99 (SUFFIX 2 หลัก)
// รหัสอ้างอิง 1: 15123141 รหัสอ้างอิง 2: 3654112 รหัสอ้างอิง 3: ABCD จำนวนเงิน: 200.00 บาท
ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().dynamicQR().billPayment().billerId("000000000000099").ref1("15123141").ref2("3654112").ref3("ABCD").amount(new BigDecimal("200.00")).build();

// รับชำระผ่าน PromptPay เลขประจำตัวผู้เสียภาษี 0000000000000 + 99 (SUFFIX 2 หลัก)
// รหัสอ้างอิง 1: 15123141 รหัสอ้างอิง 3: ABCD จำนวนเงิน: 100.00 บาท
ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().dynamicQR().billPayment().billerId("000000000000099").ref1("15123141").ref3("ABCD").amount(new BigDecimal("100.00")).build();

```

# ข้อพึงสังเกต
- QR ที่ใช้ชำระครั้งเดียวสำหรับ Bill Payment ควรระบุเป็น dynamicQR()

# วิธีนำไปใช้
1. Import .jar ของไลบราลีนี้เข้าสู่ project [ดาวน์โหลดได้ที่นี่](https://github.com/pheerathach/promptpay/raw/master/promptpay.jar)
2. เรียกใช้ไลบรารี
```java
ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().dynamicQR().creditTransfer().mobileNumber("0812345678").amount(new BigDecimal("100.00")).build();
```
2.1. หากต้องการเนื้อหาสำหรับนำไปสร้างรูป QR
```java
String content = qr.generateContent();
```
2.2. หากต้องการให้ไลบรารีสร้างรูป QR ให้

  2.2.1. เป็นไฟล์ PNG
```java
qr.draw(300, 300, new File("D:\\qr.png")); // width 300 x height 300 pixels | save to D:\qr.png
```

  2.2.2. เป็น Base64 String
```java
qr.drawToBase64(300, 300); // width 300 x height 300 pixels
```

  2.2.3. เป็น OutputStream
```java
qr.drawToOutputStream(400, 400); // width 400 x height 400 pixels
```

# เอกสารอ้างอิง
- มาตรฐานการรับชำระเงินด้วย QR ของธนาคารแห่งประเทศไทย

https://www.bot.or.th/Thai/FIPCS/Documents/FPG/2562/ThaiPDF/25620084.pdf

- ขอขอบคุณ คุณ thedayofcondor สำหรับ method ในการหา CRC16

https://stackoverflow.com/questions/13209364/convert-c-crc16-to-java-crc16/13209435

