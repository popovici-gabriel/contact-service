package com.ionos.domains.contact.samples;

import com.ionos.domains.contact.create.CreateContactEvent;
import com.ionos.domains.contact.create.CreateContactState;
import java.util.EnumSet;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine
public class ChoiceStateMachine extends EnumStateMachineConfigurerAdapter<CreateContactState, CreateContactEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<CreateContactState, CreateContactEvent> states)
            throws Exception {
        states
                .withStates()
                .initial(CreateContactState.START) // start
                .state(CreateContactState.CREATE_REGISTRY_INITIATED) //initiated
                .choice(CreateContactState.CREATE_REGISTRY_CHOICE) // choice
                .state(CreateContactState.CREATE_REGISTRY_ERROR)   // end
                .end(CreateContactState.END) //end
                .states(EnumSet.allOf(CreateContactState.class));
    }


    @Override
    public void configure(StateMachineTransitionConfigurer<CreateContactState, CreateContactEvent> transitions)
            throws Exception {
        transitions
                .withExternal()
                    .source(CreateContactState.START).target(CreateContactState.CREATE_REGISTRY_INITIATED)
                    .event(CreateContactEvent.START)
                    .and()
                .withExternal()
                    .source(CreateContactState.CREATE_REGISTRY_INITIATED).target(CreateContactState.CREATE_REGISTRY_CHOICE)
                    .event(CreateContactEvent.START)
                    .and()
                .withChoice()
                    .source(CreateContactState.CREATE_REGISTRY_CHOICE)
                    .first(CreateContactState.CREATE_REGISTRY_SUCCESS, context -> false)
                    .last(CreateContactState.CREATE_REGISTRY_ERROR);

    }

}
