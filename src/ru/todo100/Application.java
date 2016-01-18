package ru.todo100;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Application {
	public static void main(final String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Do not specified log file.");
			return;
		}
		final String logFile = args[0];

		final Path path = Paths.get(logFile);

		if (Files.notExists(path)) {
			System.out.println("Log file is not exists.");
			return;
		}

		final long start = System.currentTimeMillis();

		final AnalyzeLog analyze = new AnalyzeLogImpl(path);
		final List<MethodData> calculatedInfo = analyze.process();

		for (final MethodData data: calculatedInfo) {
			System.out.println(data.getClassName() + ":" + data.getMethodName() + " min " + data.getMinTime() + ", max " +data.getMaxTime() +  ", avg " + data.getAvgTime() + ", max id " + data.getMaxId() + ", count " + data.getCountOfCalling());	
		}
		
		final long end = System.currentTimeMillis();
		System.out.println("Total time: " + (end - start));
	}
}
