package sillybaka.springframework.entity;

import lombok.Data;
import sillybaka.springframework.beans.factory.FactoryBean;

/**
 * Description：
 * Date: 2022/10/23
 * Time: 17:33
 *
 * @Author SillyBaka
 **/
public class CarFactory implements FactoryBean<Car> {

    @Override
    public Car getObject() {
        return new Car("宝马",200000,"666",new CarRoll("牛"));
    }

    @Override
    public Class<Car> getObjectType() {
        return Car.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
