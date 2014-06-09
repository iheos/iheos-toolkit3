package gov.nist.toolkit.xdstools3.client.customWidgets.testWidget;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class TestWidget extends ListGrid {
	   final DataSource dataSource = ItemSupplyXmlDS.getInstance();
	
	public TestWidget(){
		 this.setWidth(600);  
		 this.setHeight(500);  
		 this.setDrawAheadRatio(4);  
		 this.setCanExpandRecords(true);  
		 this.setAutoFetchData(true);  
		 this.setDataSource(dataSource);  

	      ListGridField itemNameField = new ListGridField("itemName");  
	      ListGridField skuField = new ListGridField("SKU");  

	      this.setFields(itemNameField, skuField);  
	}
	
	//old code for test wodget
//	TestWidget testWidget = new TestWidget();
//	  final SectionStack sectionStack2 = new SectionStack();  
//	  sectionStack2.setVisibilityMode(VisibilityMode.MULTIPLE);  
//	  sectionStack2.setWidth(300);  
//	  sectionStack2.setHeight(350);  
//
//      SectionStackSection section1 = new SectionStackSection("Test Widget 1");  
//      section1.setExpanded(true);  
//
//      SectionStackSection section2 = new SectionStackSection("Test Widget 2");  
//      section2.setExpanded(true); 
//	
//      sectionStack2.addSection(section1);  
//      sectionStack2.addSection(section2);  
//	
//	HLayout testPanel = new HLayout(); 
//	testPanel.addMembers(sectionStack2);
	
	
	 @Override  
     protected Canvas getExpansionComponent(final ListGridRecord record) {  

         final ListGrid grid = this;  
         VLayout layout = new VLayout(5);  
         layout.setPadding(5);  

         final DynamicForm df = new DynamicForm();  
         df.setNumCols(4);  
         df.setDataSource(dataSource);  
         df.addDrawHandler(new DrawHandler() {  
             public void onDraw(DrawEvent event) {  
                 df.editRecord(record);  
             }  
         });  

         IButton saveButton = new IButton("Save");  
         saveButton.addClickHandler(new ClickHandler() {  
             public void onClick(ClickEvent event) {  
                 df.saveData();  
             }  
         });  

         IButton cancelButton = new IButton("Done");  
         cancelButton.addClickHandler(new ClickHandler() {  
             public void onClick(ClickEvent event) {  
                 grid.collapseRecord(record);  
             }  
         });  

         HLayout hLayout = new HLayout(10);  
         hLayout.setAlign(Alignment.CENTER);  
         hLayout.addMember(saveButton);  
         hLayout.addMember(cancelButton);  

         layout.addMember(df);  
         layout.addMember(hLayout);  
         return layout;  
     }  

	  

}
