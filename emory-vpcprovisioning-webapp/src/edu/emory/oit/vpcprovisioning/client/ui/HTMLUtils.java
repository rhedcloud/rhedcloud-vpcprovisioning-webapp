package edu.emory.oit.vpcprovisioning.client.ui;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public abstract class HTMLUtils {

	public static SafeHtml getProgressBarSafeHtml(int totalSteps, int completedSteps) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        float percent = new Float(completedSteps)
                / new Float(totalSteps);
        int rounded = Math.round(percent * 100);
        sb.appendHtmlConstant("<div style='width: 250px; height: 20px; position: relative; border: 1px solid; border-radius: 5px;'>");
        sb.appendHtmlConstant("<div style='z-index: 2; display: inline; width: 250px; position: absolute; left: 0px, top: 0px; text-align: center; font-weight: bold; color: black'>"
                + completedSteps
                + "/"
                + totalSteps
                + "</div>");
        sb.appendHtmlConstant("<div style='position: absolute; left: 0; top: 0; width: 250px; z-index: 1'>"
        		+ "<div style='display: inline; float: left; width: "
                + rounded
                + "%; height: 20px; background-color: #38e038;'></div>");
        sb.appendHtmlConstant("<div style='display: inline; float: right; width: "
                + (100 - rounded)
                + "%; height: 20px; background-color: #DC143C;'></div></div>");
        sb.appendHtmlConstant("</div>");
        return sb.toSafeHtml();
	}
}
