# Thai QR PromptPay Generator

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/51564ca097fa4cbba46e34ecacd813e7)](https://www.codacy.com/gh/pheerathach/promptpay/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=pheerathach/promptpay&amp;utm_campaign=Badge_Grade)

ไลบรารีช่วยสร้าง QR สำหรับรับชำระเงินด้วย PromptPay โดยสามารถสร้าง
1. QR สำหรับบุคคลธรรมดารับโอนเงิน (Credit Transfer)
2. QR สำหรับธุรกิจรับชำระเงิน (Bill Payment)

**ไลบรารีนี้ต้องการ Java เวอร์ชัน 8 ขึ้นไป**

# การใช้งาน
1. QR สำหรับบุคคลธรรมดารับโอนเงิน (Credit Transfer) Tag 29

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

2. QR สำหรับธุรกิจรับชำระเงิน (Bill Payment) Tag 30

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

3. QR สำหรับธุรกิจรับชำระเงิน (Bill Payment) ตามมาตรฐานเดิมของธปท.

```java
// รับชำระแบบ Bill Payment เดิมตามธปท. เลขประจำตัวผู้เสียภาษี 0000000000000 + 99 (SUFFIX 2 หลัก)
// รหัสอ้างอิง 1: 15123141 รหัสอ้างอิง 2: 3654112 จำนวนเงิน: <ให้ผู้ชำระระบุ>
ThaiQRPromptPay qr = new ThaiQRPromptPay.Builder().bot().billPayment().billerId("000000000000099").ref1("15123141").ref2("3654112").build();
```

# ข้อพึงสังเกต
- QR ที่ถูกสร้างขึ้นเพื่อให้ใช้ได้หลายครั้ง คือ staticQR()
- QR ที่ถูกสร้างขึ้นเพื่อให้ใช้ได้แค่ครั้งเดียว คือ dynamicQR()

# วิธีนำไปใช้
1. นำเข้าไลบรารีผ่าน Maven โดยเพิ่มโค้ดดังกล่าวใน pom.xml
```
<dependencies>
...
		<dependency>
			<groupId>com.github.pheerathach</groupId>
			<artifactId>promptpay</artifactId>
			<version>1.0.2</version>
		</dependency>
</dependencies>
```
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

  2.2.2. เป็น Base64 String (PNG)
```java
qr.drawToBase64(300, 300); // width 300 x height 300 pixels
```

  2.2.3. เป็น byte[]
```java
qr.drawToByteArray(400, 400); // width 400 x height 400 pixels
```

# เอกสารอ้างอิง
- มาตรฐานการรับชำระเงินด้วย QR ของธนาคารแห่งประเทศไทย

https://www.bot.or.th/Thai/FIPCS/Documents/FPG/2562/ThaiPDF/25620084.pdf

- ZXing 3.4.0

https://github.com/zxing/zxing

- Apache Commons Codec 1.12

https://commons.apache.org/proper/commons-codec/

- ขอขอบคุณ คุณ thedayofcondor สำหรับ method ในการหา CRC16

https://stackoverflow.com/questions/13209364/convert-c-crc16-to-java-crc16/13209435

