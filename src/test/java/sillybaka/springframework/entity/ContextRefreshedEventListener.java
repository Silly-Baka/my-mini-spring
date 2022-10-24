package sillybaka.springframework.entity;

import lombok.extern.slf4j.Slf4j;
import sillybaka.springframework.context.ApplicationListener;
import sillybaka.springframework.context.event.ContextRefreshedEvent;

/**
 * Description：用于测试的监听者
 * Date: 2022/10/24
 * Time: 15:21
 *
 * @Author SillyBaka
 **/
@Slf4j
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("上下文:{}刷新完毕",event);
    }
}
