package com.example.xml.deserializers;

import com.example.objects.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Enhanced Jackson module for registering all custom deserializers for the iStar-T model.
 * Organizes deserializers by their functional category.
 */
public class IStarTModule extends SimpleModule {

    /**
     * Constructor that registers all deserializers.
     */
    public IStarTModule() {
        super("IStarTModule");

        addDeserializer(Model.class, new ModelDeserializer());
        addDeserializer(Actor.class, new ActorDeserializer());

        addDeserializer(Goal.class, new GoalDeserializer());
        addDeserializer(Task.class, new TaskDeserializer());
        addDeserializer(Quality.class, new QualityDeserializer());
        addDeserializer(Condition.class, new ConditionDeserializer());
        addDeserializer(IndirectEffect.class, new IndirectEffectDeserializer());
        addDeserializer(Effect.class, new EffectDeserializer());

        addDeserializer(Formula.class, new FormulaDeserializer());
    }

}