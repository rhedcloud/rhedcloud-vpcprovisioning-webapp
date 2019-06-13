package edu.emory.oit.vpcprovisioning.presenter.home;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.UserAccountPojo;

public class HomePlace extends Place {
	UserAccountPojo userLoggedIn;
	/**
	 * The tokenizer for this place.
	 */
	@Prefix("home")
	public static class Tokenizer implements PlaceTokenizer<HomePlace> {

		private static final String NO_ID = "home";

		public HomePlace getPlace(String token) {
			if (token != null) {
				return new HomePlace(null);
			}
			else {
				// If the ID cannot be parsed, assume we are creating a caseRecord.
				return HomePlace.getHomePlace();
			}
		}

		public String getToken(HomePlace place) {
			return NO_ID;
		}
	}

	public HomePlace(UserAccountPojo user) {
		this.userLoggedIn = user;
	}
	
	/**
	 * The singleton instance of this place used for creation.
	 */
	private static HomePlace singleton;

	/**
	 * Create an instance of {@link HomePlace} associated with the specified caseRecord
	 * ID.
	 * 
	 * @param mrn the ID of the caseRecord to edit
	 * @param caseRecord the caseRecord to edit, or null if not available
	 * @return the place
	 */
	public static HomePlace createHomePlace() {
		return new HomePlace(null);
	}

	/**
	 * Get the singleton instance of the {@link AddCaseRecordPlace} used to create a new
	 * caseRecord.
	 * 
	 * @return the place
	 */
	public static HomePlace getHomePlace() {
		if (singleton == null) {
			singleton = new HomePlace(null);
		}
		return singleton;
	}

	public UserAccountPojo getUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(UserAccountPojo userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}
}
