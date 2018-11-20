
package project.game.investigation;




/**
 *
 * @author ISEN
 */
public class Deposition extends Clue {
    protected boolean m_isLie;
    protected DepositionType m_category;
    protected String m_depositor;//garde une trace de qui a parlé

    
    /*$$ CONSTRUCTOR $$*/
    //nouvelle partie et chargement
    public Deposition(String depositor, String content, DepositionType category, boolean isLie) {
        super(content);
        this.m_isLie = isLie;
        this.m_category = category;
        this.m_depositor = depositor;
    }

    public boolean isLie() {
        return m_isLie;
    }
    
    /*$$ METHODS $$*/
    @Override
    public void display() {
        //Témoignage de nom_personnage : ce qu'il a dit
        //m_console.display(this.getSuspect().getFullName(), this.getContent(), false).execContinue();
    }
}