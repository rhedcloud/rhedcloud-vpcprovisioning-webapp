package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AmazonS3AccessWrapperPojo implements IsSerializable {

	String accessId;
	String secretKey;
	String bucketName;
	String keyName;


	public AmazonS3AccessWrapperPojo() {
		
	}


	public String getAccessId() {
		return accessId;
	}


	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}


	public String getSecretKey() {
		return secretKey;
	}


	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}


	public String getBucketName() {
		return bucketName;
	}


	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}


	public String getKeyName() {
		return keyName;
	}


	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

}
