package com.example.xml.deserializers;

import com.example.objects.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Jackson module for registering all custom deserializers for the iStar-T model.
 * This centralizes the registration of deserializers for complex types.
 */
public class IStarTModule extends SimpleModule {

    /**
     * Constructor that registers all deserializers.
     */
    public IStarTModule() {
        super("IStarTModule");

        // Register custom deserializers for complex types
        addDeserializer(Actor.class, new ActorDeserializer());
        addDeserializer(Model.class, new ModelDeserializer());
        addDeserializer(Formula.class, new FormulaDeserializer());
        addDeserializer(Goal.class, new GoalDeserializer());
        addDeserializer(Task.class, new TaskDeserializer());
        addDeserializer(Quality.class, new QualityDeserializer());
        addDeserializer(Condition.class, new ConditionDeserializer());
        addDeserializer(IndirectEffect.class, new IndirectEffectDeserializer());
        addDeserializer(Effect.class, new EffectDeserializer());
    }
}