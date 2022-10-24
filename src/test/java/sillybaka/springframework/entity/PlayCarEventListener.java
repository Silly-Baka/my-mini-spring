package sillybaka.springframework.entity;

import sillybaka.springframework.context.ApplicationListener;

/**
 * Description：
 * Date: 2022/10/24
 * Time: 15:54
 *
 * @Author SillyBaka
 **/
public class PlayCarEventListener implements ApplicationListener<PlayCarEvent> {
    @Override
    public void onApplicationEvent(PlayCarEvent event) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("你在玩一种很新的车");
        System.out.println(event.getPayload());

        System.out.println("事件已完成");
    }
}
