package lab.problem1;

public class CaesarCipher {

    private final static int ALPHABET_SIZE = 26;

    private final int shiftLength;

    public CaesarCipher(int shiftLength) {
        this.shiftLength = shiftLength;
    }

    private char shiftRight(char c, int size) {
        char ret = (char)(c + size);

        if (ret < 'A')
            ret = (char) (ret + ALPHABET_SIZE);

        if (ret > 'Z')
            ret = (char)(ret - ALPHABET_SIZE);

        return ret;
    }

    private String shiftRight(String text, int size) {
        char[] array = text.toCharArray();

        for (int i = 0; i < array.length; i++) {
            char currentCharacter = array[i];

            array[i] = shiftRight(currentCharacter, size);
        }

        return new String(array);
    }

    public String encode(String textToEncode) {
        return shiftRight(textToEncode, shiftLength);
    }

    public String decode(String textToDecode) {
        return shiftRight(textToDecode, -shiftLength);
    }

}
