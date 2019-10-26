package com.ionos.domains.contact.samples;

import com.ionos.domains.contact.model.CreateContactEvent;
import com.ionos.domains.contact.model.CreateContactState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;
import java.util.Optional;

@Configuration
@EnableStateMachine
public class ChoiceStateMachine extends EnumStateMachineConfigurerAdapter<CreateContactState, CreateContactEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<CreateContactState, CreateContactEvent> states)
            throws Exception {
        states
                .withStates()
                .initial(CreateContactState.START) // start
                .state(CreateContactState.CONTACT_REGISTRY_INITIATED) //initiated
                .choice(CreateContactState.CONTACT_REGISTRY_CHOICE) // choice
                .end(CreateContactState.CONTACT_REGISTRY_ERROR)   // end
                .end(CreateContactState.END) //end
                .states(EnumSet.allOf(CreateContactState.class));
    }


    @Override
    public void configure(StateMachineTransitionConfigurer<CreateContactState, CreateContactEvent> transitions)
            throws Exception {
        transitions
                .withExternal()
                    .source(CreateContactState.START).target(CreateContactState.CONTACT_REGISTRY_INITIATED)
                    .event(CreateContactEvent.START)
                    .action(stateContext -> {
                        System.out.println("Done");
                        stateContext
                                .getExtendedState()
                                .getVariables()
                                .putIfAbsent("success",Boolean.TRUE);
                    })
                    .and()
                .withExternal()
                    .source(CreateContactState.CONTACT_REGISTRY_INITIATED).target(CreateContactState.CONTACT_REGISTRY_CHOICE)
                    .event(CreateContactEvent.START)
                    .and()
                .withChoice()
                    .source(CreateContactState.CONTACT_REGISTRY_CHOICE)
                    .first(CreateContactState.CONTACT_REGISTRY_SUCCESS,stateContext -> Optional
                            .of(stateContext
                                    .getExtendedState()
                                    .getVariables()
                                    .get("success"))
                            .filter(Boolean.class::isInstance)
                            .map(Boolean.class::cast)
                            .orElseThrow(IllegalAccessError::new))
                    .last(CreateContactState.CONTACT_REGISTRY_ERROR);

    }

}
