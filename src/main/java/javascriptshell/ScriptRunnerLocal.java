package javascriptshell;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ScriptRunnerLocal extends AbstractScriptRunner {

	public ScriptRunnerLocal() throws Exception {
		super();
	}

	protected static boolean isWindows;

	static {
		isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
	}
	
	
	protected Process proc;
	
	@Override
	protected String getCommandTemplate() {
		return (isWindows)? "%s \n echo JSSHELL_EXIT_CODE=%errorlevel%\n  echo JSSHELL_END" : 
				DEFAULT_COMMAND_TEMPLATE;
	}
	

	
	public static void main(String[] args) throws Exception {
		AbstractScriptRunner r = new ScriptRunnerLocal();
		String script = r.loadFileFromArgs(args);
		r.run(script);
	}
	
	@Override
	protected void close() throws  Exception {
		commandSenderStream.write("exit\n");
		commandSenderStream.flush();
		Thread.sleep(1000);
		if(proc!=null && proc.isAlive()) {
			println("process forced stop");
			proc.destroy();
		} else {
			println("process stopped normally");
		}
	}

	@Override
	protected void initProcess() throws  Exception {
		String shellEncoding = isWindows ? "Cp850" : Charset.defaultCharset().toString();
		try {
			proc = Runtime.getRuntime().exec(isWindows ? "cmd" : 
				"bash");
		} catch(Exception e) {
			println(e.toString() +" => try sh");
			proc = Runtime.getRuntime().exec("sh");
		}
		commandOutputScanner = new BufferedReader(new InputStreamReader(proc.getInputStream(), shellEncoding));
		commandSenderStream = new OutputStreamWriter(proc.getOutputStream(),  Charset.forName(shellEncoding));
	}
}
