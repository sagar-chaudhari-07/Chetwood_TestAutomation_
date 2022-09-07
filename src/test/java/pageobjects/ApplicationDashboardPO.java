package pageobjects;

import automation.library.common.BasePO;
import automation.library.common.TestConstants;
import org.apache.poi.ss.usermodel.Cell;
import org.openqa.selenium.By;
import util.ExcelUtils;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ApplicationDashboardPO extends BasePO {

    String DIPRefNumber;
    String xpath_DipRefNumber = "//strong[text()='Ref: ']//parent::h4";
    String xpath_applicationStage = "//strong[text()='Stage: ']//parent::h4";
    String xpath_reissueDIP = "//a[contains(@href,'/Portal/Application/RegenerateDIP')]";

    public boolean validateAndExitApplication(Map<String, String> data) {
        try {

            assertTrue(findElementBy(By.id("loggedinUserInfo")).getText().trim().equals("Broker Admin UAT"));
            assertTrue(findElementBy(By.xpath("//h2[@class='dashboard-header pull-left']")).getText().trim().equals("Application Dashboard"));
            assertTrue(findElementBy(By.xpath(xpath_reissueDIP)).getText().trim().equals("Reissue DIP"));
            assertTrue(findElementBy(By.xpath(xpath_applicationStage)).getText().trim().equals("Stage: New"));
            assertTrue(findElementBy(By.xpath("//button[@class='btn btn-default dropdown-toggle']")).getText().trim().equals("More Actions"));

            //validate company info
            if (data.get("ApplicantType").equals(TestConstants.APPLICANT_TYPE_COMPANY)) {
                assertTrue(findElementBy(By.xpath("//div[@class='form-block company']/div[1]/span[2]/span[2]")).getText().trim().equals("INCOMPLETE"));
                String regNum = findElementBy(By.xpath("//span[@for='reg-num']//parent::div")).getText();
                assertEquals(regNum, "Registration Number " + convertDoubleToString(data.get("CompanyNo")));
            }

            findElementBy(By.linkText("EXIT")).click();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getDIPRefNumber() {
        try {
            DIPRefNumber = findElementBy(By.xpath(xpath_DipRefNumber)).getText().substring(5);
            System.out.println("DipRefNUmber :" + DIPRefNumber);
            return DIPRefNumber;
        } catch (Exception e) {
            return null;
        }
    }

//    public boolean setDIPRefNumberInFile(String testFilePath, String testWorksheet, Map<String, String> data) {
//        int rowNum=0;
//        try {
//            String filePath = testFilePath;
//            String worksheet = testWorksheet;
//            String dipRefNumber = data.get("DIPRefNumber");
//            String address1 = data.get("Address1");
//            Cell property_address_cell = ExcelUtils.getCellByText(filePath, worksheet, address1);
//            Cell dipRefNumber_cell = ExcelUtils.getCellByText(filePath, worksheet, "DIPRefNumber");
//            ExcelUtils.setCellValue(filePath, worksheet, property_address_cell.getRowIndex(), dipRefNumber_cell.getColumnIndex(), dipRefNumber);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

//    public boolean saveDIPRefNumberInFile(String testFilePath, String testWorksheet, int rowNumber, String dipRefNumber) {
//        try {
//            Cell dipRefNumber_cell = ExcelUtils.getCellByText(testFilePath, testWorksheet, "DIPRefNumber");
//            ExcelUtils.setCellValue(testFilePath, testWorksheet, rowNumber, dipRefNumber_cell.getColumnIndex(), dipRefNumber);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

}
