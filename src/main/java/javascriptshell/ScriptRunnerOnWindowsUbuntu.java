package javascriptshell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Execute the script on Windows 10 ubuntu (subsystem).
 *
 */
public class ScriptRunnerOnWindowsUbuntu extends ScriptRunnerLocal {

	public ScriptRunnerOnWindowsUbuntu() throws Exception {
		super();
	}

	public static void main(String[] args) throws Exception {
		AbstractScriptRunner r = new ScriptRunnerOnWindowsUbuntu();
		String script = r.loadFileFromArgs(args);
		r.run(script);
	}

	@Override
	protected void initProcess() throws IOException, InterruptedException {
		String shellEncoding = Charset.defaultCharset().toString();
        
	    proc = Runtime.getRuntime().exec("ubuntu");
	    commandOutputScanner =  new BufferedReader(new InputStreamReader(proc.getInputStream(), shellEncoding));
	    commandSenderStream = new OutputStreamWriter
	    		(proc.getOutputStream(), Charset.forName(shellEncoding) );
	}
}
