package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class CidrPojo extends SharedObject implements IsSerializable, Comparable<CidrPojo> {
	String cidrId;
	String network;
	String bits;
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
}
