package pageobjects;

import automation.library.common.BasePO;
import automation.library.common.TestConstants;
import automation.library.core.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.Arrays;
import java.util.Map;

import static automation.library.common.TestConstants.YES;

public class InitiateApplicationPO extends BasePO {

    String xpath_start = "//span[text()='";
    String xpath_end = "']";

    public boolean enterApplicationDetails(Map<String, String> data) {
        try {
            //Select correct details
            performChecks(data);

            //Enter Applicant Details
            enterApplicantInformation(data);

            //Enter Current Property Details
            enterCurrentPropDetails(data);

            //Enter Security Property Details
            enterSecurityPropDetails(data);

            //Request Credit Report
            requestCreditReport();
            approveCreditReportRequest();

            return true;
        } catch (Exception e) {
            BasePO.exceptionMsg = e.getMessage();
            BasePO.stackTraceMsg = Arrays.toString(e.getStackTrace());
            return false;
        }
    }

    private void performChecks(Map<String, String> data) {
        String applicantType = data.get("ApplicantType");
        String noOfApplicants = convertDoubleToString(data.get("NoOfApplicant"));
        String isPropertyInEnglandOrWales = data.get("IsPropInEnglandOrWales");
        String LoanPurpose = data.get("LoanPurpose");
        String isApplicantLivingInProperty = data.get("IsApplicantLivingInProperty");

        //Accept Terms and Conditions
        Element termsCondition = findElementBy(By.xpath("//div[contains(@class,'Application_TermsAndCondition')]"));
        Element creditCheckConsent = findElementBy(By.xpath("//div[contains(@class,'Application_CreditCheckConsent')]"));
        Element nextButton = findElementBy(By.xpath("//button[@onclick='submitFormAndNavigate(this);']"));
        termsCondition.click();
        creditCheckConsent.click();
        nextButton.click();

        //Select ApplicantType
        String xpath = xpath_start.concat(applicantType).concat(xpath_end);
        Element applicantTypeElement = findElementBy(By.xpath(xpath));
        applicantTypeElement.click();

        //Select Number Of Applicants
        Element noOfApplicantElement = findElementBy(By.name("Application.NumberOfApplicants"));
        noOfApplicantElement.clear();
        noOfApplicantElement.sendKeys(noOfApplicants);
        Element backToParentPage = findElementBy(By.tagName("h3"));
        backToParentPage.click();
        Element next = findElementBy(By.xpath("//div[@class='pull-right']"));
        next.click();

        //Select Property Location
        String xpath_isPropertyInEngland = xpath_start.concat(isPropertyInEnglandOrWales).concat(xpath_end);
        Element isPropertyInEnglandElement = findElementBy(By.xpath(xpath_isPropertyInEngland));
        isPropertyInEnglandElement.click();

        //Select Loan Purpose
        String xpath_LoanPurpose = xpath_start.concat(LoanPurpose).concat(xpath_end);
        Element loanPurposeElement = findElementBy(By.xpath(xpath_LoanPurpose));
        loanPurposeElement.click();

        //Select Applicant Living Status
        String xpath_isApplicantLivingInProperty = xpath_start.concat(isApplicantLivingInProperty).concat(xpath_end);
        Element isApplicantLivingInPropertyElement = findElementBy(By.xpath(xpath_isApplicantLivingInProperty));
        isApplicantLivingInPropertyElement.click();

        //Select Right to reside
        String rightToReside = data.get("RightToReside");
        Element rightToResideElement = findElementBy(By.xpath(xpath_start.concat(rightToReside).concat(xpath_end)));
        rightToResideElement.click();

        //Select Top Slicing
        String topSlicing = data.get("TopSlicing");
        String topSlicingXpath;
        if (topSlicing.equals(YES)) {
            topSlicingXpath = "//input[@type='radio'][@value='true']";
        } else {
            topSlicingXpath = "//input[@type='radio'][@value='false']";
        }
        Element topSlicingElement = findElementBy(By.xpath(topSlicingXpath));
        topSlicingElement.click();
        findElementBy(By.xpath("//button[@class='btn btn-default pull-right']")).click();
    }

