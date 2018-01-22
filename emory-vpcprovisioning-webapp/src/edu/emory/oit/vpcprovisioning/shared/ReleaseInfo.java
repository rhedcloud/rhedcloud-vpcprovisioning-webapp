package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ReleaseInfo implements IsSerializable {
	/**
	 * test
	 */
	private static final long serialVersionUID = 1L;
	String version = "1.0";
	String build = "20";
	String productName = "VPC Provisioning App";
	String applicationEnvironment = "APPLICATION_ENVIRONMENT";
	String applicationName = "APPLICATION_NAME";
	String awsDefaultRegion = "AWS_DEFAULT_REGION";
	String gwtProjectName = "GWT_PROJECT_NAME";
	String mvnArtifactId = "MVN_ARTIFACT_ID";
	String mvnPackaging = "MVN_PACKAGING";
	String mvnVersion = "MVN_VERSION";
	String s3Bucket = "S3_BUCKET";
	String branchName = "BRANCH_NAME";
	String buildNumber = "BUILD_NUMBER";
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public ReleaseInfo() {
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String toString() {
		return 
				"Version: " + version + "  Build: " + build;
	}

	public String getApplicationEnvironment() {
		return applicationEnvironment;
	}

	public void setApplicationEnvironment(String applicationEnvironment) {
		this.applicationEnvironment = applicationEnvironment;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getAwsDefaultRegion() {
		return awsDefaultRegion;
	}

	public void setAwsDefaultRegion(String awsDefaultRegion) {
		this.awsDefaultRegion = awsDefaultRegion;
	}

	public String getGwtProjectName() {
		return gwtProjectName;
	}

	public void setGwtProjectName(String gwtProjectName) {
		this.gwtProjectName = gwtProjectName;
	}

	public String getMvnArtifactId() {
		return mvnArtifactId;
	}

	public void setMvnArtifactId(String mvnArtifactId) {
		this.mvnArtifactId = mvnArtifactId;
	}

	public String getMvnPackaging() {
		return mvnPackaging;
	}

	public void setMvnPackaging(String mvnPackaging) {
		this.mvnPackaging = mvnPackaging;
	}

	public String getMvnVersion() {
		return mvnVersion;
	}

	public void setMvnVersion(String mvnVersion) {
		this.mvnVersion = mvnVersion;
	}

	public String getS3Bucket() {
		return s3Bucket;
	}

	public void setS3Bucket(String s3Bucket) {
		this.s3Bucket = s3Bucket;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}
}
