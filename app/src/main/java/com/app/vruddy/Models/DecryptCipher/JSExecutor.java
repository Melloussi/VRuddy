package com.app.vruddy.Models.DecryptCipher;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public class JSExecutor {
    public static String runScript(String jsCode) {
        // Get the JavaScript in previous section
        try {
            String functionName = "mainFunction";

            org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();
            // Turn off optimization to make Rhino Android compatible
            rhino.setOptimizationLevel(-1);
            Scriptable scope = rhino.initStandardObjects();
            rhino.evaluateString(scope, jsCode, "JavaScript", 1, null);
            //define the main function
            Object obj = scope.get(functionName, scope);

            if (obj instanceof Function) {
                Function function = (Function) obj;
                // Call the hello function with params
                Object result = function.call(rhino, scope, scope, new Object[]{});

                // Finally we want to print the result of hello function
                String response = org.mozilla.javascript.Context.toString(result);
                return response;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // We must exit the Rhino VM
            org.mozilla.javascript.Context.exit();
        }

        return null;
    }
}
