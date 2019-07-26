package codexe.han.spring.asynchttp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@AllArgsConstructor
/**
 * spring 异步处理http请求的三种方式 callable;  MvcAsyncTask; Derfered
 * Servlet3.0以后tomcat支持异步调用，之前的request都是有container线程完成，3.0以后可以解放container线程
 */
public class AsyncController {
    /**
     * 异步调用restful
     * 当controller返回值是Callable的时候，springmvc就会启动一个线程将Callable交给TaskExecutor去处理
     * 然后DispatcherServlet还有所有的spring拦截器都退出主线程，然后把response保持打开的状态
     * 当Callable执行结束之后，springmvc就会重新启动分配一个request请求，然后DispatcherServlet就重新
     * 调用和处理Callable异步执行的返回结果， 然后返回视图
     *
     * @return
     */
    @GetMapping("/test/callable_async")
    public Callable<ResponseEntity> testAsyncCallable(){
        /**
         * 容器线程a进入controller之后直接返回
         * 然后调用服务线程b执行call（）
         * 执行完毕之后，通过调用容器线程c返回response
         */
        log.info("test callable async");
        log.info("{} 进入testAsyncCallable方法",Thread.currentThread().getName());
        Callable<ResponseEntity> callable = new Callable<ResponseEntity>() {

            @Override
            public ResponseEntity call() throws Exception {
                log.info("{} 进入call方法",Thread.currentThread().getName());

                return CodexeApiResponse.builder().data("test async").build();
            }
        };
        log.info(Thread.currentThread().getName() + " testAsyncCallable 返回");

        return callable;
    }

    @GetMapping("/test/webAsyncTask")
    public WebAsyncTask<ResponseEntity> testWebAsyncTask(){
        /**
         * WebAsyncTask可以定义客户端超时时间
         */

        log.info("{} 进入testWebAsyncTask方法", Thread.currentThread().getName());

        // 3s钟没返回，则认为超时
        WebAsyncTask<ResponseEntity> webAsyncTask = new WebAsyncTask<>(3000, new Callable<ResponseEntity>() {

            @Override
            public ResponseEntity call() throws Exception {
                log.info("{}进入call方法",Thread.currentThread().getName());
                log.info("{}从call返回",Thread.currentThread().getName());
                return CodexeApiResponse.builder().data("test web async task").build();
            }
        });
        log.info("{}从testWebAsyncTask方法返回",Thread.currentThread().getName());

        webAsyncTask.onCompletion(new Runnable() {

            @Override
            public void run() {
                log.info("{}执行完毕",Thread.currentThread().getName());
            }
        });

        webAsyncTask.onTimeout(new Callable<ResponseEntity>() {

            @Override
            public ResponseEntity call() throws Exception {
                log.info("{}onTimeout",Thread.currentThread().getName());
                // 超时的时候，直接抛异常，让外层统一处理超时异常
                throw new TimeoutException("调用超时");
            }
        });
        return webAsyncTask;

    }

    private LongTimeTask longTimeTask;
    @GetMapping("/test/deferred_task")
    public DeferredResult<ResponseEntity> testDeffered(){

        log.info("{}进入testDeffered",Thread.currentThread().getName());
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult<>();//可以传入timeout的时间
        //调用长时间执行的任务
        longTimeTask.execute(deferredResult);
        log.info("{}从长时间执行的任务转给你返回",Thread.currentThread().getName());

        // 超时的回调方法
        deferredResult.onTimeout(new Runnable(){

            @Override
            public void run() {
                log.info("{} onTimeout",Thread.currentThread().getName());
                // 返回超时信息
                deferredResult.setErrorResult("time out!");
            }
        });


        // 处理完成的回调方法，无论是超时还是处理成功，都会进入这个回调方法
        deferredResult.onCompletion(new Runnable(){

            @Override
            public void run() {
                log.info("{} onCompletion",Thread.currentThread().getName());
            }
        });
        log.info("{}退出testDeffered",Thread.currentThread().getName());
        return deferredResult;
    }

    @Component
    @Slf4j
    public class LongTimeTask {
        @Async
        public void execute(DeferredResult<ResponseEntity> deferred) {
            log.info("{} 进入 长时间执行 的 execute方法", Thread.currentThread().getName());
            try {
                // 模拟长时间任务调用，睡眠2s
                TimeUnit.SECONDS.sleep(2);
                // 2s后给Deferred发送成功消息，告诉Deferred，我这边已经处理完了，可以返回给客户端了
                deferred.setResult(CodexeApiResponse.builder().data("deferred test").build());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
