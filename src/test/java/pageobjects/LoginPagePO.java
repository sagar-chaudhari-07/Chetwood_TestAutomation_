package pageobjects;

import automation.library.common.BasePO;
import automation.library.core.Element;
import org.openqa.selenium.By;

import java.util.HashMap;

public class LoginPagePO extends BasePO {

    Element email = findElementBy(By.xpath("//input[@id='Email']"));
    Element password = findElementBy(By.xpath("//input[@id='Password']"));
    Element submit = findElementBy(By.xpath("//input[@type='submit']"));

    public boolean loginAsBroker() {
        HashMap<String, String> brokerUserCredentials = getBrokerUserCredentials();
        email.sendKeys(brokerUserCredentials.get("user"));
        password.sendKeys(brokerUserCredentials.get("pwd"));
        submit.click();
        return true;
    }

    public String getAppUrl() {
        return getTestAppUrl();
    }


}
