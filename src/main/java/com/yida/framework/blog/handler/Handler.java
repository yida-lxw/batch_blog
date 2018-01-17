package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.HandlerInput;
import com.yida.framework.blog.handler.output.HandlerOutput;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-15 13:12
 * @Description 任务处理接口
 */
public interface Handler<I extends HandlerInput, O extends HandlerOutput> {

    void handle(I input, O output);
}
