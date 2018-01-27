package avancer;

import avancer.Distance;

/**
 * Classe Distance représentant l'écart entre les couleurs enregistrées et celle mesurée à l'instant.
 * Avec cette distance on peut décider quelle est la couleur qui se rapproche le plus de celle que l'on mesure
 * parmi celles enregistrées.
 * Une distance contient le numéro de la couleur à laquelle est associée la distance relative aux trois composantes.
 */

public class Distance implements Comparable<Distance> {
	

	/**
	 * Attributs de la classe Distance, numeroCouleur correspond à l'identifiant de la couleur associée et distance
	 * correspond à la distance entre la mesure courante de la couleur enregistrée et la couleur testée actuellement.
	 */
	
	private int numeroCouleur;
	private float distance;
	
	
	/**
	 * Constructeur de la classe Distance initialisant le numéro de la couleur associée à cette mesure et la dis
	 * tance relative de cette même mesure par rapport à la couleur testée.
	 */
	
	public Distance(int num, float d) {
		numeroCouleur = num;
		distance = d;
	}
	
	
	/**
	 * Accesseur pour le l'attribut distance.
	 * @return la valeur de l'attribut distance courant.
	 */
	
	protected float getDistance() {
		return distance;
	}
	
	
	/**
	 * Accesseur pour le l'attribut numeroCouleur.
	 * @return la valeur de l'attribut numeroCouleur courant.
	 */
	
	protected int getNumeroCouleur() {
		return this.numeroCouleur;
	}
	
	
	/**
	 * Méthode de comparaison entre deux couleurs décidant laquelle est la plus proche de la couleur testée.
	 * @return un entier valant -1 si la deuxième couleur est plus proche, 0 si égales et 1 si la première est chosie.
	 */

	@Override
	public int compareTo(Distance d1) {
		if(this.getDistance() > d1.getDistance()) {
			return 1;
		}
		else if(this.getDistance() < d1.getDistance()) {
			return -1;
		}
		else {
			return 0;
		}
	}
}
