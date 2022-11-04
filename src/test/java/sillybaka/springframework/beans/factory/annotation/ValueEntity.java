package sillybaka.springframework.beans.factory.annotation;

import sillybaka.springframework.context.stereotype.Component;

/**
 * Description：
 * <p>Date: 2022/11/4
 * <p>Time: 20:06
 *
 * @Author SillyBaka
 **/
@Component
public class ValueEntity {

    @Value("${niubiaaa}")
    private String testString;

    @Value("666")
    private double testDouble;

    public ValueEntity(){

    }

    public void hello(){
        System.out.println(testString);
        System.out.println(testDouble);
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public void setTestDouble(double testDouble) {
        this.testDouble = testDouble;
    }

    public String getTestString() {
        return testString;
    }

    public double getTestDouble() {
        return testDouble;
    }
}
