package testscripts;

import automation.library.common.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import pageobjects.InitiateApplicationPO;
import pageobjects.BrokerHomePagePO;
import pageobjects.LoginPagePO;
import pageobjects.TCPagePO;

import java.util.Map;

@Ignore
public class LoginTests extends BaseTest{

    LoginPagePO loginPagePO;
    public boolean beReturn=false;

    @Test(dataProvider = "TestDataEnv")
    public void companyTest(Map<String, String> map, Map<String, String> data) {

        getDriver().get(testAppUrl_UAT);
        loginPagePO = new LoginPagePO();

        beReturn = loginPagePO.loginAsBroker();
        reportStatus(beReturn,"Login is successful","Login is failed");

        BrokerHomePagePO brokerHomePagePO = new BrokerHomePagePO();
        beReturn = brokerHomePagePO.startNewApplication();
        reportStatus(beReturn,"Proceeding with new application","Failed to proceed for new Application");

        TCPagePO tcPagePO = new TCPagePO();
        beReturn = tcPagePO.acceptTC();
        reportStatus(beReturn,"Accepted Terms and Conditions","Failed to accept terms and conditions");

        InitiateApplicationPO initiateApplicationPO = new InitiateApplicationPO();
        beReturn = initiateApplicationPO.enterApplicationDetails(data);
        reportStatus(beReturn,"Initiated application successfully ","Failed to initiate application");

        String applicationRejectedMsg = "Sorry, we are not able to accept applications where an applicant lives in the property.";
        Assert.assertTrue(getPageSource().contains(applicationRejectedMsg));
    }

    @Ignore
    @Test(dataProvider = "TestDataEnv")
    public void individualTest(Map<String, String> map, Map<String, String> data) {
        extentTest.info("Test started");

        getDriver().get(testAppUrl_UAT);
        loginPagePO = new LoginPagePO();

        beReturn = loginPagePO.loginAsBroker();
        reportStatus(beReturn,"Login is successful","Login is failed");

        BrokerHomePagePO brokerHomePagePO = new BrokerHomePagePO();
        beReturn = brokerHomePagePO.startNewApplication();
        reportStatus(beReturn,"Proceeding with new application","Failed to proceed for new Application");

        TCPagePO tcPagePO = new TCPagePO();
        beReturn = tcPagePO.acceptTC();
        reportStatus(beReturn,"Accepted Terms and Conditions","Failed to accept terms and conditions");

        InitiateApplicationPO initiateApplicationPO = new InitiateApplicationPO();
        beReturn = initiateApplicationPO.enterApplicationDetails(data);
        reportStatus(beReturn,"Initiated application successfully ","Failed to initiate application");

        String applicationRejectedMsg = "Sorry, we are not able to accept applications where an applicant lives in the property.";
        Assert.assertTrue(getPageSource().contains(applicationRejectedMsg));
    }



}
