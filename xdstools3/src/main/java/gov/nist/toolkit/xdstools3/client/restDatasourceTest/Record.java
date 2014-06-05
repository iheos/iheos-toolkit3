package gov.nist.toolkit.xdstools3.client.restDatasourceTest;

/**
 * Created by dazais on 5/30/2014.
 */
public class Record {
    private String continent;
    private String countryName;
    private String countryCode;

    public Record(String _countryName, String _countryCode){
        countryName = _countryName;
        countryCode = _countryCode;
    }

    @Override
    public String toString() {
        return new StringBuffer(" country_name : ").append(this.countryName)
                .append(" country_code : ").append(this.countryCode).toString();
    }

}
