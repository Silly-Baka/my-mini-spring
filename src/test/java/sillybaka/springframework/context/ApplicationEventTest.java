package sillybaka.springframework.context;

import org.junit.Test;
import sillybaka.springframework.context.support.ClassPathXmlApplicationContext;
import sillybaka.springframework.entity.Car;
import sillybaka.springframework.entity.CarRoll;
import sillybaka.springframework.entity.PlayCarEvent;
import sillybaka.springframework.entity.PlayCarEventListener;

import java.util.concurrent.Executors;

/**
 * Description：用于测试事件机制的测试类
 * Date: 2022/10/24
 * Time: 15:16
 *
 * @Author SillyBaka
 **/
public class ApplicationEventTest {

    /**
     * 测试ApplicationContext内部自动发布的事件
     */
    @Test
    public void testAutoEvent() throws InterruptedException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:testEvent.xml");

        Thread.sleep(2000);

        classPathXmlApplicationContext.close();
    }

    @Test
    public void testManualEvent() throws InterruptedException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext();
        PlayCarEvent playCarEvent = new PlayCarEvent(new Car("宝马", 200000, "666", new CarRoll("阿奇")));
        PlayCarEventListener playCarEventListener = new PlayCarEventListener();

        classPathXmlApplicationContext.addApplicationListener(playCarEventListener);
        System.out.println("监听器已添加");

        Thread.sleep(2000);

        classPathXmlApplicationContext.publishEvent(playCarEvent);
        System.out.println("事件已发布");
    }

    /**
     * 测试异步处理事件
     */
    @Test
    public void testExecutorEvent() throws InterruptedException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext();
        classPathXmlApplicationContext.addTaskExecutor(Executors.newSingleThreadExecutor());

        PlayCarEvent playCarEvent = new PlayCarEvent(new Car("宝马", 200000, "666", new CarRoll("阿奇")));
        PlayCarEventListener playCarEventListener = new PlayCarEventListener();

        classPathXmlApplicationContext.addApplicationListener(playCarEventListener);
        System.out.println("监听器已添加");

        Thread.sleep(2000);

        classPathXmlApplicationContext.publishEvent(playCarEvent);
        System.out.println("事件已发布");

        Thread.sleep(4000);
    }
}
