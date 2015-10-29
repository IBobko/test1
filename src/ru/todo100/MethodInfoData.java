package ru.todo100;

import java.util.Date;
import java.util.HashMap;

public class MethodInfoData extends MethodNameData {
	public static class CallingData {
		private int id;
		private Date start;
		private Date end;
		public Date getEnd() {
			return end;
		}
		public int getId() {
			return id;
		}
		public Date getStart() {
			return start;
		}
		public void setEnd(Date end) {
			this.end = end;
		}
		public void setId(int id) {
			this.id = id;
		}
		public void setStart(Date start) {
			this.start = start;
		}
		public Long time() {
			if (start != null && end != null) {
				return end.getTime() - start.getTime();
			}
			return null;
		}
	}
	private Integer count = 0;
	private Long minTime = Long.MAX_VALUE;
	
	private Long sumTime = 0l;

	private Long maxTime = Long.MIN_VALUE;

	private Integer maxId = 0;

	private HashMap<Integer,CallingData> callings = new HashMap<>();

	public HashMap<Integer, CallingData> getCallings() {
		return callings;
	}

	public Integer getCount() {
		return count;
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

	public Long getSumTime() {
		return sumTime;
	}

	public void setCount(Integer count) {
		this.count = count;
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
	
	public void setSumTime(Long sumTime) {
		this.sumTime = sumTime;
	}
}