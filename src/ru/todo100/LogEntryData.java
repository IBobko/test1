package ru.todo100;

import java.util.Date;

public class LogEntryData extends MethodNameData {
	/**
	 * Stores Date name of entry made.
	 */
	private Date date;
	/**
	 * Stores id of method.
	 */
	private Integer id;
	/**
	 * Stores exit from or entrance to method.
	 */
	private String io;
	public Date getDate() {
		return date;
	}
	public Integer getId() {
		return id;
	}
	public String getIo() {
		return io;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setIo(String io) {
		this.io = io;
	}
}
