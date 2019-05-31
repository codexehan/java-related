package codexe.han.elasticsearch.test;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ReadExcel {
    /**
     * 读取xls文件
     * @throws IOException
     */
    public static void readXls() throws IOException
    {
        FileInputStream in = new FileInputStream("/Users/zhenchao/Documents/influencer_synonyms_20190524.xls");
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
    public static void readXlsx() throws IOException
    {
        XSSFWorkbook xwb = new XSSFWorkbook("/Users/zhenchao/Documents/influencer_synonyms_20190524.xlsx");
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
        //readXls();
        readXlsx();
    }

}
