package pageobjects;

import automation.library.common.BasePO;
import automation.library.common.TestConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Arrays;
import java.util.Map;

public class CreditProfilePO extends BasePO {


    String xpath_first = "//span[text()='";

    String xpath_applicant_DefaultsCCJs = "']//preceding-sibling::input[@name='Applicant[0].CreditData.CreditDefaults']";
    String xpath_applicant_SecuredArrears = "']//preceding-sibling::input[@name='Applicant[0].CreditData.MissedMortgagePayments']";
    String xpath_applicant_UnsecuredArrears = "']//preceding-sibling::input[@name='Applicant[0].CreditData.MissedUnsecuredPayments']";
    String xpath_applicant_bankruptcy = "']//preceding-sibling::input[@name='Applicant[0].CreditData.Bankruptcy']";
    String xpath_applicant_repossession = "']//preceding-sibling::input[@name='Applicant[0].CreditData.Repossession']";

    String xpath_company_DefaultsCCJs = "']//preceding-sibling::input[@name='CreditData.CompanyCreditDefaults']";
    String xpath_company_SecuredArrears = "']//preceding-sibling::input[@name='CreditData.CompanyMissedMortgagePayments']";
    String xpath_company_UnsecuredArrears = "']//preceding-sibling::input[@name='CreditData.CompanyMissedUnsecuredPayments']";
    String xpath_company_repossession = "']//preceding-sibling::input[@name='CreditData.CompanyRepossession']";
    String xpath_company_disqualifiedDirector = "']//preceding-sibling::input[@name='CreditData.DisqualifiedDirector']";
    String xpath_company_legalNotice = "']//preceding-sibling::input[@name='CreditData.LegalNotices']";

    public boolean enterCreditProfileDetails(Map<String, String> data) {
        try {
            mandatoryWait(5000);
            enterApplicantCreditProfileDetails(data);
            if (data.get("ApplicantType").equals(TestConstants.APPLICANT_TYPE_COMPANY)) {
                enterCompanyCreditProfileDetails(data);
            }
            clickSelectProduct();
            return true;
        } catch (Exception e) {
            BasePO.exceptionMsg = e.getMessage();
            BasePO.stackTraceMsg = Arrays.toString(e.getStackTrace());
            return false;
        }
    }

    private void clickSelectProduct() {
        findElement(By.xpath("//button[@data-action='CreditProfile']")).click();
    }

    private void enterApplicantCreditProfileDetails(Map<String, String> data) {
        mandatoryWait(3000);
        String applicant_defaultCCJs = xpath_first + data.get("Applicant Defaults") + xpath_applicant_DefaultsCCJs;
        findElementBy(By.xpath(applicant_defaultCCJs)).click();
        String applicant_securedArrears = xpath_first + data.get("Applicant Secured Arrears") + xpath_applicant_SecuredArrears;
        findElementBy(By.xpath(applicant_securedArrears)).click();
        String applicant_unsecuredArrears = xpath_first + data.get("Applicant Unsecured Arrears") + xpath_applicant_UnsecuredArrears;
        findElementBy(By.xpath(applicant_unsecuredArrears)).click();
        String applicant_bankruptcy = xpath_first + data.get("Applicant Bankruptcy") + xpath_applicant_bankruptcy;
        findElementBy(By.xpath(applicant_bankruptcy)).click();
        String applicant_repossession = xpath_first + data.get("Applicant Repossessions") + xpath_applicant_repossession;
        findElementBy(By.xpath(applicant_repossession)).click();
    }

    private void enterCompanyCreditProfileDetails(Map<String, String> data) {
        String company_defaultCCJs = xpath_first + data.get("Company Defaults") + xpath_company_DefaultsCCJs;
        findElementBy(By.xpath(company_defaultCCJs)).click();
        String company_securedArrears = xpath_first + data.get("Company Secured Arrears") + xpath_company_SecuredArrears;
        findElementBy(By.xpath(company_securedArrears)).click();
        String company_unsecuredArrears = xpath_first + data.get("Company Unsecured Arrears") + xpath_company_UnsecuredArrears;
        findElementBy(By.xpath(company_unsecuredArrears)).click();
        String company_repossession = xpath_first + data.get("Company Repossession") + xpath_company_repossession;
        findElementBy(By.xpath(company_repossession)).click();
        String company_director = xpath_first + data.get("Disqualified Director") + xpath_company_disqualifiedDirector;
        findElementBy(By.xpath(company_director)).click();
        String company_legalNotice = xpath_first + data.get("Legal Notices") + xpath_company_legalNotice;
        findElementBy(By.xpath(company_legalNotice)).click();
    }

}
