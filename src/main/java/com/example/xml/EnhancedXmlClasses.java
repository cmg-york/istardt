package com.example.xml;

import jakarta.xml.bind.annotation.*;
import java.util.List;

/**
 * Enhanced XML binding classes for the iStar-T XML schema
 */
public class EnhancedXmlClasses {

    /**
     * Root element for the iStar-T XML schema
     */
    @XmlRootElement(name = "actor", namespace = "https://example.org/istar-t")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ActorXml {

        @XmlAttribute
        private String name;

        @XmlElement(namespace = "https://example.org/istar-t")
        private PredicatesXml predicates;

        @XmlElement(namespace = "https://example.org/istar-t")
        private PreBoxesXml preBoxes;

        @XmlElement(namespace = "https://example.org/istar-t")
        private IndirectEffectsXml indirectEffects;

        @XmlElement(namespace = "https://example.org/istar-t")
        private QualitiesXml qualities;

        @XmlElement(namespace = "https://example.org/istar-t")
        private GoalsXml goals;

        @XmlElement(namespace = "https://example.org/istar-t")
        private TasksXml tasks;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public PredicatesXml getPredicates() { return predicates; }
        public void setPredicates(PredicatesXml predicates) { this.predicates = predicates; }

        public PreBoxesXml getPreBoxes() { return preBoxes; }
        public void setPreBoxes(PreBoxesXml preBoxes) { this.preBoxes = preBoxes; }

        public IndirectEffectsXml getIndirectEffects() { return indirectEffects; }
        public void setIndirectEffects(IndirectEffectsXml indirectEffects) { this.indirectEffects = indirectEffects; }

        public QualitiesXml getQualities() { return qualities; }
        public void setQualities(QualitiesXml qualities) { this.qualities = qualities; }

        public GoalsXml getGoals() { return goals; }
        public void setGoals(GoalsXml goals) { this.goals = goals; }

        public TasksXml getTasks() { return tasks; }
        public void setTasks(TasksXml tasks) { this.tasks = tasks; }
    }

