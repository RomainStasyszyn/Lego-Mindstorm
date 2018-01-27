package avancer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import avancer.Couleur;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;


/**
 * Classe d'apprentissage permettant de prendre des mesures pour un ensemble de couleurs puis 
 * de sauvegarder dans un fichier texte les valeurs obtenues afin de les lire lorsque l'on aura
 * besoin de vérifier si la couleur qu'on teste a été apprise par l'EV3.
 *
 */
public class Apprentissage {
	
	
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
	 * Méthode d'apprentissage mesurant la couleur courante et ajoute les valeurs
	 * R, G et B associées dans les listes correspondantes.
	 * 
	 * @param couleur la couleur courante que l'on mesure.
	 * @param p le port utilisé de l'EV3.
	 * @param htcs le HiTechnicColorSensor courant.
	 * @param sp le SampleProvider courant.
	 * @param size la taille du tableau sample.
	 */
	
	protected static void prendreMesure(Couleur couleur, SampleProvider sp, int size) {
		float[] sample = new float[size];
		sp.fetchSample(sample, 0);
		for(int compteur = 0 ; compteur < size ; compteur++) {
			sample[compteur] = convertToRGBValue(sample[compteur]);
		}
		LCD.clearDisplay();
		LCD.drawString("Rouge : " + sample[0], 0, 3);
		LCD.drawString("Vert : " + sample[1], 0, 4);
		LCD.drawString("Bleu : " + sample[2], 0, 5);
		Delay.msDelay(2000);
		couleur.getRed().add(sample[0]);
		couleur.getGreen().add(sample[1]);
		couleur.getBlue().add(sample[2]);
	}
	
	
	/**
	 * Fonction sauvegardant les valeurs des mesures de chaque couleur dans le fichier paletteDeCouleurs.txt.
	 */
	
	
	protected static void sauvegarder(ArrayList<Couleur> couleurs) {
		String chemin = "paletteDeCouleurs.txt";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(chemin)));
			for (int compteur = 0 ; compteur < couleurs.size() ; compteur ++) {
				for (int cpt = 0 ; cpt < couleurs.get(compteur).getRed().size() ; cpt++) {
					writer.write(Float.toString(couleurs.get(compteur).getRed().get(cpt)) + "f-");
					writer.write(Float.toString(couleurs.get(compteur).getGreen().get(cpt)+255) + "f-");
					writer.write(Float.toString(couleurs.get(compteur).getBlue().get(cpt)+510) + "f-");
				}
				writer.newLine();
			}
			writer.close();
		} catch (IOException ie) {
			LCD.drawString(ie.toString(), 0, 4);
		}
	}
	
	protected static ArrayList<Couleur> lancer_apprendre(Port port, SampleProvider colorRGBSensor, int sampleSize){
		ArrayList <Couleur> couleurs = new ArrayList <Couleur>();
		int position = -1;
		do {
			LCD.clearDisplay();
			LCD.drawString("Mesure(droit)", 0, 3);
			LCD.drawString("Couleur(gauche)", 0, 4);
			LCD.drawString("Finir(enter)", 0, 5);
			Button.waitForAnyPress();
			
			if (Button.RIGHT.isDown()) {
				if (!couleurs.isEmpty())
					prendreMesure (couleurs.get(position), colorRGBSensor, sampleSize);
			}else if (Button.LEFT.isDown()) {
				couleurs.add(new Couleur());
				position++;
				LCD.clearDisplay();
				LCD.drawString("Nouvelle couleur", 0, 4);
				Delay.msDelay(1500);
			}
		} while (!Button.ENTER.isDown());
		return couleurs;
	
	}
	
	protected static void apprendre(Port port, SampleProvider colorRGBSensor, int sampleSize, char c){
		ArrayList <Couleur> couleurs = lancer_apprendre(port, colorRGBSensor, sampleSize);
		sauvegarder(couleurs);
	}
}
