package com.bettercloud.FlowableDemo.service;

import com.bettercloud.FlowableDemo.model.Action;
import com.bettercloud.FlowableDemo.model.ActionResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ExecutionContextService {

    /**
     * Use JSON serialization for store complex object, downside is JsonNode is not play well with expression.
     *
     * e.g. ugly expression
     * <completionCondition>${actions.required(nrOfCompletedInstances - 1).stopOnFailure &amp;&amp; !history.required(nrOfCompletedInstances - 1).success}</completionCondition>
     */

    private static final TypeReference<List<Action>> ACTIONS_TYPE = new TypeReference<List<Action>>() {
    };

    private static final TypeReference<List<ActionResult>> HISTORY_TYPE = new TypeReference<List<ActionResult>>() {
    };

    private final ObjectMapper objectMapper;

    public List<Action> getActions(DelegateExecution execution) {
        return objectMapper.convertValue(execution.getVariable("actions", JsonNode.class), ACTIONS_TYPE);
    }

    public Action getAction(DelegateExecution execution) {
        return objectMapper.convertValue(execution.getVariable("action", JsonNode.class), Action.class);
    }

    public List<ActionResult> getHistory(DelegateExecution execution) {
        return objectMapper.convertValue(execution.getVariable("history", JsonNode.class), HISTORY_TYPE);
    }

    public Map<String, Object> initContext(List<Action> actions) {
        Map<String, Object> context = new HashMap<>();

        context.put("actions", objectMapper.convertValue(actions, JsonNode.class));
        context.put("history", objectMapper.createArrayNode());
        return context;
    }

    public JsonNode serializeHistory(List<ActionResult> actionResults) {
        return objectMapper.convertValue(actionResults, JsonNode.class);
    }
}
