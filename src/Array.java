/**
 * Object that offers better two dimensional array support for Java's ints
 *
 * @author Rich Kirk
 */
class Array {
    private final int[][] array;

    /**
     * Default constructor
     *
     * @param rows number of rows for the array
     * @param cols number of columns for the array
     */
    Array(int rows, int cols) {
        array = new int[rows][cols];
    }

    /**
     * Receives a row and returns the one dimensional array of that row.
     *
     * @param row row of array desired
     * @return one dimensional array of the given {@code row}
     */
    int[] getRow(int row) {
        return array[row];
    }

    /**
     * Receives a column and returns the one dimensional array of that column.
     *
     * @param col column of array desired
     * @return one dimensional array of the given {@code col}
     */
    int[] getCol(int col) {
        int[] colArray = new int[array[0].length];
        for (int r = 0; r < array.length; r++) {
            colArray[r] = array[r][col];
        }
        return colArray;
    }

    /**
     * Returns number of rows in this
     *
     * @return row size
     */
    int rowSize() {
        return array.length;
    }

    /**
     * Returns number of columns in this
     *
     * @return column size
     */
    int colSize() {
        return array[0].length;
    }

    /**
     * Returns the element at the specified row and column
     *
     * @param row row of desired element
     * @param col column of desired element
     * @return element at the given {@code row} and {@code col}
     */
    int getElement(int row, int col) {
        return array[row][col];
    }


    /**
     * Sets the given element to a specified value
     *
     * @param row row of desired element
     * @param col column of desired element
     * @param value value to assign to the element of the given index
     */
    void setElement(int row, int col, int value) {
        array[row][col] = value;
    }
}
