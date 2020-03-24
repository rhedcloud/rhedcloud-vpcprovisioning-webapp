package edu.emory.oit.vpcprovisioning.shared;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AWSServiceSummaryPojo extends SharedObject implements IsSerializable {

	// key is category
	HashMap<String, List<AWSServicePojo>> serviceMap = new HashMap<String, List<AWSServicePojo>>();
	List<AWSServicePojo> serviceList = new java.util.ArrayList<AWSServicePojo>();
	
	// - total # of services
	// - not assessed count (services in a "Blocked Pending Review" status)
	// - Count by status (both emory and aws status)
	// - new services in the last 90, 180, 360 days by create time
	// - emory hipaa eligible count
	// - emory not hipaa eligible count
	// - aws hipaa eligible count
	// - aws not hipaa eligible count
	
	List<AWSServiceStatisticPojo> awsServiceStatistics = new java.util.ArrayList<AWSServiceStatisticPojo>();
	public List<AWSServiceStatisticPojo> getAwsServiceStatistics() {
		return awsServiceStatistics;
	}

	public void setAwsServiceStatistics(List<AWSServiceStatisticPojo> awsServiceStatistics) {
		this.awsServiceStatistics = awsServiceStatistics;
	}

	public List<AWSServiceStatisticPojo> getSiteServiceStatistics() {
		return siteServiceStatistics;
	}

	public void setSiteServiceStatistics(List<AWSServiceStatisticPojo> siteServiceStatistics) {
		this.siteServiceStatistics = siteServiceStatistics;
	}

	List<AWSServiceStatisticPojo> siteServiceStatistics = new java.util.ArrayList<AWSServiceStatisticPojo>();
	
	
	public AWSServiceSummaryPojo() {
	}

	public void initializeStatistics() {
		awsServiceStatistics = new java.util.ArrayList<AWSServiceStatisticPojo>();
		siteServiceStatistics = new java.util.ArrayList<AWSServiceStatisticPojo>();
		
		int blockedPendingReviewCount = 0;
		int availableCount = 0;
		int availableWithCounterMeasuresCount = 0;
		int emoryHipaaAvailableCount=0;
		int emoryHipaaAvailableWithCountermeasuresCount=0;
		int blockedCount = 0;
		int emoryHipaaEligibleCount=0;
		int emoryNotHipaaEligibleCount=0;
		int awsHipaaEligibleCount=0;
		int awsNotHipaaEligibleCount=0;
		int awsActiveCount=0;
		int awsDeprecatedCount=0;
		int ninetyDayCount = 0;
		int oneEightyDayCount = 0;
		int threeSixtyDayCount = 0;
		Date today = new java.util.Date();
		Date ninetyDaysAgo = new Date(today.getTime() - (Constants.MILLIS_PER_DAY * 90));
		Date oneHundredEightyDaysAgo = new Date(today.getTime() - (Constants.MILLIS_PER_DAY * 180));

		for (AWSServicePojo svc : serviceList) {
			long createTime = svc.getCreateTime().getTime();
			if (createTime >= ninetyDaysAgo.getTime()) {
				ninetyDayCount++;
			}
			else if (createTime >= oneHundredEightyDaysAgo.getTime()) {
				oneEightyDayCount++;
			}
			else {
				threeSixtyDayCount++;
			}
			
			if (svc.isBlockedPendingReview()) {
				blockedPendingReviewCount++;
			}
			if (svc.isAvailableStandard()) {
				availableCount++;
			}
			if (svc.isAvailableWithCountermeasuresStandard()) {
				availableWithCounterMeasuresCount++;
			}
			if (svc.isBlocked()) {
				blockedCount++;
			}
			
			if (svc.isAwsHipaaEligible()) {
				awsHipaaEligibleCount++;
			}
			else {
				awsNotHipaaEligibleCount++;
			}

			if (svc.isAvailableHIPAA()) {
				emoryHipaaAvailableCount++;
			}
			if (svc.isAvailableWithCountermeasuresHIPAA()) {
				emoryHipaaAvailableWithCountermeasuresCount++;
			}
			
			if (svc.isSiteHipaaEligible()) {
				emoryHipaaEligibleCount++;
			}
			else {
				emoryNotHipaaEligibleCount++;
			}
			
			if (svc.getAwsStatus().equalsIgnoreCase("active")) {
				awsActiveCount++;
			}
			if (svc.getAwsStatus().equalsIgnoreCase("deprecated")) {
				awsDeprecatedCount++;
			}
		}

		AWSServiceStatisticPojo s1 = new AWSServiceStatisticPojo("Total number of services offered by AWS");
		s1.setCount(serviceList.size());
		awsServiceStatistics.add(s1);

		AWSServiceStatisticPojo s13 = new AWSServiceStatisticPojo("New Services in the last 90 days");
		s13.setCount(ninetyDayCount);
		awsServiceStatistics.add(s13);
		
		AWSServiceStatisticPojo s14 = new AWSServiceStatisticPojo("New Services in the last 180 days");
		s14.setCount(oneEightyDayCount);
		awsServiceStatistics.add(s14);
		
		AWSServiceStatisticPojo s15 = new AWSServiceStatisticPojo("New Services in the last 360 days");
		s15.setCount(threeSixtyDayCount);
		awsServiceStatistics.add(s15);
		
		AWSServiceStatisticPojo s6 = new AWSServiceStatisticPojo("Services blocked by Emory after review");
		s6.setCount(blockedCount);
		siteServiceStatistics.add(s6);

		AWSServiceStatisticPojo s5 = new AWSServiceStatisticPojo("Services blocked pending Emory review");
		s5.setCount(blockedPendingReviewCount);
		siteServiceStatistics.add(s5);

		AWSServiceStatisticPojo s3 = new AWSServiceStatisticPojo("Services available in Emory Standard Accounts without Countermeasures");
		s3.setCount(availableCount);
		siteServiceStatistics.add(s3);
		
		AWSServiceStatisticPojo s4 = new AWSServiceStatisticPojo("Services available in Emory Standard Accounts with Countermeasures");
		s4.setCount(availableWithCounterMeasuresCount);
		siteServiceStatistics.add(s4);

		AWSServiceStatisticPojo s7 = new AWSServiceStatisticPojo("Services available in Emory HIPAA Accounts without Countermeasures");
		s7.setCount(emoryHipaaAvailableCount);
		siteServiceStatistics.add(s7);
		
		AWSServiceStatisticPojo s8 = new AWSServiceStatisticPojo("Services available in Emory HIPAA Accounts with Countermeasures");
		s8.setCount(emoryHipaaAvailableWithCountermeasuresCount);
		siteServiceStatistics.add(s8);
		

//		AWSServiceStatisticPojo s2 = new AWSServiceStatisticPojo("Services not Assessed yet");
//		s2.setCount(blockedPendingReviewCount);
//		serviceStatistics.add(s2);
//		
//		AWSServiceStatisticPojo s8 = new AWSServiceStatisticPojo("Emory HIPAA NOT Eligible Services");
//		s8.setCount(emoryNotHipaaEligibleCount);
//		serviceStatistics.add(s8);
//		
		AWSServiceStatisticPojo s9 = new AWSServiceStatisticPojo("AWS HIPAA Eligible Services");
		s9.setCount(awsHipaaEligibleCount);
		awsServiceStatistics.add(s9);
		
		AWSServiceStatisticPojo s10 = new AWSServiceStatisticPojo("AWS HIPAA NOT Eligible Services");
		s10.setCount(awsNotHipaaEligibleCount);
		awsServiceStatistics.add(s10);

		AWSServiceStatisticPojo s11 = new AWSServiceStatisticPojo("AWS active Services");
		s11.setCount(awsActiveCount);
		awsServiceStatistics.add(s11);
		
		AWSServiceStatisticPojo s12 = new AWSServiceStatisticPojo("AWS deprecated Services");
		s12.setCount(awsDeprecatedCount);
		awsServiceStatistics.add(s12);

	}
	
	public HashMap<String, List<AWSServicePojo>> getServiceMap() {
		return serviceMap;
	}

	public void setServiceMap(HashMap<String, List<AWSServicePojo>> serviceMap) {
		this.serviceMap = serviceMap;
	}

	public List<AWSServicePojo> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<AWSServicePojo> serviceList) {
		this.serviceList = serviceList;
	}

}
