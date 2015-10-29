package ru.todo100;

import java.io.IOException;
import java.util.List;

public interface AnalizeLog {
	public List<MethodData> process() throws IOException;
}