    private void enterApplicantInformation(Map<String, String> data) {

        int noOfApplicants = Integer.parseInt(convertDoubleToString(data.get("NoOfApplicant")));

        //Enter Applicant Details
        if (data.get("ApplicantType").equals(TestConstants.APPLICANT_TYPE_COMPANY)) {
            String companyNo = convertDoubleToString(data.get("CompanyNo"));
            Element companyNoElement = findElementBy(By.name("Company.RegisteredNumber"));
            companyNoElement.sendKeys(companyNo);
            String sicCode = convertDoubleToString(data.get("SICCode"));
            Select sicCodeElement = findElementBy(By.id("Company_SICCode")).dropdown();
            sicCodeElement.selectByValue(sicCode);
        }

        for (int i = 1; i <= noOfApplicants; i++) {
            int id = i - 1;
            String applicantId = "Applicant[" + id + "]";

            String fName = data.get("FName" + i);
            String mName = data.get("MName" + i);
            String lName = data.get("LName" + i);
            Element fNameElement = findElementBy(By.name(applicantId + ".Applicant.FirstName"));
            Element mNameElement = findElementBy(By.name(applicantId + ".Applicant.MiddleName"));
            Element lNameElement = findElementBy(By.name(applicantId + ".Applicant.LastName"));
            fNameElement.sendKeys(fName);
            mNameElement.sendKeys(mName);
            lNameElement.sendKeys(lName);

            String postCode = data.get("PostCode" + i);
            Element postCodeElement = findElement(By.id(applicantId + "_Applicant_AddressPostCode"));
            postCodeElement.sendKeys(postCode);
            findElementBy(By.xpath("//button[@data-address-type='HomeAddress:" + i + "']")).click();
            String address1 = data.get("Address1_" + i);
            findElementBy(By.linkText(address1)).click();

            String dob = changeDateFormat(data.get("DOB" + i));
            Element dobElement = findElementBy(By.name(applicantId + ".Applicant.DateOfBirth_Date"));
            dobElement.sendKeys(dob);

            String nationality = data.get("Nationality" + i);
            Select nationalityDropdown = findElementBy(By.id(applicantId + "_Applicant_Nationality")).dropdown();
            nationalityDropdown.selectByVisibleText(nationality);

            String empStatus = data.get("EmpStatus" + i);
            Select empStatusDropdown = findElementBy(By.id(applicantId + "_Applicant_EmploymentClass")).dropdown();
            empStatusDropdown.selectByVisibleText(empStatus);

            String income = convertDoubleToString(data.get("Income" + i));
            findElementBy(By.id(applicantId + "_Employment_BasicGrossIncome")).sendKeys(income);

            String moveInDate = changeDateFormat(data.get("MoveDate" + i));
            findElementBy(By.name(applicantId + ".Applicant.AddressMovedDate_Date")).sendKeys(moveInDate);

            String taxBand = data.get("TaxBand" + i);
            Select taxBandDropdown = findElementBy(By.id(applicantId + "_Applicant_TaxPayer")).dropdown();
            taxBandDropdown.selectByVisibleText(taxBand);

            if (data.get("FirstTimeLandlord" + i).equals(YES)) {
                String xpath1 = applicantId + "_Applicant_FirstTimelandlord";
                findElementBy(By.xpath("//div[contains(@class,'" + xpath1 + "')]")).click();
            }
            if (data.get("FirstTimeBuyer" + i).equals(YES)) {
                String xpath2 = applicantId + "_Applicant_FirstTimeBuyer";
                findElementBy(By.xpath("//div[contains(@class,'" + xpath2 + "')]")).click();
            }

            String noOfDependents = convertDoubleToString(data.get("NoOfDependents" + i));
            findElementBy(By.id(applicantId + "_Applicant_NumberOfDependants")).clear().sendKeys(noOfDependents);

        }

    }


