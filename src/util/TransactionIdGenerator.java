package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TransactionIdGenerator {

    public static String generateTransactionId() {
// Format: TRX-YYYYMMDD-AB
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = LocalDateTime.now().format(dtf);
        Random random = new Random();
        String randomLetters = ""
                + (char) (65 + random.nextInt(26)) // A-Z
                + (char) (65 + random.nextInt(26)); // A-Z

        return "TRX-" + date + "-" + randomLetters;
    }

// Contoh pemakaian
    public static void main(String[] args) {
        System.out.println(generateTransactionId());
    }
}
