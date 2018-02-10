package com.yida.framework.blog.handler;

import com.yida.framework.blog.handler.input.HandlerChainInput;
import com.yida.framework.blog.handler.output.HandlerChainOutput;
import com.yida.framework.blog.utils.common.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Lanxiaowei
 * @Date 2018-01-15 23:32
 * @Description 任务处理器链, 多个任务组成一个任务处理链
 */
public class HandlerChain implements Handler<HandlerChainInput, HandlerChainOutput> {
    private List<Handler> handlers = new ArrayList<Handler>();

    @Override
    public void handle(HandlerChainInput input, HandlerChainOutput output) {
        if (null == handlers || handlers.size() <= 0) {
            return;
        }
        String inputClassName = null;
        String outputClassName = null;
        String className = null;
        String basePackage = null;
        String simpleClassName = null;
        for (Handler handler : handlers) {
            className = handler.getClass().getName();
            simpleClassName = handler.getClass().getSimpleName();
            basePackage = className.substring(0, className.lastIndexOf("."));
            inputClassName = basePackage + ".input." + simpleClassName + "Input";
            outputClassName = basePackage + ".output." + simpleClassName + "Output";
            handler.handle(ReflectionUtil.createInstance(inputClassName), ReflectionUtil.createInstance(outputClassName));
        }
    }

    public HandlerChain addHandler(Handler handler) {
        handlers.add(handler);
        return this;
    }

    public HandlerChain addHandlers(List<Handler> handlerList) {
        if (null != handlerList && handlerList.size() > 0) {
            handlers.addAll(handlerList);
        }
        return this;
    }
}
