/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pergaud.automate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.event.EventListenerList;

import fr.pergaud.automate.IfMachineListener;
/**
 *
 * @author Pierre
 */
public class Machine {
    
    private AlimentationMachine Alimentation;
    private EtatMachine EtatMachine;
    private String infoMessage;
    private boolean isRunning;
   
    // cycle
    private int delay = 50;
    private Timer timerCycle;
    private int pourcentageCycle = 0;
    
    // urgence
    private Timer timerUrgence;
    private int tempsRestant = 6;
    private int delayUrgence = 1000;
    
    /**
     * 
     * Etats de la machine
     *
     */
    enum AlimentationMachine {
    	Allumee("Allumée"),
    	Eteinte("Eteinte");
    	
    	private String etat;
        AlimentationMachine(String e) {
            this.etat = e;
        }
        public String toString() {
            
            return String.valueOf(etat);
        }
    }
    enum EtatMachine {
        AttenteJeton("En attente de jetons..."),
        Lavage("Lavage en cours..."),
        Rinçage("Rinçage en cours..."),
        Sechage("Séchage en cours..."),
        UrgenceAttenteRinçage("En attente de rinçage."),
        UrgenceAttenteLavage("En attente de lavage.");
        
        
        private String etat;
        EtatMachine(String e) {
            this.etat = e;
        }
        public String toString() {
            
            return String.valueOf(etat);
        }
    }
    
    /**
     * Constructeur de la classe
     */
    public Machine() {
        // initialisation de la machine (état initial).
        this.Alimentation = AlimentationMachine.Allumee;
        this.EtatMachine = EtatMachine.AttenteJeton;
        this.listeners = new EventListenerList();
        this.infoMessage = "Insérez des jetons";
        this.isRunning = false;
        
        
        timerCycle = new Timer(this.delay, (ActionEvent ae) -> {
    		
    		if (this.EtatMachine == EtatMachine.Lavage) {
    			// Lavage en cours
        		pourcentageCycle++;
        		if (this.pourcentageCycle == 100) {
        			// cycle terminé
        			this.timerCycle.stop();
        			this.pourcentageCycle = 0;
        			LavageFini();
        		}
        	}
    		if (this.EtatMachine == EtatMachine.Rinçage) {
    			// Rinçage en cours
        		pourcentageCycle++;
        		if (this.pourcentageCycle == 100) {
        			// cycle terminé
        			this.timerCycle.stop();
        			this.pourcentageCycle = 0;
        			RincageFini();
        		}
        	}
    		if (this.EtatMachine == EtatMachine.Sechage) {
    			// Sechage en cours
        		pourcentageCycle++;
        		if (this.pourcentageCycle == 100) {
        			// cycle terminé
        			this.timerCycle.stop();
        			this.pourcentageCycle = 0;
        			SechageFini();
        		}
        	}
        	
			this.indiquerEtatMachine(EtatMachine);
            this.indiquerPourcentage(pourcentageCycle);
    	
    	});
        
        // timer d'urgence
    	timerUrgence = new Timer(this.delayUrgence, (ActionEvent ae) -> {
		
    	if(this.EtatMachine == EtatMachine.UrgenceAttenteLavage || this.EtatMachine == EtatMachine.UrgenceAttenteRinçage) {
			
    		tempsRestant--;
	    	if(this.tempsRestant == 0) {
	    		this.timerUrgence.stop();
	    		SechageFini();
	    		this.indiquerEtatMachine(EtatMachine);
	        	this.indiquerPourcentage(pourcentageCycle);
	        	this.indiquerTempsRestant(infoMessage);
	    		
	    	}
	    	else {
	    		this.indiquerEtatMachine(EtatMachine);
	        	this.indiquerPourcentage(pourcentageCycle);
	        	this.indiquerTempsRestant("Arrêt d'urgence, "+tempsRestant+"secondes avant fin définitive du programme !");
	    	}
    	}
    	
		        	
    	});
    }
    
    /**
     * Getters et setters
     */
    AlimentationMachine getAlimentationMachine() {
        
        return Alimentation;
    }
    
    EtatMachine getEtatMachine() {
        
        return EtatMachine;
    }
    
    String getInfoMessage() {
    	
    	return infoMessage;
    }
    
    Boolean getIsRunning() {
    	
    	return isRunning;
    }
    
    private void setIsRunning(Boolean pIsRunning) {
    	
    	this.isRunning = pIsRunning;
    }
    
    private void setIsUrgence(Boolean pIsUrgence) {
    	
    	this.isRunning = pIsUrgence;
    }
    
