package avancer;

import java.util.ArrayList;

/**
 * Classe Couleur représentant une couleur apprise par l'EV3.
 * Une un objet de type Couleur contient trois champs.
 * Une liste de valeurs de teinte rouge mesurées pour la Couleur courante.
 * Une liste de valeurs de teinte verte mesurées pour la Couleur courante.
 * Une liste de valeurs de teinte bleue mesurées pour la Couleur courante.
 * 
 *
 *
 */
public class Couleur {

	/** 
	 * ArrayList contenant respectivement les différentes valeurs en rouge, 
	 * vert et bleu d'une couleur apprise.
	 */
	
	private ArrayList <Float> red;
	private ArrayList <Float> green;
	private ArrayList <Float> blue;
	
	/**
	 * Constructeur de la classe Couleur initialisant les listes de valeurs RGB.
	 */
	
	Couleur () {
		red = new ArrayList <Float> ();
		green = new ArrayList <Float> ();
		blue = new ArrayList <Float> ();
	}
	
	/**
	 * Accesseur de la liste de valeurs de la teinte rouge.
	 * 
	 * @return red.
	 */
	
	ArrayList <Float> getRed () {
		return red;
	}
	
	/**
	 * Accesseur de la liste de valeurs de la teinte verte.
	 * 
	 * @return green.
	 */
	
	ArrayList <Float> getGreen () {
		return green;
	}
	
	/**
	 * Accesseur de la liste de valeurs de la teinte bleue.
	 * 
	 * @return blue.
	 */
	
	ArrayList <Float> getBlue () {
		return blue;
	}
}
