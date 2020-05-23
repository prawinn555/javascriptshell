package javascriptshell;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TestCallback2 {


    public String hi(String name) {
        return "Hi, " + name + "!";
    }


	public static void main(String[] args) throws Exception {
	    ScriptEngine engine = new ScriptEngineManager().getEngineByName("graal.js");
	    Bindings bindings = engine.createBindings();
	    bindings.put("polyglot.js.allowAllAccess", true);
	    bindings.put("foo", new TestCallback2());
	    engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
	    engine.eval("var myFn = function(name){return foo.hi(name);}");
	  // assertEquals("Say Hi, Fred!", "Say " + engine.eval("myFn('Fred')"));
	}
}
