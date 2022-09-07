package automation.library.common;

import automation.library.common.driver.factory.DriverContext;
import automation.library.common.driver.factory.DriverFactory;
import automation.library.core.Screenshot;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import util.ExcelUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class to hold start up (set test context) and tear down (shut down browser) for selenium test. This class should be
 * extended by each test class in the test project
 */
public class BaseTest {

    protected Logger log = LogManager.getLogger(this.getClass().getName());
    protected BasePO po;
    protected TestContext context = TestContext.getInstance();
    public String testAppUrl_UAT = Property.getProperties(automation.library.core.Constants.SELENIUMRUNTIMEPATH).getString("testAppUrl");
    public String reportPath = Property.getProperties(automation.library.core.Constants.SELENIUMRUNTIMEPATH).getString("reportPath")
            + LocalDate.now() + "_" +Instant.now().getEpochSecond();


    WebDriver driver;
    public static ExtentReports extentReport;
    public ExtentTest extentTest;
    public static ExtentHtmlReporter reporter;

    public String testClassName,testFilePath,testWorksheet;

    @BeforeSuite(alwaysRun=true)
    public void setUp(){
        reporter = new ExtentHtmlReporter(reportPath+"/extentReport.html");
        reporter.config().setAutoCreateRelativePathMedia(true);
        extentReport = new ExtentReports();
        extentReport.attachReporter(reporter);
//        test = extentReport.createTest("ApplicationRejectedTest");
//        reportPath = reportPath + LocalDateTime.now();
    }

    @AfterTest(alwaysRun=true)
    public void tearDown(){
        extentReport.flush();
    }

    public String getPageSource() {
        return DriverFactory.getInstance().getDriver().getPageSource();
    }

    /**
     * return web driver for the current thread - can be used when running using TestNG
     */
    public WebDriver getDriver() {
        log.debug("obtaining the driver for current thread");
        this.driver =  DriverFactory.getInstance().getDriver();
        return driver;
    }

    /**
     * return web driver wait for the current thread - can be used when running using TestNG
     */
    public WebDriverWait getWait() {
        log.debug("obtaining the wait for current thread");
        return DriverFactory.getInstance().getWait();
    }

    /**
     * return BasePO instance - can be used when running using TestNG
     */
    public BasePO getPO(){
        log.debug("obtaining an instance of the base page object");
        if (this.po == null) {
            this.po = new BasePO();
        }
        return po;
    }

    public void setPO() {
        log.debug("obtaining an instance of the base page object");
        this.po = new BasePO();
    }

    /**
     * SoftAssert instance from TestContext to be used - can be used when running using TestNG
     */
    protected SoftAssert sa() {
        return TestContext.getInstance().sa();
    }

    /**
     * Read the 'tech stack' for a given test run and enable parallel execution from json file
     */
    @DataProvider(name = "techStackJSON", parallel = true)
    public Object[][] techStackJSON() throws Exception {
        log.debug("spinning up parallel execution threads for multi browser testing");
        JSONArray jsonArr = JsonHelper.getJSONArray(Constants.SELENIUMSTACKSPATH);
        Object[][] obj = new Object[jsonArr.length()][1];
        Gson gson = new GsonBuilder().create();

        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            @SuppressWarnings("unchecked")
            Map<String, String> map = gson.fromJson(jsonObj.toString(), Map.class);
            obj[i][0] = map;
        }

