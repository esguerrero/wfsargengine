/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.uikm.argbuilder.control.entities;

/**
 *
 * @author Esteban
 */
public class AtomEntity {
    
    private int idAtom;
    private String nameAtom;
    private String polarityAtom;
    private int possibilityAtom;
    private boolean inferredAtom;
    private boolean headAtom;
    
    
    public int getIdAtom() {
        return idAtom;
    }

    public void setIdAtom(int idAtom) {
        this.idAtom = idAtom;
    }

    public String getNameAtom() {
        return nameAtom;
    }

    public void setNameAtom(String nameAtom) {
        this.nameAtom = nameAtom;
    }

    public String getPolarityAtom() {
        return polarityAtom;
    }

    public void setPolarityAtom(String polarityAtom) {
        this.polarityAtom = polarityAtom;
    }

    public int getPossibilityAtom() {
        return possibilityAtom;
    }

    public void setPossibilityAtom(int possibilityAtom) {
        this.possibilityAtom = possibilityAtom;
    }

    public boolean isInferredAtom() {
        return inferredAtom;
    }

    public void setInferredAtom(boolean inferredAtom) {
        this.inferredAtom = inferredAtom;
    }

    public boolean isHeadAtom() {
        return headAtom;
    }

    public void setHeadAtom(boolean headAtom) {
        this.headAtom = headAtom;
    }
    
    
    
    
}
