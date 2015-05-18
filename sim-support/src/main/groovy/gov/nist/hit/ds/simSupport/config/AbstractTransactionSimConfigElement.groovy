package gov.nist.hit.ds.simSupport.config

import gov.nist.hit.ds.actorTransaction.TransactionType

/**
 * Created by bmajur on 1/16/15.
 */
abstract class AbstractTransactionSimConfigElement {
    def name
    List<AbstractConfigElement> elements = new ArrayList<AbstractConfigElement>();

    def clear() { elements.clear() }

    def contains(name) { elements.find { it.name == name }}

    def add(AbstractConfigElement ele) {
        assert !contains(ele.name)
        elements.add(ele)
    }

    def get(name) { elements.find { it.name == name}}

    def addBool(String name, boolean value) { add(new BooleanSimConfigElement(name, value)) }

    BooleanSimConfigElement getBool(String name) {
        def ele = get(name)
        if (ele == null) return null
        assert ele instanceof BooleanSimConfigElement
        return ele
    }

    TextSimConfigElement getText(String name) {
        def ele = get(name)
        if (ele == null) return null
        assert ele instanceof TextSimConfigElement
        return ele
    }

    def setBool(String name, boolean value) {
        BooleanSimConfigElement bool = getBool(name);
        if (bool == null) { addBool(name, value); return; }
        bool.setValue(value);
    }

    def setText(String name, String value) {
        TextSimConfigElement text = getText(name);
        if (text == null) { addText(name, value); return; }
        text.setValue(value);
    }

    TextSimConfigElement addText(String name, String value) {
        TextSimConfigElement text = new TextSimConfigElement(name, value);
        elements.add(text);
        return text;
    }

    boolean getBooleanValue(String name) { get(name)?.value }
    String getTextValue(String name) { get(name)?.value }

    TransactionType getTransactionType() { return endpointType.getTransType(); }

    String getTransactionName() { return endpointType.getTransType().code; }
}