    /**
     * Gestion des actions fenetre/interface machine
     */
    public void AppuiSurBTAjoutJeton() {
        
        switch(getAlimentationMachine()) {
        
        	case Allumee:
        		
        		switch(getEtatMachine()) {
        		
	        		case AttenteJeton:
	        			this.isRunning = true;
	        			this.infoMessage = "Lavage en cours...";
	        			this.EtatMachine = EtatMachine.Lavage;
	        			this.timerCycle.start();
	        			
	        			this.indiquerMessageInfo(this.infoMessage);
	        			this.indiquerIsRunning(this.isRunning);
	        			break;
	        			
	        			
	        		case UrgenceAttenteRinçage:
	        			// ne rien faire
	        			
	        			break;
	        			
	        		case UrgenceAttenteLavage:
	        			// ne rien faire
	        			
	        			break;
	        		
	        		}
        		
        		break;
        		
        	case Eteinte:
        		
        		
        		break;
        	
        } 
    }
    
    public void AppuiSurBTArretUrgence() {     
            
            switch(getAlimentationMachine()) {
            
            	case Allumee:
            		
            		switch(getEtatMachine()) {            		
            			
            		case Lavage:
            			// arrêt du chrono
            			this.timerCycle.stop();
            			this.EtatMachine = EtatMachine.UrgenceAttenteLavage;	
            			this.tempsRestant = 6;
            			this.timerUrgence.start();

            			
            			
            			break;
            			
            		case Rinçage:
            			// arrêt du chrono
            			this.timerCycle.stop();
            			
            			this.EtatMachine = EtatMachine.UrgenceAttenteRinçage;
            			this.tempsRestant = 6;
            			this.timerUrgence.start();
            			
            			
            			break;
            			
            		case Sechage:
            			// arrêt du chrono
            			this.timerCycle.stop();		
            			SechageFini();
            			this.indiquerIsRunning(false);
            			this.indiquerPourcentage(pourcentageCycle);
            			this.indiquerEtatMachine(EtatMachine);
            			            			
            			break;           			
            		
            		}
            		
            		break;
            		
            	case Eteinte:
            		
            		break;      	
            }
    }
    
    public void AppuiSurBTReprendre() {
            
            switch(getAlimentationMachine()) {
            
            	case Allumee:
            		
            		switch(getEtatMachine()) {    			
            			
            		case UrgenceAttenteRinçage:
            			this.timerUrgence.stop();
            			this.EtatMachine = EtatMachine.Rinçage;
            			this.infoMessage = "Rinçage en cours...";
            			this.indiquerMessageInfo(infoMessage);
            			this.timerCycle.restart();
            			      		
            			break;
            			
            		case UrgenceAttenteLavage:
            			this.timerUrgence.stop();
            			this.EtatMachine = EtatMachine.Lavage;
            			this.infoMessage = "Lavage en cours...";
            			this.indiquerMessageInfo(infoMessage);
            			this.timerCycle.restart();
            			
            			break;
            		
            		}
            		
            		break;
            		
            	case Eteinte:
            		
            		break;
            	
            }
    	
    }
    
    public void LavageFini()
    {
		switch(getAlimentationMachine()) {
		        
		    	case Allumee:
		    		
		    		switch(getEtatMachine()) {
		    		
		    		case AttenteJeton:
		    		
		    			break;
		    			
		    		case Lavage:
		    			// Passer au statut Rinçage suite au timer écoulé dans la jForm.
		    			this.EtatMachine = EtatMachine.Rinçage;
		    			this.infoMessage = "Rinçage en cours...";
		    			this.indiquerMessageInfo(infoMessage);
		    			timerCycle.start();
		    			break;
		    			
		    		case Rinçage:

		    			
		    			break;
		    			
		    		case Sechage:

		    			
		    			break;
		    			
		    		case UrgenceAttenteRinçage:
		    			
		    			break;
		    			
		    		case UrgenceAttenteLavage:
		    			
		    			break;
		    		
		    		}
		    		
		    		break;
		    		
		    	case Eteinte:
		    		
		    		break;
    	
    	}
    }
    
    public void RincageFini() {
				switch(getAlimentationMachine()) {
		        
		    	case Allumee:
		    		
		    		switch(getEtatMachine()) {
		    		
		    		case AttenteJeton:
		    		
		    			break;
		    			
		    		case Lavage:
		    			this.LavageFini();
		    			break;
		    			
		    		case Rinçage:
		    			this.EtatMachine = EtatMachine.Sechage;
		    			this.infoMessage = "Sechage en cours...";
		    			this.indiquerMessageInfo(infoMessage);
		    			this.timerCycle.start();
		    			break;
		    			
		    		case Sechage:
		
		    			
		    			break;
		    			
		    		case UrgenceAttenteRinçage:
		    			
		    			break;
		    			
		    		case UrgenceAttenteLavage:
		    			
		    			break;
		    		
		    		}
		    		
		    		break;
		    		
		    	case Eteinte:
		    		
		    		break;
		
		}
    	
    }
    
