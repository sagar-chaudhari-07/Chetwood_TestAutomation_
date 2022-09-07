package pageobjects;

import automation.library.common.BasePO;
import automation.library.core.Element;
import org.openqa.selenium.By;

public class BrokerHomePagePO extends BasePO {

    Element newApplication = findElementBy(By.xpath("//div[@class='row ']//a[@href='/Portal/Application/DisplayForm?formName=Apply%20-%20Terms%20And%20Conditions']"));

    public boolean startNewApplication() {
        newApplication.click();
        return true;
    }

}
