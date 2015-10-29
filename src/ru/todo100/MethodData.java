package ru.todo100;

public class MethodData extends MethodNameData {
	/**
	 * Stores minimum time of method calling.
	 */
	private Long minTime = 0l;
	/**
	 * Stores maximum time of method calling.
	 */
	private Long maxTime = 0l;
	/**
	 * Stores count of method calling.
	 */
	private Integer countOfCalling = 0;
	/**
	 * Stores max id of method calling.
	 */
	private Integer maxId = 0;
	/**
	 * Stores average time of method calling.
	 */
	private Long avgTime = 0l;	
	
	public Long getAvgTime() {
		return avgTime;
	}
	public void setAvgTime(Long avgTime) {
		this.avgTime = avgTime;
	}
	public Integer getCountOfCalling() {
		return countOfCalling;
	}
	public Integer getMaxId() {
		return maxId;
	}
	public Long getMaxTime() {
		return maxTime;
	}

	public Long getMinTime() {
		return minTime;
	}
	public void setCountOfCalling(Integer countOfCalling) {
		this.countOfCalling = countOfCalling;
	}
	public void setMaxId(Integer maxId) {
		this.maxId = maxId;
	}
	public void setMaxTime(Long maxTime) {
		this.maxTime = maxTime;
	}

	public void setMinTime(Long minTime) {
		this.minTime = minTime;
	}
}
