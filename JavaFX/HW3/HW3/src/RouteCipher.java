public class RouteCipher {

    private static int DEFAULT_KEY = 5;
    private static char DEFAULT_PAD = 'X';

    private int key;

    public RouteCipher() {
        this(DEFAULT_KEY);
    }

    public RouteCipher(int key) {
        setKey(key);
    }

    public RouteCipher(RouteCipher r) {
        this(r.key);
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = (key != 0) ? key : DEFAULT_KEY;
    }

    public String encrypt(String plainText) {
        char[][] matrix = generateMatrix(plainText);
        int rowSize = matrix[0].length;
        int colSize = matrix.length;

        SpiralIterator2D iterator = new SpiralIterator2D(rowSize, colSize, key > 0);
        char[] encryptedText = new char[rowSize * colSize];

        while (iterator.hasNext()) {
            int x = iterator.rowIndex();
            int y = iterator.columnIndex();
            int i = iterator.arrayIndex();

            encryptedText[i] = matrix[y][x];

            iterator.advance();
        }

        return new String(encryptedText);
    }

    public String decrypt(String cipherText) {
        char[] encryptedText = cipherText.toCharArray();
        int rowSize = calculateRowSize();
        int colSize = calculateColSize(encryptedText);

        char[][] matrix = new char[colSize][rowSize];
        SpiralIterator2D iterator = new SpiralIterator2D(rowSize, colSize, key > 0);

        while (iterator.hasNext()) {
            int x = iterator.rowIndex();
            int y = iterator.columnIndex();
            int i = iterator.arrayIndex();

            matrix[y][x] = encryptedText[i];

            iterator.advance();
        }

        char[] decrypted = matrixToArray(matrix);

        return new String(decrypted);
    }

    private char[] matrixToArray(char[][] matrix) {
        int rowSize = matrix[0].length;
        int colSize = matrix.length;
        char[] result = new char[rowSize * colSize];

        int k = 0;
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < rowSize; j++)
                result[k++] = matrix[i][j];

        return result;
    }

    private char[][] generateMatrix(String text) {
        char[] letters = text.toCharArray();
        int rowSize = calculateRowSize();
        int colSize = calculateColSize(letters);

        char[][] result = new char[colSize][rowSize];

        for (int i = 0; i < letters.length; i++) {
            int x = i % rowSize;
            int y = i / rowSize;

            result[y][x] = letters[i];
        }

        for (int i = letters.length; i < rowSize * colSize; i++) {
            int x = i % rowSize;
            int y = i / rowSize;

            result[y][x] = DEFAULT_PAD;
        }

        return result;
    }

    private int calculateRowSize() {
        return (key > 0) ? key : -key;
    }

    private int calculateColSize(char[] letters) {
        int rowSize = calculateRowSize();
        int size = letters.length / calculateRowSize();

        if (letters.length % rowSize != 0)
            size++;

        return size;
    }

    @Override
    public String toString() {
        return "RouteCipher with key: " + key;
    }

}
