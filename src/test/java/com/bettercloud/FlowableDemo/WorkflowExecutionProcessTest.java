package com.bettercloud.FlowableDemo;

import com.bettercloud.FlowableDemo.model.Action;
import com.bettercloud.FlowableDemo.service.ExecutionContextService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.engine.impl.test.PluggableFlowableTestCase;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.bettercloud.FlowableDemo.FlowableDemoApplication.tempExecutionId;

public class WorkflowExecutionProcessTest extends PluggableFlowableTestCase {

    private ExecutionContextService contextService;

    @BeforeEach
    void setUp() {
        contextService = new ExecutionContextService(new ObjectMapper());
    }

    @Test
    @Deployment(resources = {"processes/WorkflowExecutionProcess.bpmn20.xml"})
    void testProcessExecuteTwoActions() {
        final List<Action> actions = new ArrayList<>();
        actions.add(Action.builder().id("a1").name("add user to group").stopOnFailure(true).isSuccess(true).build());
        actions.add(Action.builder().id("a2").name("remove user from group").stopOnFailure(false).isSuccess(true).build());

        final ProcessInstance instance = runtimeService.startProcessInstanceByKey("workflowExecutionProcess", contextService.initContext(actions));

        assertEquals(1, runtimeService.createProcessInstanceQuery().processInstanceId(instance.getId()).active().count());
        runtimeService.trigger(tempExecutionId);
        assertEquals(1, runtimeService.createProcessInstanceQuery().processInstanceId(instance.getId()).active().count());
        runtimeService.trigger(tempExecutionId);
        assertEquals(0, runtimeService.createProcessInstanceQuery().processInstanceId(instance.getId()).active().count());

    }

    @Test
    @Deployment(resources = {"processes/WorkflowExecutionProcess.bpmn20.xml"})
    void testProcessExecuteTwoActions_withStopOnFailureFalse_actionFailed_and_continue() {
        final List<Action> actions = new ArrayList<>();
        actions.add(Action.builder().id("a1").name("add user to group").stopOnFailure(false).isSuccess(false).build());
        actions.add(Action.builder().id("a2").name("remove user from group").stopOnFailure(false).isSuccess(true).build());

        final ProcessInstance instance = runtimeService.startProcessInstanceByKey("workflowExecutionProcess", contextService.initContext(actions));

        assertEquals(1, runtimeService.createProcessInstanceQuery().processInstanceId(instance.getId()).active().count());
        runtimeService.trigger(tempExecutionId);
        assertEquals(1, runtimeService.createProcessInstanceQuery().processInstanceId(instance.getId()).active().count());
        runtimeService.trigger(tempExecutionId);
        assertEquals(0, runtimeService.createProcessInstanceQuery().processInstanceId(instance.getId()).active().count());

    }

    @Test
    @Deployment(resources = {"processes/WorkflowExecutionProcess.bpmn20.xml"})
    void testProcessExecuteTwoActions_withStopOnFailureTrue_actionFailed_and_stopped() {
        final List<Action> actions = new ArrayList<>();
        actions.add(Action.builder().id("a1").name("add user to group").stopOnFailure(true).isSuccess(false).build());
        actions.add(Action.builder().id("a2").name("remove user from group").stopOnFailure(false).isSuccess(true).build());

        final ProcessInstance instance = runtimeService.startProcessInstanceByKey("workflowExecutionProcess", contextService.initContext(actions));

        assertEquals(1, runtimeService.createProcessInstanceQuery().processInstanceId(instance.getId()).active().count());
        runtimeService.trigger(tempExecutionId);
        assertEquals(0, runtimeService.createProcessInstanceQuery().processInstanceId(instance.getId()).active().count());
    }
}
