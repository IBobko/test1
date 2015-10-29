package ru.todo100;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Application {
	public static void main(final String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Do not specified log file.");
			return;
		}
		final String logFile = args[0];
		{
			final File f = new File(logFile);
			if (!f.exists()) {
				System.out.println("Log file is not exists.");
				return;
			}
		}
		
		final long start = System.currentTimeMillis();
		
		final AnalizeLog analize = new AnalizeLogImpl(logFile);
		final List<MethodData> calculatedInfo = analize.process();

		for (final MethodData data: calculatedInfo) {
			System.out.println(data.getClassName() + ":" + data.getMethodName() + " min " + data.getMinTime() + ", max " +data.getMaxTime() +  ", avg " + data.getAvgTime() + ", max id " + data.getMaxId() + ", count " + data.getCountOfCalling());	
		}
		
		final long end = System.currentTimeMillis();
		System.out.println("Total time: " + (end - start));	

	}
}
