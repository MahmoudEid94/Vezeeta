package Utilities;

import com.google.common.io.Files;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

public class Utilities {

    public static void captureScreenshot (WebDriver driver, String screenshotName){
        // casting the driver to takeScreenshot driver
        TakesScreenshot captureScreen = (TakesScreenshot) driver;

        // Capture the screenshot
        File source = captureScreen.getScreenshotAs(OutputType.FILE);

        // Add the date of execution to the screenshot name
        Date currntDate = new Date();
        String fullName = currntDate.toString().replace(" ","-").replace(":","-") +"_"+ screenshotName;

        // Define the destination of the screenshot
        String destination = "Resources/Screenshots/" + fullName + ".png";

        // move method cause IO exception, so we will put it in try catch
        try {
            Files.move(source, new File(destination));
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static String[][] readExcel(String excelFilePath, int sheetIndex) {
        String[][] data = null;

        try (FileInputStream fis = new FileInputStream(new File(excelFilePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            // Get the required sheet in the Excel workbook (index 0).
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            // Get the total number of rows in the sheet.
            int numRows = sheet.getLastRowNum();
            // Get the number of columns in the first row (assuming all rows have the same number of columns).
            int numCols = sheet.getRow(0).getPhysicalNumberOfCells();

            // Initialize the 2D array to store the Excel data.
            // Adjust the numRows to start from 1 to skip the title row.
            data = new String[numRows][numCols];

            // If Excel Sheet has a title row, put rowIdx = 1 to skip title row else keep it Zero
            for (int rowIdx = 1; rowIdx <= numRows; rowIdx++) {
                // Get the current row.
                Row row = sheet.getRow(rowIdx);

                if (row != null) {
                    for (int colIdx = 0; colIdx < numCols; colIdx++) {
                        // Get the current cell in the row.
                        Cell cell = row.getCell(colIdx);

                        if (cell != null) {
                            // Convert the cell's value to a string and store it in the data array.
                            // Subtract 1 from rowIdx as at beginning of for loop we skipped the title row
                            data[rowIdx-1][colIdx] = cell.toString();
                        } else {
                            // If the cell is empty, store an empty string in the data array.
                            // Subtract 1 from rowIdx as at beginning of for loop we skipped the title row
                            data[rowIdx-1][colIdx] = null;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
