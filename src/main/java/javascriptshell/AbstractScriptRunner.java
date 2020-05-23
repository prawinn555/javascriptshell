package javascriptshell;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public abstract class AbstractScriptRunner {


	static final Pattern JSSHELL_END = Pattern.compile("^JSSHELL_END$");
	static final String JSSHELL_EXIT_CODE = "JSSHELL_EXIT_CODE=";
	static final Map<String, String> profileWindows = new HashMap<>();

    static final String DEFAULT_COMMAND_TEMPLATE = "%s\n echo JSSHELL_EXIT_CODE=$?; echo JSSHELL_END";



	protected ScriptEngineManager manager = new ScriptEngineManager();
	protected ScriptEngine engine = manager.getEngineByName("graal.js");
	
	protected Writer commandSenderStream;
	protected BufferedReader commandOutputScanner;

	boolean trace = false;



	protected String loadFile(String[] args) throws IOException {
		String filePath = (args.length > 0) ? args[0]
				: "test/testScriptBash.js";

		return String.join("\n", Files.readAllLines(Paths.get(filePath), Charset.forName("UTF-8")));
	}



	public void run(String script) throws Exception {

		println("default JVM charset is " + Charset.defaultCharset());
		engine.put("polyglot.js.allowAllAccess", true);
		engine.put("shell", this);

		initProcess();

		trace = true;
		// shortcut function
		engine.eval("function exec(command) { return shell.exec(command); }");
		Object v = engine.eval(script);
		println("Script result " + v);
		
		close();
	}

	protected abstract void close() throws Exception ;

	protected abstract void initProcess() throws  Exception ;


//	public Data test() throws InterruptedException {
//	    Thread.currentThread().sleep(2000);
//	    Data d = new Data();
//	    d.a1 = "hello";
//	    d.a2 = "world";
//	    return d;
//	}

	public CommandResult exec(String pCommand) throws Exception {

		println("exec " +pCommand);
		if (pCommand.trim().isEmpty()) {
			return new CommandResult();
		}
		if (trace) {
			println("--- command " + pCommand);
		}
		String c = getCommandTemplate().replace("%s", pCommand);
		commandSenderStream.write(c);
		commandSenderStream.write("\n");
		commandSenderStream.flush();
		List<String> filterPattern = new ArrayList<>();

		for (String s : c.split("\n")) {
			s = s.trim();
			if (s.length() > 0) {
				filterPattern.add(s);
			}
		}
		// filterPattern.remove(0);
		CommandResult r = new CommandResult();

		Thread t = scanUntil(JSSHELL_END, filterPattern, r);
		t.join();

		return r;
	}

	protected String getCommandTemplate() {
		return DEFAULT_COMMAND_TEMPLATE;
	}



	/**
	 * 
	 * @param endPattern
	 * @param filterPattern filter "echo" output of command prompts.
	 * @param r
	 * @return
	 */
	protected Thread scanUntil(Pattern endPattern, List<String> filterPattern, CommandResult r) {
		Thread t;
		t = new Thread(() -> {
			try {
				doScnanJob(endPattern, filterPattern, r);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		t.start();
		return t;
	}

	protected void doScnanJob(Pattern endPattern, List<String> filterPattern, CommandResult r) throws IOException {
		StringBuilder commandOutput = new StringBuilder();
		boolean trouve = false;
		String s;
		while ( (s=commandOutputScanner.readLine())!=null) {
			String trimString = s.replace("\r","").replaceAll("\\p{Cc}", "");
			trimString = trimString.trim();
			if(endPattern.matcher(trimString).find()) {
				//println("found " +trimString + " regex " + endPattern);
				trouve = true;
				break;
			}
			if (!filter(trimString, filterPattern, r)) {
				if (!trimString.isEmpty() && trace) {
					println(4, "- " + trimString);
				}
				commandOutput.append(trimString).append("\n");
			}
		}
		if(!trouve) {
			println("Warn : incomplete stream");
		}
		if (trace) {
			println("-----");
		}
		r.output=commandOutput.toString();
	}

	protected boolean filter(String s, List<String> filterPattern, CommandResult r) {
		boolean isPrompt = filterPattern.stream().anyMatch(el -> s.endsWith(el));
		if(!isPrompt && s.contains("pwd")) {
			println("s='"+s.trim()+"'");
			println("filter=" + filterPattern);
		}
		if (!isPrompt && s.startsWith(JSSHELL_EXIT_CODE)) {
			r.returnCode = s.replace(JSSHELL_EXIT_CODE, "").replace("\n", "").replace("\r", "");
			println(5, "Return code " + r.returnCode);
			return true;
		}
		return isPrompt;
	}

	protected static void println(String s) {
		System.out.println(s);
		//System.out.println("*** "+s);
	}

	protected static void println(int indent, String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			sb.append(" ");
		}
		println(sb.append(s).toString());
	}
	
	protected String getProp(String prop, String defaultVal) {
		String s = System.getProperties().getProperty(prop);
		if (s == null) {
			println(String.format("use -D%s=<value> : using default '%s'", prop, defaultVal));
		}
		return s == null ? defaultVal : s;
	}
}
