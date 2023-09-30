public class SpiralIterator2D {

    private static int DEFAULT_ROW_SIZE = 1;
    private static int DEFAULT_COL_SIZE = 1;

    private int rowSize = 1;
    private int colSize = 1;

    private boolean[][] isVisited;
    private int x;
    private int y;
    private int i;

    private int dx;
    private int dy;

    public SpiralIterator2D(int rowSize, int colSize, boolean startFromTopLeft) {
        setRowSize(rowSize);
        setColSize(colSize);

        isVisited = new boolean[colSize][rowSize];

        if (startFromTopLeft) {
            dy = 1;
            isVisited[0][0] = true;
        } else {
            dy = -1;
            x = rowSize - 1;
            y = colSize - 1;
            isVisited[colSize - 1][rowSize - 1] = true;
        }
    }


    public SpiralIterator2D(int rowSize, int colSize) {
        this(rowSize, colSize, true);
    }

    public int getRowSize() {
        return rowSize;
    }

    private void setRowSize(int rowSize) {
        this.rowSize = (rowSize > 0) ? rowSize : DEFAULT_ROW_SIZE;
    }

    public int getColSize() {
        return colSize;
    }

    private void setColSize(int colSize) {
        this.colSize = (colSize > 0) ? colSize : DEFAULT_COL_SIZE;

    }

    public boolean hasNext() {
        return i != rowSize * colSize;
    }

    public void advance() {
        if (!hasNext())
            return;

        int nextX = x + dx;
        int nextY = y + dy;

        boolean isOutOfBounds = nextX < 0 || nextX >= rowSize || nextY < 0 || nextY >= colSize;

        if (isOutOfBounds || isVisited[nextY][nextX]) {
            int oldDx = dx;
            int oldDy = dy;

            dx = oldDy;
            dy = -oldDx;

            nextX = x + dx;
            nextY = y + dy;
        }

        isVisited[nextY][nextX] = true;
        i++;
        x = nextX;
        y = nextY;
    }

    public int rowIndex() {
        return x;
    }

    public int columnIndex() {
        return y;
    }

    public int arrayIndex() {
        return i;
    }

}
