/* 
** Author(s): Miguel Calejo
** Contact:   interprolog@declarativa.com, http://www.declarativa.com
** Copyright (C) Declarativa, Portugal, 2000-2004
** Use and distribution, without any warranties, under the terms of the 
** GNU Library General Public License, readable in http://www.fsf.org/copyleft/lgpl.html
*/
package com.declarativa.interprolog.gui;
import com.declarativa.interprolog.*;
import java.io.IOException;

public class YAPSubprocessEngineWindow extends SubprocessEngineWindow{
	public YAPSubprocessEngineWindow(YAPSubprocessEngine e) throws IOException{
		super(e);
	}
	public YAPSubprocessEngineWindow(YAPSubprocessEngine e,boolean autoDisplay) throws IOException{
		super(e,autoDisplay);
	}	
	/** Useful for launching the system, by passing the full Prolog executable path and 
	optionally extra arguments, that are passed to the Prolog command */
	public static void main(String[] args) throws IOException{
		commonMain(args);
		new YAPSubprocessEngineWindow(new YAPSubprocessEngine(prologStartCommand,debug,loadFromJar));
	}
}