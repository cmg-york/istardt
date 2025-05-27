package ca.yorku.cmg.istardt.xmlparser.xml.formula;

import com.fasterxml.jackson.databind.JsonNode;
import ca.yorku.cmg.istardt.xmlparser.objects.Formula;

import java.io.IOException;

/**
 * Visitor interface for processing formula JSON nodes.
 */
public interface FormulaNodeVisitor {
    Formula visitNumConst(JsonNode node);
    Formula visitBoolConst(JsonNode node);
    Formula visitPredicateID(JsonNode node);
    Formula visitGoalID(JsonNode node);
    Formula visitTaskID(JsonNode node);
    Formula visitVariableID(JsonNode node);
    Formula visitQualID(JsonNode node);

    Formula visitAdd(JsonNode node) throws IOException;
    Formula visitSubtract(JsonNode node) throws IOException;
    Formula visitMultiply(JsonNode node) throws IOException;
    Formula visitDivide(JsonNode node) throws IOException;
    Formula visitPrevious(JsonNode node) throws IOException;
    Formula visitNegate(JsonNode node) throws IOException;
    Formula visitGreaterThan(JsonNode node) throws IOException;
    Formula visitGreaterThanEquals(JsonNode node) throws IOException;
    Formula visitLessThan(JsonNode node) throws IOException;
    Formula visitLessThanEquals(JsonNode node) throws IOException;
    Formula visitEquals(JsonNode node) throws IOException;
    Formula visitNotEquals(JsonNode node) throws IOException;
    Formula visitAnd(JsonNode node) throws IOException;
    Formula visitOr(JsonNode node) throws IOException;
    Formula visitNot(JsonNode node) throws IOException;
}