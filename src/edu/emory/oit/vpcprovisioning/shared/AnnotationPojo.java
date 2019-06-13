package edu.emory.oit.vpcprovisioning.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("serial")
public class AnnotationPojo extends SharedObject implements IsSerializable {
	/*
		<!ELEMENT Annotation (
			Text, CreateUser, CreateDatetime, LastUpdateUser?, LastUpdateDatetime?)>	 
	*/
	String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
