package gov.nist.hit.ds.dsSims.eb.metadataValidator.model;

public class UuidModel {
	private String uuid;
	
	public UuidModel(String uuid) { this.uuid = uuid; }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UuidModel other = (UuidModel) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	public String id() { return uuid; }
	
	public String toString() { return uuid; }
}
