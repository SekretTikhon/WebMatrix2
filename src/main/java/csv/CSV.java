package csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CSV {

    public static void toCSV(double[][] matrix, int n, int m, String filename) {
        CSVWriter writer = null;
        try {
            List<String[]> lines = new LinkedList<>();
            for (int i = 0; i < n; i++) {
                String[] line = new String[m];
                for (int j = 0; j < m; j++) {
                    line[j] = String.valueOf(matrix[i][j]);
                }
                lines.add(line);

            }
            writer = new CSVWriter(new FileWriter(filename));
            writer.writeAll(lines);
            writer.close();
            System.out.println("toCSV done");
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static double[][] fromCSV(String filename) {
        List<List<Double>> matrix = new ArrayList<>();

        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(filename));
            String [] nextLine;
            int i = 0;
            //Read one line at a time
            while ((nextLine = reader.readNext()) != null)
            {
                ArrayList<Double> line =  new ArrayList<>();
                int j = 0;
                for(String token : nextLine)
                {
                    Double value = Double.parseDouble(token);
                    line.add(j++, value);
                }
                matrix.add(i++, line);
            }
            reader.close();
        }
        catch (IOException e) { }
        int n = matrix.size();
        double res[][] = new double[n][];
        for (int i = 0; i < n; i++) {
            int m = matrix.get(i).size();
            res[i] = new double[m];
            for (int j = 0; j < m; j++) {
                res[i][j] = matrix.get(i).get(j);
            }
        }
        return res;
    }

}
