package javascriptshell;

public class TestLocalScript {
	
	public static void main(String[] args) throws Exception {

		ScriptRunnerLocal.main(new String[] {
				(ScriptRunnerLocal.isWindows ? "test/testScriptWindows.js" : "test/testScriptBash.js")});

	}
}
