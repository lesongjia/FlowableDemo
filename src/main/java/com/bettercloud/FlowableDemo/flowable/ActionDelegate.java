package com.bettercloud.FlowableDemo.flowable;

import com.bettercloud.FlowableDemo.FlowableDemoApplication;
import com.bettercloud.FlowableDemo.model.Action;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.delegate.TriggerableActivityBehavior;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ActionDelegate implements JavaDelegate, TriggerableActivityBehavior {
    @Override
    public void execute(final DelegateExecution execution) {
        FlowableDemoApplication.tempExecutionId = execution.getId();
        log.info(String.format("Execute action: %s", getActionName(execution)));
    }

    @Override
    public void trigger(final DelegateExecution execution, final String signalEvent, final Object signalData) {
        log.info(String.format("Action: %s finished", getActionName(execution)));
    }

    private String getActionName(final DelegateExecution execution) {
        return ((Action) execution.getVariable("action")).getName();
    }
}
