package edu.emory.oit.vpcprovisioning.presenter.notification;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

import edu.emory.oit.vpcprovisioning.shared.NotificationQueryFilterPojo;

public class ListNotificationPlace extends Place {

	/**
	 * The tokenizer for this place. case recordList doesn't have any state, so we don't
	 * have anything to encode.
	 */
	@Prefix("NotificationList")
	public static class Tokenizer implements PlaceTokenizer<ListNotificationPlace> {

		public ListNotificationPlace getPlace(String token) {
			return new ListNotificationPlace(true);
		}

		public String getToken(ListNotificationPlace place) {
			return "";
		}
	}

	private final boolean listStale;
	NotificationQueryFilterPojo filter;

	/**
	 * Construct a new {@link case recordListPlace}.
	 * 
	 * @param case recordListStale true if the case record list is stale and should be cleared
	 */
	public ListNotificationPlace(boolean listStale) {
		this.listStale = listStale;
	}

	/**
	 * Check if the case record list is stale and should be cleared.
	 * 
	 * @return true if stale, false if not
	 */
	public boolean isListStale() {
		return listStale;
	}

	public NotificationQueryFilterPojo getFilter() {
		return filter;
	}

	public void setFilter(NotificationQueryFilterPojo filter) {
		this.filter = filter;
	}

}