    private void approveCreditReportRequest() {
        mandatoryWait(300);
        String heading = "By clicking continue, you are approving to perform a credit bureau check on the supplied company and applicant data.";
        Assert.assertTrue(getPageSource().contains(heading));
        findElementBy(By.id("continueRequestCreditReport")).click();
    }


    private void enterCurrentPropDetails(Map<String, String> data) {
        String noOfMortgagedProp = convertDoubleToString(data.get("MortgagedProperties"));
        int numOfMortgagedProperties = Integer.parseInt(noOfMortgagedProp);
        findElementBy(By.id("Property_NoOfBuyToLetProperties")).clear().sendKeys(noOfMortgagedProp);
        if (numOfMortgagedProperties >= 3) {
            findElementBy(By.name("Property.TotalValuePortfolio")).sendKeys(data.get("TotalValuePortfolio"));
            findElementBy(By.name("Property.OutstandingMortgageBalance")).sendKeys(data.get("OutstandMortgBalance"));
            findElementBy(By.name("Property.TotalPortfolioMonthlyRental")).sendKeys(data.get("PortfolioMonthlyRent"));
        }
    }

    private void enterSecurityPropDetails(Map<String, String> data) {
        String sePropertyType = data.get("SEPropertyType");
        String purchasePrice = convertDoubleToString(data.get("PurchasePrice"));
        String loanAmount = convertDoubleToString(data.get("LoanAmount"));
        String monthlyRent = convertDoubleToString(data.get("MonthlyRent"));
        String exLocalAuthority = data.get("ExLocalAuthority");
        String decadeOldProp = data.get("10YearsOld");

        String sePropertyAddress = data.get("SEPropertyAddress");
        String sePinCode = data.get("SEPinCode");
        String seAddress1 = data.get("SEAddress1");
        String seCity = data.get("SECity");
        String seCountry = data.get("SECountry");

        if (sePropertyAddress.equals(YES)) {
            findElementBy(By.xpath("//div[contains(@class,'Security_AddressKnown')]")).click();

            findElementBy(By.id("Security_PropertyAddressPostCode")).sendKeys(sePinCode);
            findElementBy(By.xpath("//button[@data-address-type='SecurityAddress:1']")).click();
            Element seAddressElement = findElementBy(By.linkText(seAddress1));
            seAddressElement.click();

            Element security_propertyAddressCity = findElementBy(By.id("Security_PropertyAddressCity"));
            if (security_propertyAddressCity.getText().isEmpty()) {
                security_propertyAddressCity.sendKeys(seCity);
            }

            Select security_propertyType = findElementBy(By.id("Security_PropertyType")).dropdown();
            security_propertyType.selectByVisibleText(sePropertyType);
            findElementBy(By.id("Mortgage_PurchasePrice")).sendKeys(purchasePrice);
            findElementBy(By.id("Mortgage_LoanRequired")).sendKeys(loanAmount);
            findElementBy(By.id("Mortgage_MonthlyRentalIncome")).sendKeys(monthlyRent);

            if (exLocalAuthority.equals(YES)) {
                findElementBy(By.xpath("//div[contains(@class,'Security_OwnedByLocalAuthority')]")).click();
            }
            if (decadeOldProp.equals(YES)) {
                findElementBy(By.xpath("//div[contains(@class,'Security_IsPropertyNewBuild')]")).click();
            }

            Select security_propertyAddressCountry = findElementBy(By.id("Security_PropertyAddressCountry")).dropdown();
            security_propertyAddressCountry.selectByVisibleText(seCountry);
        }

    }

    private void requestCreditReport() {
        Element requestCreditReport = findElement(By.id("requestCreditReport"));
        requestCreditReport.click();
    }

}
