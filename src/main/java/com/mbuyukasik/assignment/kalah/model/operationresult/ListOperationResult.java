package com.mbuyukasik.assignment.kalah.model.operationresult;

import java.util.List;

import com.mbuyukasik.assignment.kalah.enumeration.EnmResultCode;

/**
 * Used as a return type of listing operations. 
 * If list operation succeed, list object is filled. 
 * In error case only fields of super class are set to return error
 * 
 * @author TTMBUYUKASIK
 *
 * @param <T>
 */
public class ListOperationResult<T> extends OperationResult {

	private List<T> objectList;
	private Integer totalSize;
	
	public List<T> getObjectList() {
		return objectList;
	}
	public void setObjectList(List<T> objectList) {
		this.objectList = objectList;
	}
	public Integer getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	public ListOperationResult(EnmResultCode resultCode, List<T> objectList) {
		this(resultCode, objectList, null);
	}
	
	public ListOperationResult(EnmResultCode resultCode, List<T> objectList, Integer totalSize) {
		super(resultCode, null);
		this.objectList = objectList;
		this.totalSize = totalSize;
	}
	
}