    public void SechageFini() {
				switch(getAlimentationMachine()) {
		        
		    	case Allumee:
		    		
		    		switch(getEtatMachine()) {
		    		
		    		case AttenteJeton:
		    		
		    			break;
		    			
		    		case Lavage:
		    			this.LavageFini();
		    			break;
		    			
		    		case Rinçage:
		    			this.RincageFini();
		    			break;
		    			
		    		case Sechage:
		    			this.EtatMachine = EtatMachine.AttenteJeton;
	    	    		this.indiquerEtatMachine(this.EtatMachine);
	    	    		this.infoMessage = "Lavage terminé !";
	    	    		this.indiquerMessageInfo(infoMessage);
	    	    		this.isRunning = false;
	    	    		this.indiquerIsRunning(false);
	    	    		this.pourcentageCycle = 0;
		    			break;
		    			
		    		case UrgenceAttenteRinçage:
		    			this.EtatMachine = EtatMachine.AttenteJeton;
	    	    		this.infoMessage = "Lavage terminé !";
	    	    		this.indiquerMessageInfo(infoMessage);
	    	    		this.isRunning = false;
	    	    		this.indiquerIsRunning(false);
	    	    		this.pourcentageCycle = 0;
		    			break;
		    			
		    		case UrgenceAttenteLavage:
		    			this.EtatMachine = EtatMachine.AttenteJeton;
	    	    		this.infoMessage = "Lavage terminé !";
	    	    		this.indiquerMessageInfo(infoMessage);
	    	    		this.isRunning = false;
	    	    		this.indiquerIsRunning(false);
	    	    		this.pourcentageCycle = 0;
		    			break;

		    		}
		    		
		    		break;
		    		
		    	case Eteinte:
		    		
		    		break;
		
		}
    	
    }
    
    /**
     * 
     * Gestion des listeners
     */
    private final EventListenerList listeners;
    
    public void addMachineListener(IfMachineListener listener) {
        
        listeners.add(IfMachineListener.class, listener);
    }
    
    public void removeMachineListener(IfMachineListener listener) {
        
        listeners.remove(IfMachineListener.class, listener);
    }
    
    public IfMachineListener[] getMachineListeners() {
        
        // retourner les écouteurs de type IfMachineListener de la liste des écouteurs.
        return listeners.getListeners(IfMachineListener.class);
    }
    
    /**
     * 
     * Gestion des évènements à déclencher aurpès des listeners
     */
    
    private void indiquerAlimentationMachine(AlimentationMachine pAlimentationMachine) {
        // déclenche un évènement auprès des suiveurs pour indiquer si la machine est allumée ou éteinte.
        for (IfMachineListener listener : getMachineListeners()) {
            
            // Fire l'évènement
            listener.alimentationMachine(pAlimentationMachine);;
        }
    }
    
    private void indiquerEtatMachine(EtatMachine pEtatMachine) {
        // Déclenche un évènement auprès des suiveurs pour indiquer l'état de la machine quant au cycle de lavage.
        for (IfMachineListener listener : getMachineListeners()) {
            
            // Fire l'évènement
            listener.etatMachine(pEtatMachine);
        }
        
    }
    
    private void indiquerMessageInfo(String pMessage) {
    	// déclenche l'évènement info auprès des suiveurs pour indiquer un éventuel message d'indication
    	
    	for (IfMachineListener listener : getMachineListeners()) {
    		
    		// Fire l'évènement
    		listener.info(pMessage);

    	}
    }
    
    
    private void indiquerIsRunning(Boolean pIsRunning) {
    	// déclenche l'évènement IsUrgence auprès des suiveurs pour indiquer si un lavage est en cours pour gérer le bouton d'ajout de jetons
    	
    	for (IfMachineListener listener : getMachineListeners()) {
    		
    		// Fire l'évènement 
    		listener.isRunning(pIsRunning);;
    	}
    	
    }
    
    private void indiquerPourcentage(int pPourcentage) {
    	// déclenche l'évènement progression aurpès des suiveurs pour indiquer la progression du lavage pour la barre de défilement.
    	
    	for (IfMachineListener listener : getMachineListeners()) {
    		
    		// Fire l'évènement 
    		listener.indiquerPourcentage(pPourcentage);;
    	}
    }
    
    private void indiquerTempsRestant(String pTempsRestant) {
    	// déclenche l'évènement progression aurpès des suiveurs pour indiquer la progression du lavage pour la barre de défilement.
    	
    	for (IfMachineListener listener : getMachineListeners()) {
    		
    		// Fire l'évènement 
    		listener.indiquerTempsRestant(pTempsRestant);;
    	}
    }
}
