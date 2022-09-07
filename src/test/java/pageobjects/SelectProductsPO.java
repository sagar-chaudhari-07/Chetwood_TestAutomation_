package pageobjects;

import automation.library.common.BasePO;
import automation.library.core.Element;
import org.openqa.selenium.By;

import java.util.*;

public class SelectProductsPO extends BasePO {


    public boolean selectProductDetails(Map<String, String> data) {
        try {
            mandatoryWait(5000);
            selectProduct(data);
            enterBrokerFees(data);
            issueDIP();
            return true;
        } catch (Exception e) {
            BasePO.exceptionMsg = e.getMessage();
            BasePO.stackTraceMsg = Arrays.toString(e.getStackTrace());
            return false;
        }
    }

    private void enterBrokerFees(Map<String, String> data) {
        String brokerFees = convertDoubleToString(data.get("BrokerFees"));
        findElementBy(By.id("Broker_FixedAmount")).sendKeys(brokerFees);
    }

    private void issueDIP() {
        findElement(By.xpath("//button[@onclick='submitFormToAction(this);']")).click().click();
        //mandatoryWait(5000);
    }

    private void selectProduct(Map<String, String> data) {
        Element tableProducts = findElement(By.id("tableproducts"));
        List<Element> rows = tableProducts.findElements(By.tagName("tr"));
        String productSelectionCriteria = data.get("ProductSelectionCriteria");
        String criteria = getProductBySelectionCriteria(rows, productSelectionCriteria).substring(1);
        if (productSelectionCriteria.equals("LowestMonthlyPayment")) {
            String xpath_selectButton = "//td[text()='" + criteria + "']//following-sibling::td//input";
            findElementBy(By.xpath(xpath_selectButton)).click();
        }
    }

    private String getProductBySelectionCriteria(List<Element> rows, String productSelectionCriteria) {
        ArrayList<String> monthlyPaymentList = new ArrayList<>();
        String lowestMonthlyPayment = null;
        if (productSelectionCriteria.equals("LowestMonthlyPayment")) {
            for (int i = 1; i < rows.size(); i++) {
                String xpath_monthlyPayment = "//table//tbody//tr[" + i + "]//td[8]";
                String emi = findElementBy(By.xpath(xpath_monthlyPayment))
                        .getText();
                String pay = emi.substring(1).trim();
                monthlyPaymentList.add(formatPay(pay));
            }
            Collections.sort(monthlyPaymentList);
            lowestMonthlyPayment = monthlyPaymentList.get(0);

        }
        return "£ " + convertToOriginal(lowestMonthlyPayment);
    }

    private String formatPay(String pay) {
        if (pay.length() == 6) {
            return "0," + pay;
        }
        return pay;
    }

    private String convertToOriginal(String pay) {
        if (pay.contains("0,")) {
            return pay.substring(2);
        }
        return pay;
    }

}
