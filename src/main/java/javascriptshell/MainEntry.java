package javascriptshell;

public class MainEntry {

	public static void main(String[] args) throws Exception {
		String mode = System.getProperty("JSSHELL.mode", "");
		if(mode.equals("ssh")) {
			ScriptRunnerOnSSh.main(args);
		} else {
			ScriptRunnerLocal.main(args);
		}

	}

}
