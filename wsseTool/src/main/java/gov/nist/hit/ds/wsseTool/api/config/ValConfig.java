package gov.nist.hit.ds.wsseTool.api.config;

public class ValConfig {
	
	public ValConfig(String version){
		this.version = version;
	}

	public String version;

	public Status status;
	
	public String cotegory;
	
	public class Status {
		
		public static final int implemented = 0;
		public static final int not_implemented = 1;
		public static final int review = 2;
		public static final int not_implementable = 3;
	}
}
