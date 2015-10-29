package ru.todo100;

public class MethodCallingData {
	/**
	 * Log data about entry to method
	 */
	private LogEntryData entry;
	/**
	 * Log data about exit from method
	 */
	private LogEntryData exit;
	public LogEntryData getEntry() {
		return entry;
	}
	public void setEntry(LogEntryData entry) {
		this.entry = entry;
	}
	public LogEntryData getExit() {
		return exit;
	}
	public void setExit(LogEntryData exit) {
		this.exit = exit;
	}
	
	public boolean isFullFilled() {
		return entry != null && exit != null;
	}
}
