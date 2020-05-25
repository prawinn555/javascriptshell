package javascriptshell;

public class TestSsh {

	public static void main(String[] args) throws Exception {
		System.setProperty("JSSHELL_USER", "root");
		System.setProperty("JSSHELL_PWD", "root");
		System.setProperty("JSSHELL_HOST", "localhost");
		System.setProperty("JSSHELL_PORT", "2222");

		ScriptRunnerOnSSh.main(args);

	}
}
