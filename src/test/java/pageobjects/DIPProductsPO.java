package pageobjects;

import automation.library.common.BasePO;
import automation.library.core.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import java.util.Arrays;
import java.util.Map;

import static automation.library.common.TestConstants.YES;

public class DIPProductsPO extends BasePO {


    public boolean enterDIPProductDetails(Map<String, String> data) {
        try {
            enterMortgageDetails(data);
            enterMandatoryDetails(data);
            return true;
        } catch (Exception e) {
            BasePO.exceptionMsg = e.getMessage();
            BasePO.stackTraceMsg = Arrays.toString(e.getStackTrace());
            return false;
        }
    }

    private void enterMortgageDetails(Map<String, String> data) {
        if (data.get("MortgageClubApplicable").equals(YES)) {
            findElement(By.xpath("//span[@class='bootstrap-switch-label']")).click();
            Select application_mortgageClub = findElementBy(By.id("Application_MortgageClub")).dropdown();
            application_mortgageClub.selectByVisibleText(data.get("MortgageClub"));
        }
    }

    private void enterMandatoryDetails(Map<String, String> data) {
        Element noOfYearsToRepayElement = findElementBy(By.id("Product_NumberOfYearsToRepay"));
        noOfYearsToRepayElement.clear();
        noOfYearsToRepayElement.sendKeys(convertDoubleToString(data.get("RepaymentTerm")));
        mandatoryWait(1000);

        Select product_loanRepaymentType = findElementBy(By.id("Product_LoanRepaymentType")).dropdown();
        product_loanRepaymentType.selectByVisibleText(data.get("RepaymentType"));
        mandatoryWait(1000);

        Select product_productFeePaymentType = findElementBy(By.id("Product_ProductFeePaymentType")).dropdown();
        product_productFeePaymentType.selectByVisibleText(data.get("ProductFeePaymentType"));
        mandatoryWait(3000);
    }


}
