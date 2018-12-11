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
	
	List<AWSServiceStatisticPojo> serviceStatistics = new java.util.ArrayList<AWSServiceStatisticPojo>();
	
	public AWSServiceSummaryPojo() {
	}

	public void initializeStatistics() {
		serviceStatistics = new java.util.ArrayList<AWSServiceStatisticPojo>();
		
//		Object[] keys = serviceMap.keySet().toArray();
		int blockedPendingReviewCount = 0;
		int availableCount = 0;
		int availableWithCounterMeasuresCount = 0;
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
		Date threeHundredSixtyDaysAgo = new Date(today.getTime() - (Constants.MILLIS_PER_DAY * 360));

		for (AWSServicePojo svc : serviceList) {
			/*
				the value 0 if the argument Date is equal to this Date; 
				a value less than 0 if this Date is before the Date argument; 
				and a value greater than 0 if this Date is after the Date argument.			 
			*/
			if (threeHundredSixtyDaysAgo.compareTo(svc.getCreateTime()) >= 0) {
				threeSixtyDayCount++;
			}
			else if (oneHundredEightyDaysAgo.compareTo(svc.getCreateTime()) >= 0) {
				oneEightyDayCount++;
			}
			else {
				ninetyDayCount++;
			}
			
			if (svc.getSiteStatus().equalsIgnoreCase("blocked pending review")) {
				blockedPendingReviewCount++;
			}
			else if (svc.getSiteStatus().equalsIgnoreCase("available")) {
				availableCount++;
			}
			else if (svc.getSiteStatus().equalsIgnoreCase("available with countermeasures")) {
				availableWithCounterMeasuresCount++;
			}
			else if (svc.getSiteStatus().equalsIgnoreCase("blocked")) {
				blockedCount++;
			}
			
			if (svc.isAwsHipaaEligible()) {
				awsHipaaEligibleCount++;
			}
			else {
				awsNotHipaaEligibleCount++;
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
			else if (svc.getAwsStatus().equalsIgnoreCase("deprecated")) {
				awsDeprecatedCount++;
			}
		}

		AWSServiceStatisticPojo s1 = new AWSServiceStatisticPojo("Total Number of Services");
		s1.setCount(serviceList.size());
		serviceStatistics.add(s1);

		AWSServiceStatisticPojo s2 = new AWSServiceStatisticPojo("Services not Assessed yet");
		s2.setCount(blockedPendingReviewCount);
		serviceStatistics.add(s2);
		
		AWSServiceStatisticPojo s3 = new AWSServiceStatisticPojo("Emory Available Services");
		s3.setCount(availableCount);
		serviceStatistics.add(s3);
		
		AWSServiceStatisticPojo s4 = new AWSServiceStatisticPojo("Emory Services Available with Countermeasures");
		s4.setCount(availableWithCounterMeasuresCount);
		serviceStatistics.add(s4);

		AWSServiceStatisticPojo s5 = new AWSServiceStatisticPojo("Emory Services Blocked Pending Review");
		s5.setCount(blockedPendingReviewCount);
		serviceStatistics.add(s5);

		AWSServiceStatisticPojo s6 = new AWSServiceStatisticPojo("Emory Services Blocked");
		s6.setCount(blockedCount);
		serviceStatistics.add(s6);

		AWSServiceStatisticPojo s7 = new AWSServiceStatisticPojo("Emory HIPAA Eligible Services");
		s7.setCount(emoryHipaaEligibleCount);
		serviceStatistics.add(s7);
		
		AWSServiceStatisticPojo s8 = new AWSServiceStatisticPojo("Emory HIPAA NOT Eligible Services");
		s8.setCount(emoryNotHipaaEligibleCount);
		serviceStatistics.add(s8);
		
		AWSServiceStatisticPojo s9 = new AWSServiceStatisticPojo("AWS HIPAA Eligible Services");
		s9.setCount(awsHipaaEligibleCount);
		serviceStatistics.add(s9);
		
		AWSServiceStatisticPojo s10 = new AWSServiceStatisticPojo("AWS HIPAA NOT Eligible Services");
		s10.setCount(awsNotHipaaEligibleCount);
		serviceStatistics.add(s10);

		AWSServiceStatisticPojo s11 = new AWSServiceStatisticPojo("AWS active Services");
		s11.setCount(awsActiveCount);
		serviceStatistics.add(s11);
		
		AWSServiceStatisticPojo s12 = new AWSServiceStatisticPojo("AWS deprecated Services");
		s12.setCount(awsDeprecatedCount);
		serviceStatistics.add(s12);

		AWSServiceStatisticPojo s13 = new AWSServiceStatisticPojo("New Services in the last 90 days");
		s13.setCount(ninetyDayCount);
		serviceStatistics.add(s13);
		
		AWSServiceStatisticPojo s14 = new AWSServiceStatisticPojo("New Services in the last 180 days");
		s14.setCount(oneEightyDayCount);
		serviceStatistics.add(s14);
		
		AWSServiceStatisticPojo s15 = new AWSServiceStatisticPojo("New Services in the last 360 days");
		s15.setCount(threeSixtyDayCount);
		serviceStatistics.add(s15);
		
	}
	
	public HashMap<String, List<AWSServicePojo>> getServiceMap() {
		return serviceMap;
	}

	public void setServiceMap(HashMap<String, List<AWSServicePojo>> serviceMap) {
		this.serviceMap = serviceMap;
	}

	public List<AWSServiceStatisticPojo> getServiceStatistics() {
		return serviceStatistics;
	}

	public void setServiceStatistics(List<AWSServiceStatisticPojo> serviceStatistics) {
		this.serviceStatistics = serviceStatistics;
	}

	public List<AWSServicePojo> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<AWSServicePojo> serviceList) {
		this.serviceList = serviceList;
	}

}
