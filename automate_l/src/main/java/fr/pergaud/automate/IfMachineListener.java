/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pergaud.automate;

import fr.pergaud.automate.Machine.EtatMachine;
import fr.pergaud.automate.Machine.AlimentationMachine;

/**
 *
 * @author Pierre
 */
public interface IfMachineListener extends java.util.EventListener {
    
    void etatMachine(EtatMachine pEtatMachine);
    void alimentationMachine(AlimentationMachine pAlimentationMachine);
    void info(String pMessage);
    void isRunning(Boolean pIsRunning);
    void indiquerPourcentage(int pProgression);
    void indiquerTempsRestant(String pTempsRestant);
    
}
