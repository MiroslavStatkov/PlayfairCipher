import java.util.Scanner;

public class PlayfairCipher {

    private static char[][] charTable;
    private static int[][] positions;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String keyPhrase = keyMinLength(sc);
        System.out.print("Enter the message: ");
        String message = sc.nextLine();
        System.out.print("Replace J with I? yes/no: ");
        String jOrI = sc.nextLine();

        boolean skipLetter = userChoice(jOrI, sc);

        createCiperTable(keyPhrase, skipLetter);

        String encode = insertX(prepareText(message, skipLetter));

        System.out.printf("%nEncoded message: %n%s%n", encode);
        System.out.printf("%nDecoded message: %n%s%n", decodeMessage(encode));
    }

    private static boolean userChoice(String jOrI, Scanner sc) {
        while ((!jOrI.matches("(?i)(yes)")) && (!jOrI.matches("(?i)(no)"))) {
            System.out.println("============ Wrong user input. Please enter yes or no. ============");
            System.out.print("Replace J with I? yes/no: ");
            jOrI = sc.nextLine();
        }
        return jOrI.equalsIgnoreCase("yes");
    }

    private static String keyMinLength(Scanner sc) {
        String keyPhrase;
        do {
            System.out.print("Enter an encryption key (min length 6): ");
            keyPhrase = sc.nextLine().trim();
        } while (keyPhrase.length() < 6);
        return keyPhrase;
    }

    private static String prepareText(String text, boolean changeJtoI) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");
        return changeJtoI ? text.replace("J", "I") : text.replace("Q", "");
    }

    private static void createCiperTable(String key, boolean changeJtoI) {
        String concatKeyWithMessage = prepareText(key + "ABCDEFGHIJKLMNOPQRSTUVWXYZ", changeJtoI);
        charTable = new char[5][5];
        positions = new int[5][5];

        int length = concatKeyWithMessage.length();
        for (int i = 0, count = 0; i < length; i++) {
            char letter = concatKeyWithMessage.charAt(i);
            int firstOccurance = concatKeyWithMessage.indexOf(letter);

            if (firstOccurance == i) {
                charTable[count / 5][count % 5] = letter;
                positions[count / 5][count % 5] = letter;
                count++;
            }
        }

        System.out.println();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                System.out.print(charTable[row][col] + "  ");
            }
            System.out.println();
        }
    }

    private static String insertX(String message) {
        StringBuilder sb = new StringBuilder(message);

        for (int i = 0; i < sb.length(); i += 2) {

            if (i == sb.length() - 1)
                sb.append(sb.length() % 2 == 1 ? 'X' : "");

            else if (sb.charAt(i) == sb.charAt(i + 1))
                sb.insert(i + 1, 'X');
        }
        return encodeMessage(sb, 1);
    }

    private static String decodeMessage(String message) {
        return encodeMessage(new StringBuilder(message), 4);
    }

    private static String encodeMessage(StringBuilder message, int direction) {
        int firstLetterRow = 0;
        int secondLetterRow = 0;
        int firstLetterCol = 0;
        int secondLetterCol = 0;

        int length = message.length();
        for (int i = 0; i < length; i += 2) {
            char firstLetter = message.charAt(i);
            char secondLetter = message.charAt(i + 1);

            for (int row = 0; row < positions.length; row++) {
                for (int col = 0; col < positions.length; col++) {
                    if (firstLetter == positions[row][col]) {
                        firstLetterRow = row;
                        firstLetterCol = col;
                    } else if (secondLetter == positions[row][col]) {
                        secondLetterRow = row;
                        secondLetterCol = col;
                    }
                }
            }

            if (firstLetterRow == secondLetterRow) {
                firstLetterCol = (firstLetterCol + direction) % 5;
                secondLetterCol = (secondLetterCol + direction) % 5;

            } else if (firstLetterCol == secondLetterCol) {
                firstLetterRow = (firstLetterRow + direction) % 5;
                secondLetterRow = (secondLetterRow + direction) % 5;

            } else {
                int temp = firstLetterCol;
                firstLetterCol = secondLetterCol;
                secondLetterCol = temp;
            }

            message.setCharAt(i, charTable[firstLetterRow][firstLetterCol]);
            message.setCharAt(i + 1, charTable[secondLetterRow][secondLetterCol]);
        }
        return message.toString();
    }
}