/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package se.umu.uikm.argbuilder.engine;

/**
 *
 * @author Esteban
 */
public class ArgEngineCall {
    
    private String URLPROGRAM ="";
    private String QUERY = "";
    private String SEMANTIC = "";
    private boolean oneResult = true;

    public ArgEngineCall() {
    }

    public String getURLPROGRAM() {
        return URLPROGRAM;
    }

    public void setURLPROGRAM(String URLPROGRAM) {
        this.URLPROGRAM = URLPROGRAM;
    }

    public String getQUERY() {
        return QUERY;
    }

    public void setQUERY(String QUERY) {
        this.QUERY = QUERY;
    }

    public String getSEMANTIC() {
        return SEMANTIC;
    }

    public void setSEMANTIC(String SEMANTIC) {
        this.SEMANTIC = SEMANTIC;
    }

    public boolean isOneResult() {
        return oneResult;
    }

    public void setOneResult(boolean oneResult) {
        this.oneResult = oneResult;
    }
    
    
    
}
