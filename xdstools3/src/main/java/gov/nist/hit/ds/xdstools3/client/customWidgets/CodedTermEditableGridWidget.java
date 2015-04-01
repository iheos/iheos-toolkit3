package gov.nist.hit.ds.xdstools3.client.customWidgets;

import com.smartgwt.client.types.ListGridComponent;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Created by onh2 on 1/8/2015.
 */
public class CodedTermEditableGridWidget extends VStack{
    ListGrid codedTermGrid = new ListGrid();
    ToolStripButton addButton;

    public CodedTermEditableGridWidget(){
        buildUI();
    }

    private void buildUI() {
        ToolStrip gridEditControls = new ToolStrip();
        gridEditControls.setWidth100();
        gridEditControls.setHeight(24);

        addButton = new ToolStripButton();
        addButton.setIcon("icons/glyphicons/add.gif");
        addButton.setPrompt("Add a custom value");

        gridEditControls.setMembers(addButton);

        codedTermGrid.setShowAllRecords(true);
        codedTermGrid.setShowAllColumns(true);
        codedTermGrid.setHeight(200);
        codedTermGrid.setWidth100();
        codedTermGrid.setCellHeight(25);
        codedTermGrid.setTitle("Coded term grid");

        ListGridField displayNameField = new ListGridField("displayName","Display name");
        ListGridField codeField = new ListGridField("code","Code");
        ListGridField codingScheme = new ListGridField("codingScheme","Coding scheme");

        codedTermGrid.setFields(displayNameField,codeField,codingScheme);
        codedTermGrid.setCanEdit(true);
        codedTermGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);

        SelectItem existingCodedTerms = new SelectItem();
        existingCodedTerms.setEmptyDisplayValue("Select a coded term to add...");
        existingCodedTerms.setType("comboBox");
        existingCodedTerms.setWidth(400);
        existingCodedTerms.setTitle("Existing codes");
        DynamicForm form=new DynamicForm();
        form.setWidth100();
        form.setFields(existingCodedTerms);

        codedTermGrid.setGridComponents(new Object[] {
                gridEditControls,
                ListGridComponent.HEADER,
                ListGridComponent.FILTER_EDITOR,
                ListGridComponent.BODY,
                form
        });

        this.addMembers(codedTermGrid);
    }

}
