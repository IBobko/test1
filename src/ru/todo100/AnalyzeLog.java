package ru.todo100;

import java.io.IOException;
import java.util.List;

public interface AnalyzeLog {
	List<MethodData> process() throws IOException;
}
