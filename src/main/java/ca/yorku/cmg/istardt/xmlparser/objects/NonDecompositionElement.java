package ca.yorku.cmg.istardt.xmlparser.objects;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class NonDecompositionElement extends Element {
    @JacksonXmlProperty(localName = "formula")
    protected Formula valueFormula;

    protected JsonNode rawFormulaNode;
    public JsonNode getRawFormulaNode() {
        return rawFormulaNode;
    }
    public void setRawFormulaNode(JsonNode rawFormulaNode) {
        this.rawFormulaNode = rawFormulaNode;
    }
    public void setFormula(Formula valueFormula) {
        this.valueFormula = valueFormula;
    }

    public Formula getFormula() {
        return valueFormula;
    }


}