    /**
     * Container for predicates
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PredicatesXml {

        @XmlElement(name = "predicate", namespace = "https://example.org/istar-t")
        private List<PredicateXml> predicate;

        // Getters and setters
        public List<PredicateXml> getPredicate() { return predicate; }
        public void setPredicate(List<PredicateXml> predicate) { this.predicate = predicate; }
    }

    /**
     * Single predicate definition
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PredicateXml {

        @XmlAttribute
        private Boolean primitive;

        @XmlAttribute
        private Boolean init;

        @XmlAttribute
        private Boolean exported;

        @XmlAttribute
        private String description;

        @XmlValue
        private String value;

        // Getters and setters
        public Boolean getPrimitive() { return primitive; }
        public void setPrimitive(Boolean primitive) { this.primitive = primitive; }

        public Boolean getInit() { return init; }
        public void setInit(Boolean init) { this.init = init; }

        public Boolean getExported() { return exported; }
        public void setExported(Boolean exported) { this.exported = exported; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }

    /**
     * Container for preBoxes (Conditions)
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PreBoxesXml {

        @XmlElement(name = "preBox", namespace = "https://example.org/istar-t")
        private List<PreBoxXml> preBox;

        // Getters and setters
        public List<PreBoxXml> getPreBox() { return preBox; }
        public void setPreBox(List<PreBoxXml> preBox) { this.preBox = preBox; }
    }

    /**
     * Single preBox definition (mapped to Condition in the domain model)
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PreBoxXml {

        @XmlAttribute
        private String name;

        @XmlAttribute
        private String description;

        // Boolean expressions
        @XmlElement(name = "gt", namespace = "https://example.org/istar-t")
        private GtXml gt;

        @XmlElement(name = "boolConst", namespace = "https://example.org/istar-t")
        private String boolConst;

        @XmlElement(name = "boolAtom", namespace = "https://example.org/istar-t")
        private String boolAtom;

        @XmlElement(name = "and", namespace = "https://example.org/istar-t")
        private BooleanAndXml and;

        @XmlElement(name = "or", namespace = "https://example.org/istar-t")
        private BooleanOrXml or;

        @XmlElement(name = "not", namespace = "https://example.org/istar-t")
        private BooleanNotXml not;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public GtXml getGt() { return gt; }
        public void setGt(GtXml gt) { this.gt = gt; }

        public String getBoolConst() { return boolConst; }
        public void setBoolConst(String boolConst) { this.boolConst = boolConst; }

        public String getBoolAtom() { return boolAtom; }
        public void setBoolAtom(String boolAtom) { this.boolAtom = boolAtom; }

        public BooleanAndXml getAnd() { return and; }
        public void setAnd(BooleanAndXml and) { this.and = and; }

        public BooleanOrXml getOr() { return or; }
        public void setOr(BooleanOrXml or) { this.or = or; }

        public BooleanNotXml getNot() { return not; }
        public void setNot(BooleanNotXml not) { this.not = not; }
    }

    /**
     * Boolean AND operation
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BooleanAndXml {
        // Elements for AND operands
        // Can be complex with nested boolean expressions
    }

    /**
     * Boolean OR operation
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BooleanOrXml {
        // Elements for OR operands
        // Can be complex with nested boolean expressions
    }

    /**
     * Boolean NOT operation
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BooleanNotXml {
        // Element for NOT operand
        // Can be complex with nested boolean expression
    }

    /**
     * Greater than expression
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GtXml {

        @XmlElement(name = "left", namespace = "https://example.org/istar-t")
        private LeftXml left;

        @XmlElement(name = "right", namespace = "https://example.org/istar-t")
        private RightXml right;

        // Getters and setters
        public LeftXml getLeft() { return left; }
        public void setLeft(LeftXml left) { this.left = left; }

        public RightXml getRight() { return right; }
        public void setRight(RightXml right) { this.right = right; }
    }

    /**
     * Left expression in a comparison
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class LeftXml {

        @XmlElement(name = "const", namespace = "https://example.org/istar-t")
        private String const_;

        @XmlElement(name = "numAtom", namespace = "https://example.org/istar-t")
        private String numAtom;

        // Getters and setters
        public String getConst() { return const_; }
        public void setConst(String const_) { this.const_ = const_; }

        public String getNumAtom() { return numAtom; }
        public void setNumAtom(String numAtom) { this.numAtom = numAtom; }
    }

    /**
     * Right expression in a comparison
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RightXml {

        @XmlElement(name = "const", namespace = "https://example.org/istar-t")
        private String const_;

        @XmlElement(name = "numAtom", namespace = "https://example.org/istar-t")
        private String numAtom;

        // Getters and setters
        public String getConst() { return const_; }
        public void setConst(String const_) { this.const_ = const_; }

        public String getNumAtom() { return numAtom; }
        public void setNumAtom(String numAtom) { this.numAtom = numAtom; }
    }

    /**
     * Container for indirectEffects
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class IndirectEffectsXml {

        @XmlElement(name = "indirectEffect", namespace = "https://example.org/istar-t")
        private List<IndirectEffectXml> indirectEffect;

        // Getters and setters
        public List<IndirectEffectXml> getIndirectEffect() { return indirectEffect; }
        public void setIndirectEffect(List<IndirectEffectXml> indirectEffect) { this.indirectEffect = indirectEffect; }
    }

    /**
     * Single indirectEffect definition
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class IndirectEffectXml {

        @XmlAttribute
        private String name;

        @XmlAttribute
        private Boolean exported;

        @XmlAttribute
        private String description;

        @XmlElement(name = "formula", namespace = "https://example.org/istar-t")
        private FormulaXml formula;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Boolean getExported() { return exported; }
        public void setExported(Boolean exported) { this.exported = exported; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public FormulaXml getFormula() { return formula; }
        public void setFormula(FormulaXml formula) { this.formula = formula; }
    }

    /**
     * Container for qualities
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class QualitiesXml {

        @XmlElement(name = "quality", namespace = "https://example.org/istar-t")
        private List<QualityXml> quality;

        // Getters and setters
        public List<QualityXml> getQuality() { return quality; }
        public void setQuality(List<QualityXml> quality) { this.quality = quality; }
    }

    /**
     * Single quality definition
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class QualityXml {

        @XmlAttribute
        private String name;

        @XmlAttribute
        private String description;

        @XmlAttribute
        private Boolean exported;

        @XmlAttribute
        private Boolean root;

        @XmlElement(name = "formula", namespace = "https://example.org/istar-t")
        private FormulaXml formula;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Boolean getExported() { return exported; }
        public void setExported(Boolean exported) { this.exported = exported; }

        public Boolean getRoot() { return root; }
        public void setRoot(Boolean root) { this.root = root; }

        public FormulaXml getFormula() { return formula; }
        public void setFormula(FormulaXml formula) { this.formula = formula; }
    }

    /**
     * Formula structure - enhanced version
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FormulaXml {

        @XmlElement(name = "add", namespace = "https://example.org/istar-t")
        private AddXml add;

        @XmlElement(name = "subtract", namespace = "https://example.org/istar-t")
        private SubtractXml subtract;

        @XmlElement(name = "multiply", namespace = "https://example.org/istar-t")
        private MultiplyXml multiply;

        @XmlElement(name = "divide", namespace = "https://example.org/istar-t")
        private DivideXml divide;

        @XmlElement(name = "const", namespace = "https://example.org/istar-t")
        private String const_;

        @XmlElement(name = "numAtom", namespace = "https://example.org/istar-t")
        private String numAtom;

        // Getters and setters
        public AddXml getAdd() { return add; }
        public void setAdd(AddXml add) { this.add = add; }

        public SubtractXml getSubtract() { return subtract; }
        public void setSubtract(SubtractXml subtract) { this.subtract = subtract; }

        public MultiplyXml getMultiply() { return multiply; }
        public void setMultiply(MultiplyXml multiply) { this.multiply = multiply; }

        public DivideXml getDivide() { return divide; }
        public void setDivide(DivideXml divide) { this.divide = divide; }

        public String getConst() { return const_; }
        public void setConst(String const_) { this.const_ = const_; }

        public String getNumAtom() { return numAtom; }
        public void setNumAtom(String numAtom) { this.numAtom = numAtom; }
    }

    /**
     * Addition operation in a formula
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AddXml {

        @XmlElement(name = "const", namespace = "https://example.org/istar-t")
        private List<String> const_;

        @XmlElement(name = "numAtom", namespace = "https://example.org/istar-t")
        private List<String> numAtom;

        @XmlElement(name = "subtract", namespace = "https://example.org/istar-t")
        private SubtractXml subtract;

        @XmlElement(name = "multiply", namespace = "https://example.org/istar-t")
        private MultiplyXml multiply;

        // Getters and setters
        public List<String> getConst() { return const_; }
        public void setConst(List<String> const_) { this.const_ = const_; }

        public List<String> getNumAtom() { return numAtom; }
        public void setNumAtom(List<String> numAtom) { this.numAtom = numAtom; }

        public SubtractXml getSubtract() { return subtract; }
        public void setSubtract(SubtractXml subtract) { this.subtract = subtract; }

        public MultiplyXml getMultiply() { return multiply; }
        public void setMultiply(MultiplyXml multiply) { this.multiply = multiply; }
    }

    /**
     * Subtraction operation in a formula
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SubtractXml {

        @XmlElement(name = "left", namespace = "https://example.org/istar-t")
        private LeftXml left;

        @XmlElement(name = "right", namespace = "https://example.org/istar-t")
        private RightXml right;

        // Getters and setters
        public LeftXml getLeft() { return left; }
        public void setLeft(LeftXml left) { this.left = left; }

        public RightXml getRight() { return right; }
        public void setRight(RightXml right) { this.right = right; }
    }

    /**
     * Multiplication operation in a formula
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MultiplyXml {

        @XmlElement(name = "const", namespace = "https://example.org/istar-t")
        private List<String> const_;

        @XmlElement(name = "numAtom", namespace = "https://example.org/istar-t")
        private List<String> numAtom;

        // Getters and setters
        public List<String> getConst() { return const_; }
        public void setConst(List<String> const_) { this.const_ = const_; }

        public List<String> getNumAtom() { return numAtom; }
        public void setNumAtom(List<String> numAtom) { this.numAtom = numAtom; }
    }

    /**
     * Division operation in a formula
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DivideXml {

        @XmlElement(name = "left", namespace = "https://example.org/istar-t")
        private LeftXml left;

        @XmlElement(name = "right", namespace = "https://example.org/istar-t")
        private RightXml right;

        // Getters and setters
        public LeftXml getLeft() { return left; }
        public void setLeft(LeftXml left) { this.left = left; }

        public RightXml getRight() { return right; }
        public void setRight(RightXml right) { this.right = right; }
    }

    /**
     * Container for goals
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GoalsXml {

        @XmlElement(name = "goal", namespace = "https://example.org/istar-t")
        private List<GoalXml> goal;

        // Getters and setters
        public List<GoalXml> getGoal() { return goal; }
        public void setGoal(List<GoalXml> goal) { this.goal = goal; }
    }

    /**
     * Single goal definition
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GoalXml {

        @XmlAttribute
        private String name;

        @XmlAttribute
        private Boolean root;

        @XmlAttribute
        private String description;

        @XmlAttribute
        private String episodeLength;

        @XmlElement(name = "pre", namespace = "https://example.org/istar-t")
        private List<String> pre;

        @XmlElement(name = "npr", namespace = "https://example.org/istar-t")
        private List<String> npr;

        @XmlElement(name = "refinement", namespace = "https://example.org/istar-t")
        private RefinementXml refinement;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Boolean getRoot() { return root; }
        public void setRoot(Boolean root) { this.root = root; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getEpisodeLength() { return episodeLength; }
        public void setEpisodeLength(String episodeLength) { this.episodeLength = episodeLength; }

        public List<String> getPre() { return pre; }
        public void setPre(List<String> pre) { this.pre = pre; }

        public List<String> getNpr() { return npr; }
        public void setNpr(List<String> npr) { this.npr = npr; }

        public RefinementXml getRefinement() { return refinement; }
        public void setRefinement(RefinementXml refinement) { this.refinement = refinement; }
    }

    /**
     * Goal refinement structure
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RefinementXml {

        @XmlAttribute
        private String type;

        @XmlElement(name = "childGoal", namespace = "https://example.org/istar-t")
        private List<ChildRefXml> childGoal;

        @XmlElement(name = "childTask", namespace = "https://example.org/istar-t")
        private List<ChildRefXml> childTask;

        // Getters and setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public List<ChildRefXml> getChildGoal() { return childGoal; }
        public void setChildGoal(List<ChildRefXml> childGoal) { this.childGoal = childGoal; }

        public List<ChildRefXml> getChildTask() { return childTask; }
        public void setChildTask(List<ChildRefXml> childTask) { this.childTask = childTask; }
    }

    /**
     * Reference to a child goal or task
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ChildRefXml {

        @XmlAttribute
        private String ref;

        // Getters and setters
        public String getRef() { return ref; }
        public void setRef(String ref) { this.ref = ref; }
    }

    /**
     * Container for tasks
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TasksXml {

        @XmlElement(name = "task", namespace = "https://example.org/istar-t")
        private List<TaskXml> task;

        // Getters and setters
        public List<TaskXml> getTask() { return task; }
        public void setTask(List<TaskXml> task) { this.task = task; }
    }

    /**
     * Single task definition
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TaskXml {

        @XmlAttribute
        private String name;

        @XmlAttribute
        private String description;

        @XmlElement(name = "pre", namespace = "https://example.org/istar-t")
        private List<String> pre;

        @XmlElement(name = "npr", namespace = "https://example.org/istar-t")
        private List<String> npr;

        @XmlElement(name = "effectGroup", namespace = "https://example.org/istar-t")
        private EffectGroupXml effectGroup;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public List<String> getPre() { return pre; }
        public void setPre(List<String> pre) { this.pre = pre; }

        public List<String> getNpr() { return npr; }
        public void setNpr(List<String> npr) { this.npr = npr; }

        public EffectGroupXml getEffectGroup() { return effectGroup; }
        public void setEffectGroup(EffectGroupXml effectGroup) { this.effectGroup = effectGroup; }
    }

    /**
     * Group of effects associated with a task
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class EffectGroupXml {

        @XmlElement(name = "effect", namespace = "https://example.org/istar-t")
        private List<EffectXml> effect;

        // Getters and setters
        public List<EffectXml> getEffect() { return effect; }
        public void setEffect(List<EffectXml> effect) { this.effect = effect; }
    }

    /**
     * Single effect definition
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class EffectXml {

        @XmlAttribute
        private String name;

        @XmlAttribute
        private Boolean satisfying;

        @XmlAttribute
        private Float probability;

        @XmlAttribute
        private String description;

        @XmlElement(name = "turnsTrue", namespace = "https://example.org/istar-t")
        private List<String> turnsTrue;

        @XmlElement(name = "turnsFalse", namespace = "https://example.org/istar-t")
        private List<String> turnsFalse;

        @XmlElement(name = "pre", namespace = "https://example.org/istar-t")
        private List<String> pre;

        @XmlElement(name = "npr", namespace = "https://example.org/istar-t")
        private List<String> npr;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Boolean getSatisfying() { return satisfying; }
        public void setSatisfying(Boolean satisfying) { this.satisfying = satisfying; }

        public Float getProbability() { return probability; }
        public void setProbability(Float probability) { this.probability = probability; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public List<String> getTurnsTrue() { return turnsTrue; }
        public void setTurnsTrue(List<String> turnsTrue) { this.turnsTrue = turnsTrue; }

        public List<String> getTurnsFalse() { return turnsFalse; }
        public void setTurnsFalse(List<String> turnsFalse) { this.turnsFalse = turnsFalse; }

        public List<String> getPre() { return pre; }
        public void setPre(List<String> pre) { this.pre = pre; }

        public List<String> getNpr() { return npr; }
        public void setNpr(List<String> npr) { this.npr = npr; }
    }
}