package codexe.han.elasticsearch.test;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadExcel {
    /**
     * 读取xls文件
     * @throws IOException
     */
    public static void readXls(String path) throws IOException
    {
        FileInputStream in = new FileInputStream(path);
        HSSFWorkbook book = new HSSFWorkbook(in);

        HSSFSheet sheet = book.getSheetAt(0);
        HSSFRow row;
        String cell;

        for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++)
        {
            row = sheet.getRow(i);
            for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++)
            {
                cell = row.getCell(j).toString();
                System.out.print(cell + " \t");
            }
            System.out.println("");
        }
    }

    /**
     * 读取xlsx文件
     * @throws IOException
     */
    public static void readXlsx(String path) throws IOException
    {
        XSSFWorkbook xwb = new XSSFWorkbook(path);
        XSSFSheet sheet = xwb.getSheetAt(0);
        XSSFRow row;
        String cell;
        int count=0;
        for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++)
        {
            row = sheet.getRow(i);
            String leftStr = "";
            String rightStr = "";
            for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++)
            {
                if(j!=0) {
                    if(!row.getCell(j).toString().isEmpty()){
                        if(j==2) {
                            rightStr = " => " + row.getCell(j).toString();
                        }
                        else{
                            if(j==1) {
                                leftStr = row.getCell(j).toString();
                            }
                            else{
                                leftStr = leftStr+", "+row.getCell(j).toString();
                            }
                        }
                    }
                }

            }
            if(!leftStr.isEmpty()) {
                System.out.println(leftStr + rightStr+",");
                count++;

            }
            //System.out.println("");
        }
        System.out.println(count);
    }

    public static void main(String[] args) throws IOException
    {
      //  String path = "/Users/zhenchao/Documents/influencer_synonyms_20190524.xls";

      //  readXlsx(path);
        String path = "/Users/zhenchao/Downloads/0730/n&adj.csv";
        readCsv(path);
    }


    public static void readCsv(String path){
        Path pathToFile = Paths.get(path);

        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String line = br.readLine();

            // loop until all lines are read
            while (line != null) {

                // use string.split to load a string array with the values from
                // each line of
                // the file, using a comma as the delimiter
                line=line.replace("\"","");
                String[] attributes = line.split(",");
                System.out.println(Arrays.toString(attributes));

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
