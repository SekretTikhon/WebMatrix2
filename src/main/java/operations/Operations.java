package operations;

import csv.CSV;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Operations {
    public static String Process(Map<String, String> params) {
        int n = Integer.parseInt(params.get("n"));
        int m = Integer.parseInt(params.get("m"));
        double[][] matrix = new double[n][m];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                String key = ""+ i + "_" + j;
                matrix[i][j] = Double.parseDouble(params.get(key));
            }
        }
        double[][] result = new double[0][0];
        if (params.get("method").equals("Gauss")) {
            result = Gauss(matrix);
        } else
        if (params.get("method").equals("Mult_I")) {
            result = Mult_I(matrix);
        } else
        if (params.get("method").equals("Mult_Random")) {
            result = Mult_Random(matrix);
        } else
        if (params.get("method").contains("Mult_")) {
            String filename = "matrixs/" + params.get("method").split("_", 2)[1] + ".csv";
            result = Mult_Curent(matrix, filename);
            System.out.println("Mult_Curent от " + filename);
        }

        //print result

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }


        String filename = saveToFile(result);
        return filename;
    }
    public static double[][] Gauss(double[][] Matrix) {
        int n = Matrix.length;
        int m = Matrix[0].length;
        if (n != m) return null;
        //double res[][] = new double[n][n];

        double[][] xirtaM = new double[n][n]; //Единичная матрица (искомая обратная матрица)
        for (int i = 0; i < n; i++)
            xirtaM[i][i] = 1;

        double[][] Matrix_Big = new double[n][2*n]; //Общая матрица, получаемая скреплением Начальной матрицы и единичной
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            {
                Matrix_Big[i][j] = Matrix[i][j];
                Matrix_Big[i][j + n] = xirtaM[i][j];
            }

        //Прямой ход (Зануление нижнего левого угла)
        for (int k = 0; k < n; k++) //k-номер строки
        {
            for (int i = 0; i < 2*n; i++) //i-номер столбца
                Matrix_Big[k][i] = Matrix_Big[k][i] / Matrix[k][k]; //Деление k-строки на первый член !=0 для преобразования его в единицу
            for (int i = k + 1; i < n; i++) //i-номер следующей строки после k
            {
                double K = Matrix_Big[i][k] / Matrix_Big[k][k]; //Коэффициент
                for (int j = 0; j < 2*n; j++) //j-номер столбца следующей строки после k
                    Matrix_Big[i][j] = Matrix_Big[i][j] - Matrix_Big[k][j] * K; //Зануление элементов матрицы ниже первого члена, преобразованного в единицу
            }
            for (int i = 0; i < n; i++) //Обновление, внесение изменений в начальную матрицу
                for (int j = 0; j < n; j++)
                    Matrix[i][j] = Matrix_Big[i][j];
        }

        //Обратный ход (Зануление верхнего правого угла)
        for (int k = n - 1; k > -1; k--) //k-номер строки
        {
            for (int i = 2*n - 1; i > -1; i--) //i-номер столбца
                Matrix_Big[k][i] = Matrix_Big[k][i] / Matrix[k][k];
            for (int i = k - 1; i > -1; i--) //i-номер следующей строки после k
            {
                double K = Matrix_Big[i][k] / Matrix_Big[k][k];
                for (int j = 2*n - 1; j > -1; j--) //j-номер столбца следующей строки после k
                    Matrix_Big[i][j] = Matrix_Big[i][j] - Matrix_Big[k][j] * K;
            }
        }

        //Отделяем от общей матрицы
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                xirtaM[i][j] = Matrix_Big[i][j + n];
        return xirtaM;
    }
    public static double[][] Mult_I(double[][] matrix) {
        int m = matrix[0].length;
        return multiply(matrix, getI(m));
    }
    public static double[][] Mult_Random(double[][] matrix) {
        int m = matrix[0].length;
        return multiply(matrix, getRandomRazr(m));
    }
    public static double[][] Mult_Curent(double[][] matrix, String filename) {
        return multiply(matrix, CSV.fromCSV(filename));
    }
    public static String saveToFile(double[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        String filename = "result.csv";
        CSV.toCSV(matrix, n, m, filename);
        return filename;
    }
    public static double[][] multiply(double[][] A, double[][] B) {// A: nAxn; B:nxnB
        int A_n = A.length;
        int A_m = A[0].length;
        int B_n = B.length;
        int B_m = B[0].length;
        if (A_m != B_n) return new double[0][0];

        double[][] result = new double[A_n][B_m];
        for (int i = 0; i < A_n; ++i)
            for (int j = 0; j < B_m; ++j)
                for (int k = 0; k < A_m; ++k)
                    result[i][j] += A[i][k] * B[k][j];
        return result;
    }
    public static double[][] getI(int n) {
        double[][] result = new double[n][n];
        for (int i=0; i<n; ++i)
            result[i][i] = 1;
        return result;
    }
    public static double[][] getRandomRazr(int n) {
        double[][] result = new double[n][n];
        for (int i=0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (Math.random() < 1.0 / Math.sqrt(n))
                    result[i][i] = -1.0 + Math.random() * 2;
        return result;
    }
}
