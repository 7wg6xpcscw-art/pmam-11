import java.util.Random;

public class MatrixMultiplication {

    // Метод для створення матриці розміром rows x cols
    public static int[][] createMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rand.nextInt(10); // числа від 0 до 9
            }
        }
        return matrix;
    }

    // Метод для виводу матриці
    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.printf("%4d", val);
            }
            System.out.println();
        }
    }

    // Метод для множення матриць
    public static int[][] multiplyMatrices(int[][] A, int[][] B) {
        int rowsA = A.length;
        int colsA = A[0].length;
        int rowsB = B.length;
        int colsB = B[0].length;

        if (colsA != rowsB) {
            throw new IllegalArgumentException("Кількість стовпців першої матриці повинна дорівнювати кількості рядків другої матриці!");
        }

        int[][] result = new int[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // Розміри матриць
        int rowsA = 3, colsA = 3;
        int rowsB = 3, colsB = 3;

        // Створюємо матриці
        int[][] matrixA = createMatrix(rowsA, colsA);
        int[][] matrixB = createMatrix(rowsB, colsB);

        System.out.println("Матриця A:");
        printMatrix(matrixA);

        System.out.println("\nМатриця B:");
        printMatrix(matrixB);

        // Множення матриць
        int[][] result = multiplyMatrices(matrixA, matrixB);

        System.out.println("\nРезультат множення A * B:");
        printMatrix(result);

        // Додатково: обчислюємо суму всіх елементів результату
        int sum = 0;
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                sum += result[i][j];
            }
        }
        System.out.println("\nСума всіх елементів результату: " + sum);
    }
}
