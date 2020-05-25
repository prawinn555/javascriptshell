package javascriptshell;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ScriptRunnerOnSSh extends AbstractScriptRunner {

	public ScriptRunnerOnSSh() throws Exception {
		super();
	}


	public static void main(String[] args) throws Exception {
		try {
			AbstractScriptRunner r = new ScriptRunnerOnSSh();
			String script = r.loadFileFromArgs(args);
			r.run(script);
		} catch(Exception e) {
			e.printStackTrace();
		}

	}


    private SshClient client;
    
	@Override
	protected void initProcess() throws Exception {
		String shellEncoding = "UTF-8";
        
		client = new SshClient(
				getProp("JSSHELL_USER", "root"), 
				getProp("JSSHELL_PWD", "password"), 
				getProp("JSSHELL_HOST", "localhost"),
				Integer.parseInt(getProp("JSSHELL_PORT", "22")));
		client.connect();
	    commandOutputScanner =  new BufferedReader(new InputStreamReader(client.getIn(), shellEncoding));
	    commandSenderStream = new OutputStreamWriter(client.getOut(), Charset.forName(shellEncoding) );
	}
	
	
	@Override
	protected void close() throws Exception {
		commandSenderStream.write("exit\n");
		commandSenderStream.flush();
		Thread.sleep(1000);
		client.disconnect();
	}
	

}
