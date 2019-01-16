package edu.emory.oit.vpcprovisioning.client.common;

import com.amazon.aws.moa.jmsobjects.user.v1_0.UserNotification;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class UserNotificationOverviewCell extends AbstractCell<UserNotification> {

	public UserNotificationOverviewCell() {
		
	}

	/**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {
      /**
       * The template for this Cell, which includes styles and a value.
       * 
       * @param styles the styles to include in the style attribute of the div
       * @param value the safe value. Since the value type is {@link SafeHtml},
       *          it will not be escaped before including it in the template.
       *          Alternatively, you could make the value type String, in which
       *          case the value would be escaped.
       * @return a {@link SafeHtml} instance
       */
      @SafeHtmlTemplates.Template("<div style=\"{0}\">{1}</div>")
      SafeHtml cell(SafeStyles styles, SafeHtml value);
    }

    /**
     * Create a singleton instance of the templates used to render the cell.
     */
    private static Templates templates = GWT.create(Templates.class);
	
	@Override
	public void render(Context context, UserNotification value, SafeHtmlBuilder sb) {
		
		
	}

}
