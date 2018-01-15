package com.yida.framework.blog.handler;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-15 13:12
 * @Description 任务处理接口
 */
public interface Handler<I extends HandlerInput, O extends HandlerOutput> {

    boolean handle(I input, O output);
}
