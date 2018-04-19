package edu.emory.oit.vpcprovisioning.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class CidrPojo extends SharedObject implements IsSerializable, Comparable<CidrPojo> {
	String cidrId;
	String network;
	String bits;
	List<AssociatedCidrPojo> associatedCidrs = new java.util.ArrayList<AssociatedCidrPojo>();
	List<PropertyPojo> properties = new java.util.ArrayList<PropertyPojo>();
	CidrPojo baseline;
	
	public static final ProvidesKey<CidrPojo> KEY_PROVIDER = new ProvidesKey<CidrPojo>() {
		@Override
		public Object getKey(CidrPojo item) {
			return item == null ? null : item.getCidrId();
		}
	};
	public CidrPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getCidrId() {
		return cidrId;
	}

	public void setCidrId(String ciderId) {
		this.cidrId = ciderId;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getBits() {
		return bits;
	}

	public void setBits(String bits) {
		this.bits = bits;
	}

	@Override
	public int compareTo(CidrPojo o) {
		return o.getCreateTime().compareTo(this.getCreateTime());
	}

	public CidrPojo getBaseline() {
		return baseline;
	}

	public void setBaseline(CidrPojo baseline) {
		this.baseline = baseline;
	}

	@Override
	public String toString() {
		return network + "/" + bits; 
	}

	public List<AssociatedCidrPojo> getAssociatedCidrs() {
		return associatedCidrs;
	}

	public void setAssociatedCidrs(List<AssociatedCidrPojo> associatedCidrs) {
		this.associatedCidrs = associatedCidrs;
	}

	public List<PropertyPojo> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyPojo> properties) {
		this.properties = properties;
	}

	public boolean containsAssociatedCidr(AssociatedCidrPojo acPojo) {
		for (AssociatedCidrPojo ac : this.associatedCidrs) {
			if (ac.getType().equalsIgnoreCase(acPojo.getType()) && 
					ac.getNetwork().equalsIgnoreCase(acPojo.getNetwork()) && 
					ac.getBits().equalsIgnoreCase(acPojo.getBits())) {
				
				return true;
			}
		}
		return false;
	}

	public void removeAssociatedCidr(AssociatedCidrPojo acPojo) {
		int indexToRemove=0;
		boolean foundAc = false;
		acLoop: for (int i=0; i<associatedCidrs.size(); i++) {
			AssociatedCidrPojo ac = associatedCidrs.get(i);
			if (ac.getType().equalsIgnoreCase(acPojo.getType()) && 
					ac.getNetwork().equalsIgnoreCase(acPojo.getNetwork()) && 
					ac.getBits().equalsIgnoreCase(acPojo.getBits())) {
				
				foundAc = true;
				indexToRemove = i;
				break acLoop;
			}
		}
		if (foundAc) {
			associatedCidrs.remove(indexToRemove);
		}
	}
}
