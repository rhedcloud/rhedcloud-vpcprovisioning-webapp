package edu.emory.oit.vpcprovisioning.shared;

import java.io.Serializable;
import java.util.Date;

public class SharedObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    protected String createUser=null;
    protected Date createTime=null;
    protected String updateUser=null;
    protected Date updateTime=null;
	
	public String getUniqueId() {
		return null;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setCreateInfo(String userId) {
		Date d = new Date();
		setCreateInfo(userId, d);
		setUpdateInfo(userId, d);
	}
	
	public void setCreateInfo(String userId, Date time) {
		createUser = userId;
		createTime = time;
		setUpdateInfo(userId, time);
	}
	
	public void setUpdateInfo(String userId) {
		setUpdateInfo(userId, new Date());
	}
	
	public void setUpdateInfo(String userId, Date time) {
		updateUser = userId;
		updateTime = time;
	}

	protected boolean stringEquals(String s1, String s2) {
		if (isEmpty(s1) && isEmpty(s2)) {
			return true;
		}
		else if (isEmpty(s1) && !isEmpty(s2)) {
			return false;
		}
		else if (!isEmpty(s1) && isEmpty(s2)) {
			return false;
		}
		else {
			return s1.equals(s2);
		}
	}
	
	protected boolean objectEquals(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return true;
		}
		else if (o1 == null && o2 != null) {
			return false;
		}
		else if (o1 != null && o2 == null) {
			return false;
		}
		else {
			return o1.equals(o2);
		}
	}
	
	protected boolean isEmpty(String s) {
		return s == null || s.equals("");
	}
	
	public String extractNumberFromString(final String str) {                

	    if(str == null || str.isEmpty()) return "";

	    StringBuilder sb = new StringBuilder();
	    boolean found = false;
	    for(char c : str.toCharArray()) {
	        if(Character.isDigit(c)){
	            sb.append(c);
	            found = true;
	        } 
	        else if (found) {
	            // If we already found a digit before and this 
	        	// char is not a digit, stop looping
	            break;                
	        }
	    }

	    return sb.toString();
	}
}
