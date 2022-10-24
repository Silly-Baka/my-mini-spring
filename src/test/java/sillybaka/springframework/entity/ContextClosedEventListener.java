package sillybaka.springframework.entity;

import lombok.extern.slf4j.Slf4j;
import sillybaka.springframework.context.ApplicationListener;
import sillybaka.springframework.context.event.ContextClosedEvent;

/**
 * Description：
 * Date: 2022/10/24
 * Time: 15:28
 *
 * @Author SillyBaka
 **/
@Slf4j
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("上下文:{}已关闭",event);
    }
}
