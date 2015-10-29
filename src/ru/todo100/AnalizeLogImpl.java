package ru.todo100;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class AnalizeLogImpl implements AnalizeLog {
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss,SSS";
	private static final String REGEXP_LOG_ENTRY = "([\\dT\\-,:]+)\\sTRACE\\s\\[(\\S+)\\]\\s(exit|entry)\\s(with)\\s\\((\\w+):(\\d+)\\)";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	private String file;
	
	public AnalizeLogImpl(String file){
		this.file = file;
	}
	
	@Override
	public List<MethodData> process() throws IOException {
		final List<MethodData> result = new ArrayList<>();
		final Map<String,List<LogEntryData>> methods = collectMethods();
		for (Map.Entry<String,List<LogEntryData>> methodCalling: methods.entrySet()) {
			final MethodData data = getMethodData(methodCalling.getValue());
			if (data != null) {
				result.add(data);
			}
		}
		return result;
	}
	
	public LogEntryData getEntry(Matcher matcher){
		LogEntryData entry = null;
        if (matcher.matches() && matcher.groupCount() == 6) {
			entry = new LogEntryData();
			try {
	        	Date date = dateFormat.parse(matcher.group(1));
	        	entry.setDate(date);
	        } catch(ParseException ee){
	        	System.out.println("Do not parse date.");
	        	return null;
	        }
			entry.setClassName(matcher.group(2));
	        entry.setIo(matcher.group(3));
	        entry.setMethodName(matcher.group(5));
	        try {
	        	entry.setId(Integer.parseInt(matcher.group(6)));
	        }
	        catch(NumberFormatException e)
	        {
	        	System.out.println("Can't determine id of method.");
	        	return null;
	        }
		} else {
			System.out.println("Log line is not matched.");
		}
		return entry;
	}
	
	public Map<String,List<LogEntryData>> collectMethods() throws IOException {
		final Map<String,List<LogEntryData>> result = new HashMap<>();

		final BufferedReader br = new BufferedReader(new FileReader(file));
		try {
		    String line = br.readLine();
		    final Pattern pattern = Pattern.compile(REGEXP_LOG_ENTRY);
		    int lineNumber = 0;
		    while (line != null) {
		    	lineNumber++;
		        final Matcher matcher = pattern.matcher(line);
		        final LogEntryData entry = getEntry(matcher);
		        if (entry == null) {
		        	System.out.println("Error occured in " + lineNumber + " line.");
		        	continue;
		        }
		        List<LogEntryData> list = null;
		        
		        final String key = entry.getClassName() + ":" + entry.getMethodName(); 
		        
		        if (result.containsKey(key)) {
		        	list = result.get(key);
		        } else{
		        	list = new ArrayList<>();
		        	result.put(key, list);	
		        }
		        list.add(entry);
		        line = br.readLine();
		    }
		} finally {
		    br.close();
		}
		return result;
	}
	
	public MethodData getMethodData(List<LogEntryData> entries) {
		if (entries.size() < 1) {
			return null;
		}
		
		final Map<Integer, MethodCallData> linkedHashMap = new LinkedHashMap<Integer, MethodCallData>();
		for (LogEntryData entry: entries) {
			if (!linkedHashMap.containsKey(entry.getId())) {
				if (!entry.getIo().equals("entry")) {
					System.out.println("No entry of method");
					continue;
				}
				MethodCallData methodCalling = new MethodCallData();
				methodCalling.setClassName(entry.getClassName());
				methodCalling.setId(entry.getId());
				methodCalling.setMethodName(entry.getMethodName());
				methodCalling.setEntryTime(entry.getDate());
				linkedHashMap.put(entry.getId(),methodCalling);
			} else {
				if (entry.getIo().equals("entry")) {
					System.out.println("more than one entry of method");
					continue;
				}
				linkedHashMap.get(entry.getId()).setExitTime(entry.getDate());
			}			
		}
		return genarateMethodData(linkedHashMap);
	}
	
	private MethodData genarateMethodData(Map<Integer, MethodCallData> entries) {
		final ArrayList<Long> times = new ArrayList<>();
		int maxId =  entries.entrySet().iterator().next().getKey();
		for (Map.Entry<Integer, MethodCallData> e: entries.entrySet()) {
			final Long time = e.getValue().getFullTime();
			if (time != null) {
				if (maxId < e.getKey()) {
					maxId = e.getKey();
				}
				times.add(time);
			}
		}
		
		MethodCallData entry = entries.entrySet().iterator().next().getValue();
		
		Long min = Collections.min(times);
		Long max = Collections.max(times);
		Long avg = (max - min) /2;
		final MethodData data = new MethodData();
		data.setMethodName(entry.getMethodName());
		data.setClassName(entry.getClassName());
		data.setCountOfCalling(times.size());
		data.setMaxId(maxId);
		data.setMaxTime(max);
		data.setMinTime(min);
		data.setAvgTime(avg);
		return data;
	}

}
