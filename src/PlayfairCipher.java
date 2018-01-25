import java.util.Scanner;

public class PlayfairCipher {

    private static char[][] charTable;
    private static int[][] positions;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String alphabetKey = keyMinLength(sc);
        System.out.print("Enter the message: ");
        String message = sc.nextLine();
        System.out.print("Replace J with I? yes/no: ");
        String jOrI = sc.nextLine();

        boolean skipLetter = userChoice(jOrI, sc);

        createCiperTable(alphabetKey, skipLetter);

        String encode = encodeMessage(prepareText(message, skipLetter));

        System.out.printf("%nEncoded message: %n%s%n", encode);
        System.out.printf("%nDecoded message: %n%s%n", decode(encode));
    }

    private static boolean userChoice(String jOrI, Scanner sc) {
        while ((!jOrI.matches("(?i)(yes)")) && (!jOrI.matches("(?i)(no)"))) {
            System.out.println("============ Wrong user input. Please enter yes or no. ============");
            System.out.print("Replace J with I? yes/no: ");
            jOrI = sc.nextLine();
        }
        boolean changeJtoI = jOrI.equalsIgnoreCase("yes");
        return changeJtoI;
    }

    private static String keyMinLength(Scanner sc) {
        String keyPhrase;
        do {
            System.out.print("Enter an encryption key (min length 6): ");
            keyPhrase = sc.nextLine().trim();
        } while (keyPhrase.length() < 6);
        return keyPhrase;
    }

    public static String prepareText(String alphabetKey, boolean changeJtoI) {
        alphabetKey = alphabetKey.toUpperCase().replaceAll("[^A-Z]", "");
        return changeJtoI ? alphabetKey.replace("J", "I") : alphabetKey.replace("Q", "");
    }

    private static void createCiperTable(String key, boolean changeJtoI) {
        String concatKeyWithAToZ = prepareText(key + "ABCDEFGHIJKLMNOPQRSTUVWXYZ", changeJtoI);
        charTable = new char[5][5];
        positions = new int[5][5];

        int length = concatKeyWithAToZ.length();
        for (int i = 0, count = 0; i < length; i++) {
            char letter = concatKeyWithAToZ.charAt(i);
            int firstOccurance = concatKeyWithAToZ.indexOf(letter);

            if (firstOccurance == i) {
                charTable[count / 5][count % 5] = letter;
                positions[count / 5][count % 5] = letter;
                count++;
            }
        }

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                System.out.print(charTable[row][col] + "  ");
//                System.out.print(positions[row][col] + "  ");
            }
            System.out.println();
        }
    }

    private static String encodeMessage(String encode) {
        StringBuilder sb = new StringBuilder(encode);

        for (int i = 0; i < sb.length(); i += 2) {

            if (i == sb.length() - 1)
                sb.append(sb.length() % 2 == 1 ? 'X' : "");

            else if (sb.charAt(i) == sb.charAt(i + 1))
                sb.insert(i + 1, 'X');
        }
        return codec(sb, 1);
    }

    private static String decode(String s) {
        return codec(new StringBuilder(s), 4);
    }

    private static String codec(StringBuilder text, int direction) {
        int row1 = 0;
        int row2 = 0;
        int col1 = 0;
        int col2 = 0;
        int length = text.length();
        for (int i = 0; i < length; i += 2) {
            char firstLetter = text.charAt(i);
            char secondLetter = text.charAt(i + 1);

            for (int row = 0; row < positions.length; row++) {
                for (int col = 0; col < positions.length; col++) {
                    if (firstLetter == positions[row][col]) {
                        row1 = row;
                        col1 = col;
                    } else if (secondLetter == positions[row][col]) {
                        row2 = row;
                        col2 = col;
                    }
                }
            }

            if (row1 == row2) {
                col1 = (col1 + direction) % 5;
                col2 = (col2 + direction) % 5;

            } else if (col1 == col2) {
                row1 = (row1 + direction) % 5;
                row2 = (row2 + direction) % 5;

            } else {
                int tmp = col1;
                col1 = col2;
                col2 = tmp;
            }

            text.setCharAt(i, charTable[row1][col1]);
            text.setCharAt(i + 1, charTable[row2][col2]);
        }
        return text.toString();
    }
}