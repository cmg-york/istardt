package ca.yorku.cmg.istardt.xmlparser.xml.deserializers;

import ca.yorku.cmg.istardt.xmlparser.objects.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Jackson module for registering all custom deserializers for the iStar-DT-X model.
 */
public class IStarDTXModule extends SimpleModule {

    public IStarDTXModule() {
        super("IStarDTXModule");

        addDeserializer(Model.class, new ModelDeserializer());
        addDeserializer(Actor.class, new ActorDeserializer());

        addDeserializer(Predicate.class, new PredicateDeserializer());
        addDeserializer(Variable.class, new VariableDeserializer());

        addDeserializer(Goal.class, new GoalDeserializer());
        addDeserializer(Task.class, new TaskDeserializer());
        addDeserializer(Quality.class, new QualityDeserializer());
        addDeserializer(Condition.class, new ConditionDeserializer());
        addDeserializer(Effect.class, new EffectDeserializer());

        addDeserializer(Formula.class, new FormulaDeserializer());
    }
}