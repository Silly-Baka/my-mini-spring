package sillybaka.springframework.context.event;

import sillybaka.springframework.context.ApplicationEvent;
import sillybaka.springframework.context.ApplicationListener;

import java.util.concurrent.Executor;

/**
 * {@link sillybaka.springframework.context.ApplicationEventMulticaster}接口的简单实现
 * 实现了简单的广播逻辑，可以将事件传播给相应的监听者
 *
 * @Author SillyBaka
 **/
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster{

    /**
     * 异步处理任务的自定义线程池
     */
    private Executor taskExecutor;

    @Override
    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener<?> listener : applicationListeners) {
            //todo 这里的判断逻辑可以优化 可以先将event解析成type 再使用type去检查
            if(supportsEvent(listener,event)){
                // 查看是否有自定义的线程池
                Executor executor = getTaskExecutor();

                // 有则异步处理事件
                if(executor != null){
                    executor.execute(()->{
                        invokeListener(listener,event);
                    });

                }else {
                // 无则同步处理事件
                    invokeListener(listener,event);
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void invokeListener(ApplicationListener listener, ApplicationEvent event){
        listener.onApplicationEvent(event);
    }

    protected void setTaskExecutor(Executor executor){
        this.taskExecutor = executor;
    }

    protected Executor getTaskExecutor(){
        return this.taskExecutor;
    }


}
