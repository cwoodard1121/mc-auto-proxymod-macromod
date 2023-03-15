package com.itzblaze.binds;
import net.eq2online.macros.gui.screens.GuiMacroBind;
import net.eq2online.macros.scripting.api.*;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;

@APIVersion(26)
public class ScriptActionSetEvent extends ScriptAction {


    public ScriptActionSetEvent() {
        super(ScriptContext.MAIN,"setevent");
    }


    @Override
    public boolean isThreadSafe() {
        return false;
    }


    @Override
    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        return null;
    }


    @Override
    public void onInit() {
        context.getCore().registerScriptAction(this);
    }

}
