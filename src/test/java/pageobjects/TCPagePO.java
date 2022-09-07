package pageobjects;

import automation.library.common.BasePO;
import automation.library.core.Element;
import org.openqa.selenium.By;

public class TCPagePO extends BasePO {

    Element termsCondition = findElement(By.xpath("//div[contains(@class,'Application_TermsAndCondition')]"));
    Element creditCheckConsent = findElement(By.xpath("//div[contains(@class,'Application_CreditCheckConsent')]"));

    Element next = findElement(By.xpath("//button[@onclick='submitFormAndNavigate(this);']"));

    public boolean acceptTC() {
        termsCondition.click();
        creditCheckConsent.click();
        next.click();
        return true;
    }

}
