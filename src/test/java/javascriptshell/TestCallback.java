package javascriptshell;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

public class TestCallback {

	   public static void main(String[] args) throws Exception {
	        // create a Java object
		    TestCallback es = new TestCallback();

		    ScriptEngine engine = new ScriptEngineManager().getEngineByName("graal.js");
	        engine.put("polyglot.js.allowAllAccess", true);
	        engine.put("es",es);
  
	        
	        // evaluate JavaScript code from String
	        engine.eval("console.log('Hi from JS')");


	        

	        ScriptEngineFactory sef = engine.getFactory();
	        String s = sef.getMethodCallSyntax("es", "sayHi", new String[0]);
	        // show the correct way to call the Java method
	        System.out.println(s);
	        engine.eval(s);
	    }

	    public void sayHi(){
	        System.out.println("hihi");
	    }
}
