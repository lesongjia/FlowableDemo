package com.bettercloud.FlowableDemo.flowable;

import com.bettercloud.FlowableDemo.FlowableDemoApplication;
import com.bettercloud.FlowableDemo.model.Action;
import com.bettercloud.FlowableDemo.model.ActionResult;
import com.bettercloud.FlowableDemo.service.ExecutionContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.delegate.TriggerableActivityBehavior;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ActionDelegate implements JavaDelegate, TriggerableActivityBehavior {

    private final ExecutionContextService contextService;

    @Override
    public void execute(final DelegateExecution execution) {
        FlowableDemoApplication.tempExecutionId = execution.getId();
        log.info(String.format("Execute action: %s", contextService.getAction(execution).getName()));
    }

    @Override
    public void trigger(final DelegateExecution execution, final String signalEvent, final Object signalData) {
        final List<ActionResult> history = contextService.getHistory(execution);

        Action action = contextService.getAction(execution);

        history.add(ActionResult.builder().success(action.isSuccess()).build());

        log.info(String.format("Action: %s finished %s", action.getName(), action.isSuccess() ? "successfully" : "with failure"));

        execution.setVariable("history", contextService.serializeHistory(history));
    }
}
