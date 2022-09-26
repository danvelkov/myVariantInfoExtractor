package file;

import com.opencsv.CSVWriter;

import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class FileWriterForCsv {

    public static synchronized void writeDataLineByLine(String filePath, String[] header, String[] line)
    {
        // first create file object for file placed at location
        // specified by filepath
        File file = new File(filePath);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter (file, true);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile, ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER);

            // adding header to csv
            if(file.length() == 0)
                writer.writeNext(header);

            // add data to csv

            writer.writeNext(line);

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
