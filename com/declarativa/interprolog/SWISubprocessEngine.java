/* 
** Author(s): Miguel Calejo
** Contact:   interprolog@declarativa.com, http://www.declarativa.com
** Copyright (C) Declarativa, Portugal, 2000-2005
** Use and distribution, without any warranties, under the terms of the 
** GNU Library General Public License, readable in http://www.fsf.org/copyleft/lgpl.html
*/
package com.declarativa.interprolog;
import com.declarativa.interprolog.util.*;
import java.io.*;
import java.net.*;

/** A PrologEngine encapsulating a <a href='http://www.swi-prolog.org/'>SWI Prolog</a> engine, accessed over TCP/IP sockets. 
*/
public class SWISubprocessEngine extends SubprocessEngine{
    protected PrologImplementationPeer makeImplementationPeer(){
    	return new SWIPeer(this);
    }
    public SWISubprocessEngine(String prologCommand, boolean debug, boolean loadFromJar){
    	super(prologCommand, debug, loadFromJar);
    }
    public SWISubprocessEngine(String prologCommand, boolean debug){
    	super(prologCommand, debug);
    }
    public SWISubprocessEngine(String prologCommand){
    	super(prologCommand);
    }
    public SWISubprocessEngine(){
    	super();
    }
	public boolean realCommand(String s){
		progressMessage("COMMAND:"+s+".");
		// fails to make sure SWI doesn't hang showing variables; 
		// prints prompt because SWI (apparently from version 5.4.x onwards) will print it only when its input stream is 'user'
		sendAndFlushLn("("+s+"), write('"+SWIPeer.REGULAR_PROMPT+"'), ttyflush, !, fail."); 
		//sendAndFlushLn("ttyflush."); 
		return true; // we do not really know
	}
}


