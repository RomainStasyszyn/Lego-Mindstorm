package avancer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import lejos.hardware.Button;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;
import avancer.Couleur;
import avancer.Distance;


/**
 * Classe Reconnaissance permettant de mesurer plusieurs couleurs et à chaque fois tester s'il la reconnait
 * parmi les couleurs apprisent précédemment via le programme d'apprentissage.
 * 
 *
 */

public class Reconnaissance {
	
	
	/**
	 * Méthode permettant de convertir une valeur flottante mesurées pour R, G ou B en
	 * une valeur comprise entre 0 et 255.
	 * 
	 * @param f la valeur flottante à convertir.
	 * @return la valeur R, G ou B convertie.
	 */
	
	private static float convertToRGBValue(float f) {
		return 255 * f;
	}
	/**
	 * Méthode permettant de récupérer la valeur maximale contenue dans un tableau d'entiers.
	 * 
	 * @param tab le tableau duquel on souhaite récupérer la valeur maximale.
	 * @return la valeur maximale contenue dans le tableau.
	 */
	
	private static int valeurMax(int [] tab) {
		int max = tab[0];
		int positionMax = 0;
		for(int i = 1 ; i < tab.length ; i++) {
			if(tab[i] > max) {
				max = tab[i];
				positionMax = i;
			}
		}
		return positionMax;
	}
	
	
	
	/**
	 * Méthode de reconnaissance de couleur qui parcourt la liste des valeurs déjà enregistrées calcule la somme des 
	 * distances R, G et B pour chaque mesures enregistrées, les mets dans une liste que l'on trie pour obtenir la 
	 * couleur revenant le plus parmi celles ayant les plus petites distances avec la couleur testée. Ensuite on crée un 
	 * tableau dont chaque indice correspond à chaque numéro de couleur apprise durant la phase d'apprentissage, on
	 * incrémente la position correspondante à la couleur dont la distance fait partie des plus proches. Ainsi on peut
	 * extraire la valeur maximale dont l'incide sera le numéro de la couleur la plus proche. 
	 * 
	 * @param couleur l'ArrayList contenant les valeurs déjà enregistrées.
	 * @param p le port de l'EV3 utilisé pour prendre la valeur avec le capteur.
	 * @param htcs le HiTechnicColorSensor courant.
	 * @param sp le SampleProvider courant.
	 * @param size la taille du tableau Sample récupérant le valeurs mesurées par le capteur après chaque mesure.
	 */
	
	
	protected static int reconnaitre(ArrayList <Couleur> couleur, Port p, SampleProvider sp, int size) {
		ArrayList <Distance> distancesListe = new ArrayList <Distance>();
		int nbCouleurs = couleur.size();
		int nbMesuresCouleur = couleur.get(0).getRed().size();
		int tabPlusProche [] = new int [nbCouleurs];
		float distance = 0;
		float[] sample = new float[size];
		try{
			sp.fetchSample(sample, 0);
		}catch(Exception e){
			Button.LEDPattern(1);
		}
		
		for(int compteur = 0 ; compteur < size ; compteur++) {
			sample[compteur] = convertToRGBValue(sample[compteur]);
		}
		for(int compteurCouleurs = 0 ; compteurCouleurs < couleur.size() ; compteurCouleurs++) {
			for(int compteurRGB = 0 ; compteurRGB < couleur.get(compteurCouleurs).getRed().size() ; compteurRGB++) {
				distance = (float)Math.pow(couleur.get(compteurCouleurs).getRed().get(compteurRGB) - sample[0], 2);
				distance += (float)Math.pow(couleur.get(compteurCouleurs).getGreen().get(compteurRGB) - (sample[1]+255), 2);
				distance += (float)Math.pow(couleur.get(compteurCouleurs).getBlue().get(compteurRGB) - (sample[2]+510), 2);
				distance = (float)Math.sqrt(distance);
				distancesListe.add(new Distance(compteurCouleurs, distance));
			}
		}
		int position = choixMeilleureCouleur(distancesListe, tabPlusProche, nbMesuresCouleur);
		return position;
	}
	
	/**
	 * 
	 * @param distancesListe Liste contenant les distances pour chaque mesure enregistrées entre elles et la couleur testée actuellement.
	 * @param tabPlusProche Tableau contenant autant de case qu'il y a de couleurs différentes enregistrées, l'indice numéro zéro correspond
	 * à la couleur zéro (la première enregistrée). A chaque choix de la meilleure mesure, il y a incrémentation de l'entier contenu en case
	 * i (i correspondant à l'identifiant de la couleur choisie).
	 * @param nbMesuresCouleur Entier correspondant au nombre de mesures effectuées pour toutes les couleurs enregistrées.
	 */
	
	protected static int choixMeilleureCouleur(ArrayList <Distance> distancesListe, int [] tabPlusProche, int nbMesuresCouleur) {
		int position = 0;
		Collections.sort(distancesListe);
		for(int i = 0 ; i < tabPlusProche.length ; i++)
			tabPlusProche[i] = 0;
		for(int compt = 0 ; compt < nbMesuresCouleur ; compt++) {
			position = distancesListe.get(compt).getNumeroCouleur();
			tabPlusProche[position] += 1;
		}
		position = valeurMax(tabPlusProche);
		//LCD.clearDisplay();
		//LCD.drawString("Couleur : " + position, 0, 4);
		return position;
	}
	/**
	 * Méthode récupérant les valeurs contenues dans le fichier paletteDeCouleurs.txt et les stockant dans l'ArrayList de
	 * couleurs (type Couleur) afin de s'en servir dans la phase de reconnaissance de couleur.
	 * 
	 * @param couleurs l'ArrayList que l'on remplie avec les couleurs enregistrées dans le fichier paletteDeCouleurs.txt.
	 */
	
	protected static void charger(ArrayList<Couleur> couleurs) {
		try (BufferedReader br = new BufferedReader(new FileReader("paletteDeCouleurs.txt"))) {
			String ligneCourante = " ";
			int compteurCouleurs = 0;
			while((ligneCourante = br.readLine()) != null) {
				couleurs.add(new Couleur());
				String[] tableau = ligneCourante.split("-");
				for(int compteur = 0 ; compteur + 2 < tableau.length-1 ; compteur += 3) {
					couleurs.get(compteurCouleurs).getRed().add(Float.parseFloat(tableau[compteur]));
					couleurs.get(compteurCouleurs).getGreen().add(Float.parseFloat(tableau[compteur + 1]));
					couleurs.get(compteurCouleurs).getBlue().add(Float.parseFloat(tableau[compteur + 2]));
				}
				compteurCouleurs++;
			}
			br.close();
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	
	public static void test(ArrayList <Couleur> couleur, Port port, SampleProvider colorRGBSensor, int sampleSize) {
		ArrayList <Couleur> couleurs = new ArrayList <Couleur>();
		charger(couleurs);
		if (!couleurs.isEmpty()) {
			reconnaitre(couleurs, port, colorRGBSensor, sampleSize);
		}
	}
}
