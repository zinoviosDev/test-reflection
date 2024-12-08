import model.Informations;
import model.PaysEuropeen;
import org.junit.jupiter.api.Test;
import util.ReflectionUtils;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionTest {

	@Test
	void testReflectionAvecCheminImbrique() {
		// Créer une instance de test
		PaysEuropeen pays = new PaysEuropeen(true);
		Informations infos = new Informations(pays);

		// Préparer le chemin imbriqué sous forme de chaîne
		String chemin = "pays.isSpeakFrench";

		// Tester la navigation dans les champs
		Object valeur = ReflectionUtils.getValeurAvecChemin(infos, chemin.split("\\."));

		// Vérifier que la valeur est correcte
		assertNotNull(valeur);
		assertTrue(valeur instanceof Boolean);
		assertTrue((Boolean) valeur); // Vérifie que isSpeakFrench == true
	}

	@Test
	void testReflectionAvecValeurNulle() {
		// Créer une instance avec un pays null
		Informations infos = new Informations(null);

		// Préparer le chemin imbriqué sous forme de chaîne
		String chemin = "pays.isSpeakFrench";

		// Tester la navigation dans les champs
		Object valeur = ReflectionUtils.getValeurAvecChemin(infos, chemin.split("\\."));

		// Vérifier que la valeur est null
		assertNull(valeur);
	}

	@Test
	void testReflectionAvecCheminInvalide() {
		// Créer une instance de test
		PaysEuropeen pays = new PaysEuropeen(true);
		Informations infos = new Informations(pays);

		// Préparer un chemin invalide
		String chemin = "pays.invalidField";

		// Tester la navigation dans les champs
		Object valeur = ReflectionUtils.getValeurAvecChemin(infos, chemin.split("\\."));

		// Vérifier que la valeur est null (champ inexistant)
		assertNull(valeur);
	}

	@Test
	void testPerformanceAvecCache() {
		// Créer une instance de test
		PaysEuropeen pays = new PaysEuropeen(true);
		Informations infos = new Informations(pays);

		// Préparer le chemin imbriqué sous forme de chaîne
		String chemin = "pays.isSpeakFrench";

		// Mesurer le temps pour 10 000 itérations
		long start = System.currentTimeMillis();

		for (int i = 0; i < 10000; i++) {
			Object valeur = ReflectionUtils.getValeurAvecChemin(infos, chemin.split("\\."));
			assertNotNull(valeur); // Vérifier que la valeur est toujours valide
		}

		long end = System.currentTimeMillis();
		System.out.println("Temps total pour 10 000 itérations : " + (end - start) + " ms");
	}

	@Test
	void testCacheFonctionnel() {
		// Créer une instance de test
		PaysEuropeen pays = new PaysEuropeen(true);
		Informations infos = new Informations(pays);

		// Préparer le chemin imbriqué
		String chemin = "pays.isSpeakFrench";

		// Accéder au champ une première fois
		Object valeur1 = ReflectionUtils.getValeurAvecChemin(infos, chemin.split("\\."));

		// Accéder au champ une deuxième fois (doit utiliser le cache)
		Object valeur2 = ReflectionUtils.getValeurAvecChemin(infos, chemin.split("\\."));

		// Vérifier que les deux valeurs sont identiques
		assertEquals(valeur1, valeur2);
	}
}
