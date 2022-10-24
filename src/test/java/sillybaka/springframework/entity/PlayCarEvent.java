package sillybaka.springframework.entity;

import sillybaka.springframework.context.event.PayloadApplicationEvent;

/**
 * Description：
 * Date: 2022/10/24
 * Time: 15:50
 *
 * @Author SillyBaka
 **/
public class PlayCarEvent extends PayloadApplicationEvent<Car> {

    public PlayCarEvent(Car payload) {
        super(payload);
    }
}
