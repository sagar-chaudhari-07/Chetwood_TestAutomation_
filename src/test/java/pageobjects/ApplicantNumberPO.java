package pageobjects;

import automation.library.common.BasePO;
import automation.library.core.Element;
import org.openqa.selenium.By;

public class ApplicantNumberPO extends BasePO {

    Element next = $(By.xpath("//div[@class='pull-right']"));
    Element noOfApplicants = $(By.xpath("//input[@name='Application.NumberOfApplicants']"));

    public boolean enterNoOfApplicants(String noOfApplicants){
        this.noOfApplicants.sendKeys(noOfApplicants);
        next.click();
        return true;
    }

}