        return obj;
    }

    /**
     * Read the 'tech stack' for a given test run and enable parallel execution from excel file
     */
    @DataProvider(name = "techStackExcel", parallel = true)
    public Object[][] techStackExcel() throws Exception {
        log.debug("spinning up parallel execution threads for multi browser testing");

        ArrayList<ArrayList<Object>> browsers = ExcelHelper.getDataAsArrayList(Constants.TESTCASEPATH, "browsers", new String[0]);
        Object[][] obj = new Object[browsers.size() - 1][1];

        for (int i = 1; i < browsers.size(); ++i) {
            Map<String, String> map = new HashMap();
            for (int j = 0; j < browsers.get(0).size(); j++) {
                map.put(browsers.get(0).get(j).toString(), browsers.get(i).get(j).toString());
            }
            obj[i - 1][0] = map;
        }

        return obj;
    }
    @DataProvider(name = "TestDataEnv", parallel = false)
    public Object[][] techStackAndData(Method method) throws Exception {
        log.debug("spinning up parallel execution threads for multi browser testing");
        JSONArray jsonArr = JsonHelper.getJSONArray(Constants.SELENIUMSTACKSPATH);
        Gson gson = new GsonBuilder().create();
        String testClassNm = StringUtils.substringAfterLast(method.getDeclaringClass().getName(), ".");
        ArrayList<ArrayList<Object>> testSet = ExcelHelper.getDataAsArrayList(Constants.TESTDATAPATH +testClassNm+ ".xlsx", method.getName());
        Object[][] obj = new Object[jsonArr.length() * (testSet.size() - 1)][2];
        for (int i = 0; i < jsonArr.length(); ++i) {
            JSONObject jsonObj = jsonArr.getJSONObject(i);
            @SuppressWarnings("unchecked")
            Map<String, String> mapBrowser = gson.fromJson(jsonObj.toString(), Map.class);
            for (int j = 1; j < testSet.size(); ++j) {
                Map<String, String> testConfig = new HashMap();
                for (int k = 0; k < testSet.get(0).size(); k++) {
                    testConfig.put(testSet.get(0).get(k).toString(), testSet.get(j).get(k).toString());
                }
                testConfig.put("RowNumber",String.valueOf(j));
                obj[(j-1) + (i * testSet.size())][0] = mapBrowser;
                obj[(j-1) + (i * testSet.size())][1] = testConfig;
            }
        }
        return obj;
    }

    /**
     * set the test context information
     */
    @BeforeMethod(alwaysRun=true)
    public void startUp(Method method, Object[] args) {
        Test test = method.getAnnotation(Test.class);

        Map<String, String> map = (Map<String, String>) args[args.length - 2];
        if (!TestContext.getInstance().fwSpecificData().containsKey("fw.cucumberTest"))
            TestContext.getInstance().putFwSpecificData("fw.testDescription", test.description() + " (" + map.get("description") + ")");
        if (Property.getVariable("PROJECT_NAME") != null && !Property.getVariable("PROJECT_NAME").isEmpty())
            TestContext.getInstance().putFwSpecificData("fw.projectName", Property.getVariable("PROJECT_NAME"));
        if (Property.getVariable("BUILD_NUMBER") != null && !Property.getVariable("BUILD_NUMBER").isEmpty())
            TestContext.getInstance().putFwSpecificData("fw.buildNumber", Property.getVariable("BUILD_NUMBER"));
        DriverContext.getInstance().setDriverContext(map);

        extentTest = extentReport.createTest(method.getName());
        extentTest.info("Test is started");

        testClassName = StringUtils.substringAfterLast(method.getDeclaringClass().getName(), ".");
        testFilePath = Constants.TESTDATAPATH + testClassName + ".xlsx";
        testWorksheet = method.getName();
    }

    /**
     * if cucumber test the update the status and removes the current thread's value for this thread-local
     */
    @AfterMethod(alwaysRun=true)
    public void closeDown(ITestResult result) {

        if(result.getStatus() == ITestResult.FAILURE) {
            if (BasePO.exceptionMsg != null && BasePO.stackTraceMsg.length()>0){
                extentTest.fail(BasePO.exceptionMsg);
                extentTest.fail(result.getThrowable());
                extentTest.fail(BasePO.stackTraceMsg);
            } else {
                extentTest.fail(result.getThrowable());
            }
            String screenshot = takeScreenshot(result.getMethod().getMethodName());
            try {
                extentTest.fail("", MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if(result.getStatus() == ITestResult.SUCCESS) {
            extentTest.log(Status.PASS, "Test is successful");
        }
        else {
            extentTest.log(Status.SKIP, "Test is skipped");
        }


                if (!TestContext.getInstance().fwSpecificData().containsKey("fw.cucumberTest")) {
            DriverFactory.getInstance().driverManager().updateResults(result.isSuccess() ? "passed" : "failed");
            DriverFactory.getInstance().quit();
        }
        finaliseSelenium2JmxRecording();
    }


    private void finaliseSelenium2JmxRecording(){
        if(Property.getVariable("cukes.enableHar2Jmx") != null && Property.getVariable("cukes.enableHar2Jmx").equalsIgnoreCase("true")) {
            try {
                Class<?> har2jmxHelperClass = Class.forName("automation.library.conversion2jmx.selenium" + "." + "Har2JmxBaseTest");

                Method method = har2jmxHelperClass.getMethod("finaliseRecording");
                Object newInstance = har2jmxHelperClass.getDeclaredConstructor().newInstance();
                method.invoke(newInstance);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Unable to find automation.library.conversion2jmx.selenium.Har2JmxBaseTest");
                log.error("The library-conversion2jmx is needed in order to allow HAR recording");
                throw new RuntimeException(e);
            }
        }
    }

    public void reportStatus(boolean status, String passMsg, String failMsg){
        if (status){
            extentTest.info(passMsg);
        }else {
            extentTest.fail(failMsg);
        }
    }

    public String takeScreenshot(String fileName){
        File file = Screenshot.grabScrollingScreenshot(this.driver);
        File screenshotFile = Screenshot.saveScreenshot(file, reportPath + "//", fileName);
        return screenshotFile.getPath();
    }

    public void mandatoryWait(int seconds){
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map sortMap(Map<String,String> unsorted){
        TreeMap<String,String> sorted = new TreeMap<>(unsorted);
        return sorted;
    }

//    public boolean setDIPRefNumberInFile(String testFilePath, String testWorksheet, Map<String,String> data){
//        try {
//            String filePath = testFilePath;
//            String worksheet = testWorksheet;
//            String dipRefNumber = data.get("DIPRefNumber");
//            String address1 = data.get("Address1");
//            Cell property_address_cell = ExcelUtils.getCellByText(filePath, worksheet, address1);
//            Cell dipRefNumber_cell = ExcelUtils.getCellByText(filePath, worksheet, "DIPRefNumber");
//
//            System.out.println(filePath+worksheet+property_address_cell.getRowIndex()+dipRefNumber_cell.getColumnIndex()+dipRefNumber);
//            ExcelUtils.setCellValue(filePath, worksheet, property_address_cell.getRowIndex(), dipRefNumber_cell.getColumnIndex(), dipRefNumber);
//            return true;
//        } catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }

}
