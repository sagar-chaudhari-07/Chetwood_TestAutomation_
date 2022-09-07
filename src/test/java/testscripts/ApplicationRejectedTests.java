package testscripts;

import automation.library.common.BaseTest;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import pageobjects.*;

import java.util.Map;

import static org.testng.Assert.assertTrue;

@Ignore
public class ApplicationRejectedTests extends BaseTest {

    LoginPagePO loginPagePO;
    private boolean beReturn = false;

    @Test(dataProvider = "TestDataEnv")
    public void companyTest(Map<String, String> map, Map<String, String> data) {
        getDriver().get(testAppUrl_UAT);
        loginPagePO = new LoginPagePO();
        beReturn = loginPagePO.loginAsBroker();
        reportStatus(beReturn, "Login is successful", "Login is failed");

        BrokerHomePagePO brokerHomePagePO = new BrokerHomePagePO();
        beReturn = brokerHomePagePO.startNewApplication();
        reportStatus(beReturn, "Proceeding with new application", "Failed to proceed for new Application");

        InitiateApplicationPO initiateApplicationPO = new InitiateApplicationPO();
        beReturn = initiateApplicationPO.enterApplicationDetails(data);
        reportStatus(beReturn, "Initiated application successfully ", "Failed to initiate application");

        CreditProfilePO creditProfilePO = new CreditProfilePO();
        beReturn = creditProfilePO.enterCreditProfileDetails(data);
        reportStatus(beReturn, "Credit Profile is submitted successfully ", "Failed to provide Credit Profile");
        assertTrue(beReturn);

        DIPProductsPO dipProductsPO = new DIPProductsPO();
        beReturn = dipProductsPO.enterDIPProductDetails(data);
        reportStatus(beReturn,"Product Selection criteria is submitted successfully","Failed to enter product selection criteria");
        assertTrue(beReturn);

        SelectProductsPO selectProductsPO = new SelectProductsPO();
        beReturn = selectProductsPO.selectProductDetails(data);
        reportStatus(beReturn, "Product selected successfully","Failed to select mortgage product.");
        assertTrue(beReturn);

        DIPDownloadPO dipDownloadPO = new DIPDownloadPO();
        beReturn = dipDownloadPO.downloadDIPApplication(data);
        reportStatus(beReturn,"Downloaded DIP Application successfully","Failed to download DIP Application");
        assertTrue(beReturn);

        ApplicationDashboardPO applicationDashboardPO = new ApplicationDashboardPO();
        String dipRefNumber = applicationDashboardPO.getDIPRefNumber();
        data.put("DIPRefNumber",dipRefNumber);
        //beReturn = applicationDashboardPO.setDIPRefNumberInFile(testFilePath, testWorksheet, data);
        reportStatus(beReturn,"Saved DIP Application Reference Number successfully","Failed to save DIP Application Reference Number");
        assertTrue(beReturn);
        applicationDashboardPO.validateAndExitApplication(data);

        System.out.println(data);
        System.out.println("--------SORTED----------------------");
        System.out.println(BaseTest.sortMap(data));

        String applicationRejectedMsg = "Sorry, we are not able to accept applications where an applicant lives in the property.";
        assertTrue(getPageSource().contains(applicationRejectedMsg));
    }

    @Ignore
    @Test(dataProvider = "TestDataEnv")
    public void individualTest(Map<String, String> map, Map<String, String> data) {
        getDriver().get(testAppUrl_UAT);
        loginPagePO = new LoginPagePO();
        beReturn = loginPagePO.loginAsBroker();
        reportStatus(beReturn, "Login is successful", "Login is failed");

        BrokerHomePagePO brokerHomePagePO = new BrokerHomePagePO();
        beReturn = brokerHomePagePO.startNewApplication();
        reportStatus(beReturn, "Proceeding with new application", "Failed to proceed for new Application");

        InitiateApplicationPO initiateApplicationPO = new InitiateApplicationPO();
        beReturn = initiateApplicationPO.enterApplicationDetails(data);
        reportStatus(beReturn, "Initiated application successfully ", "Failed to initiate application");

        CreditProfilePO creditProfilePO = new CreditProfilePO();
        creditProfilePO.enterCreditProfileDetails(data);

        String applicationRejectedMsg = "Sorry, we are not able to accept applications where an applicant lives in the property.";
        assertTrue(getPageSource().contains(applicationRejectedMsg));
    }


}
