package pageobjects;

import automation.library.common.BasePO;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import java.util.Arrays;
import java.util.Map;

import static automation.library.common.TestConstants.YES;

public class DIPDownloadPO extends BasePO {

    public boolean enterApplicantQuestionnaire(Map<String, String> data) {
        String incomeFromEmployment = data.get("IncomeFromEmployment");
        String incomeFromPension = data.get("IncomeFromPension");
        if (incomeFromEmployment.equals(YES)) {
            findElementBy(By.xpath("//div[contains(@class,'Applicant[0]_IncomeSource_IncomeFromEmployment')]")).click();
            Select empStatus = findElementBy(By.id("Applicant[0]_IncomeSource_EmploymentStatus")).dropdown();
            empStatus.selectByVisibleText(data.get("EmpStatus2"));
        }
        if (incomeFromPension.equals(YES)) {
            findElementBy(By.xpath("//div[contains(@class,'Applicant[0]_IncomeSource_IncomeFromPension')]")).click();
        }

        findElementBy(By.xpath("//button[@class='btn btn-default submit-button blueBtn pull-right']")).click();
        return true;
    }

    public boolean downloadDIPApplication(Map<String, String> data) {
        try {
            findElementBy(By.xpath("//button[@data-action='DownloadIllustrationDocument']")).click();
            findElementBy(By.xpath("//button[@onclick='NavigateToForm(this);']")).click();
            enterApplicantQuestionnaire(data);
            return true;
        } catch (Exception e) {
            BasePO.exceptionMsg = e.getMessage();
            BasePO.stackTraceMsg = Arrays.toString(e.getStackTrace());
            return false;
        }
    }

}
