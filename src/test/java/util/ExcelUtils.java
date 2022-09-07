package util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtils {

    public static Cell getCellByText(String filepath, String worksheet, String searchText) {
        try {
            FileInputStream file = new FileInputStream(filepath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet(worksheet);
            DataFormatter dataFormatter = new DataFormatter();
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (dataFormatter.formatCellValue(cell).equals(searchText)) {
                        return cell;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean setCellValue(String filepath, String worksheet, int row, String columnName, String insertText) {
        int columnNumber = ExcelUtils.getCellByText(filepath, worksheet, columnName).getColumnIndex();
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(filepath);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            fis.close();

            XSSFSheet sheet = workbook.getSheet(worksheet);
            XSSFCell cell = sheet.getRow(row).getCell(columnNumber);
            cell.setCellValue(insertText);
            fos = new FileOutputStream(filepath);
            workbook.write(fos);
            fos.close();
            workbook.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fis!=null){
                    fis.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                if (fos!=null){
                    fos.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
