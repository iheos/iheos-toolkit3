package gov.nist.hit.ds.dsSims.eb.metadataValidator.object

import gov.nist.hit.ds.dsSims.eb.metadata.MetadataSupport
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 8/19/14.
 */

@groovy.transform.TypeChecked
class SlotDAO {
    SlotModel model
    RegistryObjectDAO roDAO

    public OMElement toXML() {
        OMElement sl = MetadataSupport.om_factory.createOMElement(MetadataSupport.slot_qnamens);
        sl.addAttribute("name", model.name, null);
        OMElement valuelist = MetadataSupport.om_factory.createOMElement(MetadataSupport.valuelist_qnamens);
        sl.addChild(valuelist);

        for (String value : model.values) {
            OMElement valueEle = MetadataSupport.om_factory.createOMElement(MetadataSupport.value_qnamens);
            valueEle.setText(value);
            valuelist.addChild(valueEle);
        }
        return sl;
    }

    SlotDAO(SlotModel model) {
        this.model = model
        roDAO = new RegistryObjectDAO(model)
    }
}
