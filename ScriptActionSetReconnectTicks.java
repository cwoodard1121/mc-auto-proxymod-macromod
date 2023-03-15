package com.itzblaze;

import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
@APIVersion(26)
public class ScriptActionSetReconnectTicks extends ScriptAction {
    public ScriptActionSetReconnectTicks() {
        super(ScriptContext.MAIN,"setreconnectticks");
    }


    @Override
    public boolean isThreadSafe() {
        return false;
    }


    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        DisconnectCheck.INSTANCE.ticks = 0;
        Cache.ticks = Integer.parseInt(provider.expand(macro,params[0],false));
        return null;
    }


    @Override
    public void onInit() {
        context.getCore().registerScriptAction(this);
    }
}
