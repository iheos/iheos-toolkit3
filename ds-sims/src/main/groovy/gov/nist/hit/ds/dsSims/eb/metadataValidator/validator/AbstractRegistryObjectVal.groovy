package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator

import gov.nist.hit.ds.dsSims.eb.metadataValidator.datatype.AbstractFormatValidator
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.AbstractRegistryObjectModel
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.SlotModel
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase

/**
 * Created by bmajur on 12/23/14.
 */
public abstract class AbstractRegistryObjectVal extends ValComponentBase {

    AbstractRegistryObjectModel model;
    SimHandle simHandle

    public AbstractRegistryObjectVal(SimHandle _simHandle) {
        super(_simHandle)
        simHandle = _simHandle
    }

    public void validateSlot(AbstractRegistryObjectModel model, String slotName, boolean multivalue, AbstractFormatValidator validator) {
        SlotModel slot = model.getSlot(slotName);
        if (slot == null) return;
        new SlotValidator(simHandle, slot, multivalue, validator)
    }

}
