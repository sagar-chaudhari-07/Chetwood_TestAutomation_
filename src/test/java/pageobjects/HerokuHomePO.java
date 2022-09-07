package pageobjects;

import automation.library.common.BasePO;
import automation.library.core.Locator;
import org.openqa.selenium.By;

public class HerokuHomePO extends BasePO {

    static By heading = By.tagName("h3");
    static String menu = "//li/a[contains(text(),'%s')]";

    public void pickMenu(String val) {
        $(Locator.Loc.XPATH, menu, val).click();
    }

    public String getHeading(){
        String val = $(heading).getText();
        return val;
    }
}
