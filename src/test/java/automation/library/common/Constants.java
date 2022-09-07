package automation.library.common;


/**
 * POJO used to define constants for parallel execution in selenium test
 */
public class Constants extends automation.library.core.Constants{
    public static final String SELENIUMSTACKSPATH = BASEPATH + "config/selenium/stacks/" + Property.getVariable("cukes.techstack") + ".json";
    public static final String TESTCASEPATH = BASEPATH + "scripts/testcases.xlsx";

    public static final String TESTDATAPATH = BASEPATH + "DataSet/";
}
