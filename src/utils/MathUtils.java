package src.utils;

public class MathUtils {

    // Multiplica uma matriz 3x3 por um vetor 3x1 (inteiros)
    public static int[] multiplyMatrix(int[][] matrix, int[] vector) {
        int[] result = new int[3];
        for (int i = 0; i < 3; i++) {
            result[i] = 0;
            for (int j = 0; j < 3; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }

    // Multiplica uma matriz 3x3 por um vetor 3x1 (doubles)
    public static double[] multiplyMatrixVectorDouble(double[][] matrix, double[] vector) {
        double[] result = new double[3];
        for (int i = 0; i < 3; i++) {
            result[i] = 0;
            for (int j = 0; j < 3; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }
}
