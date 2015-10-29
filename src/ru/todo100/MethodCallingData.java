package ru.todo100;

import java.util.Date;

public class MethodCallingData extends MethodNameData {
	private Integer id;
	
	private Date entryTime;
	
	private Date exitTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public Date getExitTime() {
		return exitTime;
	}

	public void setExitTime(Date exitTime) {
		this.exitTime = exitTime;
	}
	
	public boolean isFullFilled() {
		return entryTime != null && exitTime != null;
	}
	
	public Long getFullTime() {
		if (isFullFilled()){
			return exitTime.getTime() - entryTime.getTime();
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode()) + super.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final MethodCallingData other = (MethodCallingData) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true && super.equals(other);
	}
	
	
}
