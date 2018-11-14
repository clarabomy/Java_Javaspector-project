
package project.game.character;

/**
 *
 * @author ISEN
 */
public abstract class LiveCharacter extends Character {
    private static final int M_SIDES = 100;
    private static final int M_CRITICAL_SUCCESS = 15;
    private static final int M_CRITICAL_FAILURE = 95;
    private static final int M_ZONE_VALUE = 5; 
    private static int m_coeffDiff; //pour 50 : 1.25 = {44, 38, 32, 25} / 1 = {45, 40, 35, 30} / 0.75 = {47, 43, 39, 35}
    private static int m_lastDiceValue; //valeur tirée du dé précédent
    private static int m_lastDiceValidStage; //palier de validation du lancer précédent

    
    /*$$ CONSTRUCTOR $$*/
    public LiveCharacter(String name, String surname, Sex sex, int age) {
        super(name, surname, sex, age);
    }
    
    
    /*$$ METHODS $$*/
    public abstract void displayStats();
    
   
    public static DiceResult rollDice(int validStage, String display, boolean newThrow) {
        //lance un dé
        int diceValue = (int) (Math.random() * M_SIDES) + 1;//donne valeur entre 1 et 100
        if (display != null) System.out.printf("Jet de %s : %d\n", display, diceValue);
        
        
        //si ce lancé est dépendant du précédent, ajuste l'intervalle de validité
        //si échec simple au précédent lancer et qu'on prend en compte l'ancien lancer
        if (!newThrow && m_lastDiceValue > m_lastDiceValidStage && m_lastDiceValue < M_CRITICAL_FAILURE) {
            int zone = 0;
            do zone++; while (m_lastDiceValue > m_lastDiceValidStage + zone * ((M_SIDES - m_lastDiceValidStage) / 4));
            validStage -= (int)(M_ZONE_VALUE * zone * m_coeffDiff); //application du malus dû à l'échec précédent
        }
        //si echec critique = loupe autres lancers
        
        
        //met à jour les attributs
        m_lastDiceValue = diceValue;
        m_lastDiceValidStage = validStage;
    
        
        //détermine le résultat
        //plus fréquent
        if (diceValue <= validStage && diceValue > M_CRITICAL_SUCCESS) return DiceResult.SUCCESS;
        else if (diceValue > validStage && diceValue < M_CRITICAL_FAILURE) return DiceResult.FAILURE;
        
        //moins fréquent
        else if (diceValue <= M_CRITICAL_SUCCESS) return DiceResult.CRITIC_SUCCESS;
        else return DiceResult.CRITIC_FAILURE;//if (throwResult >= M_CRITICAL_FAILURE)
    }
   
    
    public static DiceResult rollMultiDice(int[] validStage, String[] display, boolean newThrow) {
        DiceResult action = DiceResult.ERROR;
        for (int i = 0; i < validStage.length; i++) action = rollDice(validStage[i], (i < display.length)? display[i] : null, (i == 0)? newThrow : false);
        return action;
    }
}
