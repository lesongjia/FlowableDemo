package com.bettercloud.FlowableDemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Action {

    private String id;
    private String name;
    private boolean stopOnFailure;

    // use for testing purpose to mock the response
    private boolean isSuccess;
}
