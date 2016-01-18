package ru.todo100;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalyzeLogImpl implements AnalyzeLog {
	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss,SSS";
	private static final String REGEXP_LOG_ENTRY = "([\\dT\\-,:]+)\\sTRACE\\s\\[(\\S+)\\]\\s(exit|entry)\\s(with)\\s\\((\\w+):(\\d+)\\)";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	private final Path file;

	public AnalyzeLogImpl(Path file) {
		this.file = file;
	}
	
	@Override
	public List<MethodData> process() throws IOException {
		final HashMap<String,MethodInfoData> result = new HashMap<>();
		try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
			final Pattern pattern = Pattern.compile(REGEXP_LOG_ENTRY);
			int lineNumber = 0;
			String line;
			while ((line = br.readLine()) != null) {
				lineNumber++;
				final Matcher matcher = pattern.matcher(line);
				final LogEntryData entry = getEntry(matcher);
				if (entry == null) {
					System.out.println("Error occurred in " + lineNumber + " line.");
					continue;
				}

				final String key = entry.getClassName() + ":" + entry.getMethodName();

				if (result.containsKey(key)) {
					MethodInfoData methodInfo = result.get(key);
					HashMap<Integer, MethodInfoData.CallingData> callings = methodInfo.getCallings();
					if (callings.containsKey(entry.getId())) {
						MethodInfoData.CallingData calling = callings.get(entry.getId());
						calling.setEnd(entry.getDate());

						methodInfo.setCount(methodInfo.getCount() + 1);

						if (methodInfo.getMaxId() < entry.getId()) {
							methodInfo.setMaxId(entry.getId());
						}

						if (calling.time() > methodInfo.getMaxTime()) {
							methodInfo.setMaxTime(calling.time());
						}
						if (calling.time() < methodInfo.getMinTime()) {
							methodInfo.setMinTime(calling.time());
						}
						methodInfo.setSumTime(methodInfo.getSumTime() + calling.time());

					} else {
						MethodInfoData.CallingData calling = new MethodInfoData.CallingData();
						calling.setId(entry.getId());
						calling.setStart(entry.getDate());
						callings.put(entry.getId(), calling);
					}
				} else {
					MethodInfoData methodInfoData = new MethodInfoData();
					methodInfoData.setClassName(entry.getClassName());
					methodInfoData.setMethodName(entry.getMethodName());
					MethodInfoData.CallingData calling = new MethodInfoData.CallingData();
					calling.setId(entry.getId());
					calling.setStart(entry.getDate());
					methodInfoData.getCallings().put(entry.getId(), calling);
					result.put(key, methodInfoData);
				}
			}
		}
		
		
		final List<MethodData> methods = new ArrayList<>();
		for (Map.Entry<String, MethodInfoData> e: result.entrySet()) {
			final MethodData data = new MethodData();
			data.setAvgTime(e.getValue().getSumTime() /e.getValue().getCount());
			data.setClassName(e.getValue().getClassName());
			data.setMethodName(e.getValue().getMethodName());
			data.setMaxId(e.getValue().getMaxId());
			data.setMaxTime(e.getValue().getMaxTime());
			data.setMinTime(e.getValue().getMinTime());
			data.setCountOfCalling(e.getValue().getCount());
			methods.add(data);
		}
		return methods;
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
}
