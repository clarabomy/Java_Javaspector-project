
package javaspector.game.investigation;

import java.util.ArrayList;
import static javaspector.game.Game.convertArrayList;
import static javaspector.game.Game.getConsole;
import static javaspector.game.Game.isEndedGame;
import javaspector.game.character.Investigator;
import javaspector.game.character.Sex;
import javaspector.game.character.Victim;
import javaspector.game.character.suspect.Suspect;

/**
 *
 * Contain the investigation
 * @author Clara BOMY
 */ 
public class Investigation {
    protected Investigator m_player;
    protected static ArrayList <Suspect> m_suspectsList;
    protected Victim m_victim;
    protected InvestElement m_crimeWeapon;
    protected InvestElement m_crimeScene;
    protected static String m_genderJob;

    /** 
     * Constructor of the class Investigation
     * @param player        the investigator
     * @param suspectsList  a list with all the suspects : 1 Murderer, 1 CrimePartner and Innocents
     * @param corpse        the victim
     * @param weapon        the investigation element 1
     * @param scene         the investigation element 2
     */ 
    public Investigation(Investigator player, ArrayList <Suspect> suspectsList, Victim corpse, InvestElement weapon, InvestElement scene) {
        m_player = player;
        Investigation.m_suspectsList = suspectsList;
        m_victim = corpse;
        m_crimeWeapon = weapon;
        m_crimeScene = scene;
        m_genderJob = player.getSex() == Sex.HOMME? "Enquêteur" : "Enquêtrice";
    }
    
    /** 
     * Getter of the class
     * @return genderJob    "Enquêteur" or "Enquêtrice" according to the player's sex
     */ 
    public static String getGenderJob() {
        return m_genderJob;
    }
    
    /** 
     * Getter of the class
     * @return investigator     the current instance of the player
     */ 
    public Investigator getInvestigator() {
        return m_player;
    }
    
    /** 
     * Getter of the class
     * @return suspectsNameList     a list with all the name's suspects
     */ 
    public static ArrayList <String> getSuspectsNameList() {
        ArrayList <String> listName = new ArrayList();
        for (Suspect currentSuspect : m_suspectsList) {//route all m_suspectsList by putting current element in currentSuspect
            listName.add(currentSuspect.getFullName());
        }
        return listName;
    }
    
    /** 
     * Getter of the class
     * @return suspectsList     a list of the suspects
     */ 
    public ArrayList <Suspect> getSuspectsList() {
        return m_suspectsList;
    }
    
    /** 
     * Getter of the class
     * @return victim   the current instance of the Victim
     */ 
    public Victim getVictim() {
        return m_victim;
    }
    
    /** 
     * Getter of the class
     * @return crimeWeapon  the current instance of the InvestElement
     */ 
    public InvestElement getCrimeWeapon() {
        return m_crimeWeapon;
    }
    
    /** 
     * Getter of the class
     * @return crimeScene   the current instance of the InvestElement
     */ 
    public InvestElement getCrimeScene() {
        return m_crimeScene;
    }
    
    /** 
     * Main menu of the investigation
     */ 
    public void investigationMenu() {
        boolean previousMenu = false;
        //menu choices won't change with time
        String[] choicesList = {"Revoir mon test d'aptitude\n",
                                "Passer en revue les éléments de l'enquête.",
                                "Consulter mes indices.",
                                "Appeler un suspect.\n", 
                                "Retourner au bureau."};
        
        do {
            switch (getConsole().clean().display(m_player.getFullName(), "Aujourd'hui, je vais...", choicesList).execChoice()) {
                case 1:
                    getConsole().clean();
                    m_player.displayStats();
                    break;
                case 2:
                    elementsMenu();
                    break;
                case 3:
                    getConsole().clean();
                    if (m_player.getClueList().isEmpty()) {
                        getConsole().display(m_genderJob, "Je n'ai pas encore le moindre indice...");
                    }
                    else {
                        m_player.consultClues();
                    }
                    getConsole().execContinue("Continuer");
                    break;
                case 4:
                    suspectsMenu();
                    if (isEndedGame()) {//if player arrest somebody...
                        previousMenu = true;
                    }
                    break;
                case 5:
                    previousMenu = true;
                    break;
            }
        } while (!previousMenu);
    }
    
    /** 
     * Submenu of the investigation for the elements
     */ 
    public void elementsMenu() {
        boolean previousMenu = false;
        String[] choicesList = {"Autopsier la victime.", 
                                "Analyser l'arme du crime.", 
                                "Fouiller la scène du crime.\n",
                                "Retourner à l'enquête."};
        
        do {
            switch(getConsole().clean().display(m_genderJob, "Voyons voir les éléments de l'enquête...", choicesList).execChoice()) {
                case 1: 
                    m_victim.analyse(m_player);
                    break;
                case 2:
                    m_crimeWeapon.analyse(m_player);
                    break;
                case 3:
                    m_crimeScene.analyse(m_player);
                    break;
                case 4:
                    previousMenu = true;
                    break;
            }
            if (!previousMenu) {//if execute action, display result (and stop process)
                getConsole().execContinue("Vous relevez les indices");
            }
        } while (!previousMenu);
    }
    
    /** 
     * Submenu of the investigation for the suspects
     */ 
    public void suspectsMenu() {
        getConsole().clean();
        boolean previousMenu = false;
        
        do {
            //suspects list can evolve with time (set innocent)
            String[] suspectsList = new String[m_suspectsList.size() + 1];
            for (int i = 0; i < m_suspectsList.size(); i++) {
                String text = m_suspectsList.get(i).getFullName();
                if (m_suspectsList.get(i).isConsideredInnocent()) {
                    text += " - potentiellement innocent";
                }
                if (i == m_suspectsList.size() - 1) {
                    text += "\n";
                }
                suspectsList[i] = text;
            }
            suspectsList[m_suspectsList.size()] = "Annuler";
            
            int target = getConsole().clean().display(m_genderJob, "Je veux voir", suspectsList).execChoice() - 1;
            if (target == m_suspectsList.size()) {//last option : leave
                previousMenu = true;
            }
            else {
                m_suspectsList.get(target).displayStats().presentCharacter();
                
                //choice of list depend on player choices
                ArrayList <String> choicesList = new ArrayList();
                choicesList.add("L'interroger.");
                if (!m_suspectsList.get(target).isConsideredInnocent()) {
                    choicesList.add("L'innocenter."); 
                }
                choicesList.add("L'arrêter.\n"); 
                choicesList.add("Retourner à l'enquête.");
                
                int choice = getConsole().display("Que voulez vous faire?", convertArrayList(choicesList)).execChoice();
                if (m_suspectsList.get(target).isConsideredInnocent() && choice >= 2) {//if already innocented, is not displayed : must increment to match user choice
                    choice++;
                }
                switch(choice) {
                    case 1: 
                        m_suspectsList.get(target).beInterrogated(m_player);
                        break;
                    case 2:
                        m_suspectsList.get(target).beDisculpated();
                        break;
                    case 3:
                        m_suspectsList.get(target).beArrested();
                        if (isEndedGame()) {
                            previousMenu = true;
                        }
                        break;
                    case 4:
                        previousMenu = true;
                        break;
                }
            }
        } while (!previousMenu);
    }
}
