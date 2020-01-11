package com.bettercloud.FlowableDemo;

import com.bettercloud.FlowableDemo.model.Action;
import com.bettercloud.FlowableDemo.service.ExecutionContextService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class FlowableDemoApplication {

    public static String tempExecutionId;

    public static void main(String[] args) {
        SpringApplication.run(FlowableDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(final RepositoryService repositoryService,
                                  final RuntimeService runtimeService,
                                  final ExecutionContextService contextService) {

        return args -> {
            log.info("Number of process definitions : " + repositoryService.createProcessDefinitionQuery().count());
            log.info("Running instance# : " + runtimeService.createProcessInstanceQuery().active().count());

            final List<Action> actions = new ArrayList<>();
            actions.add(Action.builder().id("a1").name("add user to group").stopOnFailure(true).isSuccess(false).build());
            actions.add(Action.builder().id("a2").name("remove user from group").build());

            runtimeService.startProcessInstanceByKey("workflowExecutionProcess", contextService.initContext(actions));
            log.info("Running instance# : " + runtimeService.createProcessInstanceQuery().active().count());
            runtimeService.trigger(tempExecutionId);
            log.info("Running instance# : " + runtimeService.createProcessInstanceQuery().active().count());

        };
    }

}
