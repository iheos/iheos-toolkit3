package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.configure;

import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.*;
import com.smartgwt.client.widgets.form.validator.FloatPrecisionValidator;
import com.smartgwt.client.widgets.form.validator.FloatRangeValidator;

/**
 * SmartGWT datasource for accessing entities over http in a RESTful manner.
 * Defines a RESTDataSource fields, operations and REST service URLs.
 */
public class ItemSupplyDS extends RestDataSource {


    private static ItemSupplyDS instance = null;

    public static ItemSupplyDS getInstance() {
        if (instance == null) {
            instance = new ItemSupplyDS();
        }
        return instance;
    }

	private ItemSupplyDS() {
        setID("itemSupplyDS");
        setDataURL("resources/datasources/endpoint_config.data.xml");
        setRecordXPath("/List/supplyItem"); // this is the path to the record we want to display, inside the XML file holding the data
        DataSourceIntegerField pkField = new DataSourceIntegerField("itemID");
        pkField.setHidden(true);
        pkField.setPrimaryKey(true);

        DataSourceTextField itemNameField = new DataSourceTextField("itemName", "Item", 128, true);
        DataSourceTextField skuField = new DataSourceTextField("SKU", "SKU", 10, true);

        DataSourceTextField descriptionField = new DataSourceTextField("description", "Description", 2000);
        DataSourceTextField categoryField = new DataSourceTextField("categoryName", "Category", 128, true);
        categoryField.setForeignKey("supplyCategoryDS.categoryName");

        DataSourceEnumField unitsField = new DataSourceEnumField("units", "Units", 5);
        unitsField.setValueMap("Roll", "Ea", "Pkt", "Set", "Tube", "Pad", "Ream", "Tin", "Bag", "Ctn", "Box");

        DataSourceFloatField unitCostField = new DataSourceFloatField("unitCost", "Unit Cost", 5);
        FloatRangeValidator rangeValidator = new FloatRangeValidator();
        rangeValidator.setMin(0);
        rangeValidator.setErrorMessage("Please enter a valid (positive) cost");

        FloatPrecisionValidator precisionValidator = new FloatPrecisionValidator();
        precisionValidator.setPrecision(2);
        precisionValidator.setErrorMessage("The maximum allowed precision is 2");

        unitCostField.setValidators(rangeValidator, precisionValidator);

        DataSourceBooleanField inStockField = new DataSourceBooleanField("inStock", "In Stock");

        DataSourceDateField nextShipmentField = new DataSourceDateField("nextShipment", "Next Shipment");

        setFields(pkField, itemNameField, skuField, descriptionField, categoryField, unitsField,
                unitCostField, inStockField, nextShipmentField);

    }
}
