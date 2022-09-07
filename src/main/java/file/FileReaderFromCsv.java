package file;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class FileReaderFromCsv{

    public static List<String[]> readFile(String filePath){
        List<String[]> allData = new ArrayList<>();
         try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader(filePath);

            // create csvReader object passing
            // file reader as a parameter
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            // we are going to read data line by line
//            while ((nextRecord = csvReader.readNext()) != null) {
//                for (String cell : nextRecord) {
//                    System.out.print(cell + "\t");
//                }
//                System.out.println();
//            }

             allData = csvReader.readAll();
         }
        catch (Exception e) {
            e.printStackTrace();
        }

         return allData;
    }
}
