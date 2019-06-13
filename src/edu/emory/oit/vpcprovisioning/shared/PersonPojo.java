package edu.emory.oit.vpcprovisioning.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class PersonPojo extends SharedObject implements IsSerializable {
	//PublicId, PersonalName, LocalizedNameList?, SSN?, BirthDay?, BirthMonth?, BirthYear?, 
	// DriversLicense?, NationalIdCard*, Gender?, Association*, Eligibility*, Tag*, 
	// FallbackContact*,Phone*,Email*, Prsni, Location?, DirectoryTitle?, EmailRestricted?, 
	// PhoneRestricted?, LocationRestricted?)>
	String publicId;
	PersonalNamePojo personalName;
	List<PersonalNamePojo> localizedNameList = new ArrayList<PersonalNamePojo>();
	String ssn;
	String birthDay;
	String birthMonth;
	String birthYear;
	DriversLicensePojo driversLicense;
	List<NationalIdCardPojo> nationalIdCards = new ArrayList<NationalIdCardPojo>();
	String gender;
	List<AssociationPojo> associations = new ArrayList<AssociationPojo>();
	List<EligibilityPojo> eligibilities = new ArrayList<EligibilityPojo>();
	List<String> tags = new ArrayList<String>();
	List<PhonePojo> phones = new ArrayList<PhonePojo>();
	List<EmailPojo> emails = new ArrayList<EmailPojo>();
	String prsni;
	LocationPojo location;
	String directoryTitle;
	boolean emailRestricted;
	boolean phoneRestricted;
	boolean locationRestricted;

	public PersonPojo() {
		
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public PersonalNamePojo getPersonalName() {
		return personalName;
	}

	public void setPersonalName(PersonalNamePojo personalName) {
		this.personalName = personalName;
	}

	public List<PersonalNamePojo> getLocalizedNameList() {
		return localizedNameList;
	}

	public void setLocalizedNameList(List<PersonalNamePojo> localizedNameList) {
		this.localizedNameList = localizedNameList;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public String getBirthMonth() {
		return birthMonth;
	}

	public void setBirthMonth(String birthMonth) {
		this.birthMonth = birthMonth;
	}

	public String getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(String birthYear) {
		this.birthYear = birthYear;
	}

	public DriversLicensePojo getDriversLicense() {
		return driversLicense;
	}

	public void setDriversLicense(DriversLicensePojo driversLicense) {
		this.driversLicense = driversLicense;
	}

	public List<NationalIdCardPojo> getNationalIdCards() {
		return nationalIdCards;
	}

	public void setNationalIdCards(List<NationalIdCardPojo> nationalIdCards) {
		this.nationalIdCards = nationalIdCards;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<AssociationPojo> getAssociations() {
		return associations;
	}

	public void setAssociations(List<AssociationPojo> associations) {
		this.associations = associations;
	}

	public List<EligibilityPojo> getEligibilities() {
		return eligibilities;
	}

	public void setEligibilities(List<EligibilityPojo> eligibilities) {
		this.eligibilities = eligibilities;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<PhonePojo> getPhones() {
		return phones;
	}

	public void setPhones(List<PhonePojo> phones) {
		this.phones = phones;
	}

	public List<EmailPojo> getEmails() {
		return emails;
	}

	public void setEmails(List<EmailPojo> emails) {
		this.emails = emails;
	}

	public String getPrsni() {
		return prsni;
	}

	public void setPrsni(String prsni) {
		this.prsni = prsni;
	}

	public LocationPojo getLocation() {
		return location;
	}

	public void setLocation(LocationPojo location) {
		this.location = location;
	}

	public String getDirectoryTitle() {
		return directoryTitle;
	}

	public void setDirectoryTitle(String directoryTitle) {
		this.directoryTitle = directoryTitle;
	}

	public boolean isEmailRestricted() {
		return emailRestricted;
	}

	public void setEmailRestricted(boolean emailRestricted) {
		this.emailRestricted = emailRestricted;
	}

	public boolean isPhoneRestricted() {
		return phoneRestricted;
	}

	public void setPhoneRestricted(boolean phoneRestricted) {
		this.phoneRestricted = phoneRestricted;
	}

	public boolean isLocationRestricted() {
		return locationRestricted;
	}

	public void setLocationRestricted(boolean locationRestricted) {
		this.locationRestricted = locationRestricted;
	}

}
