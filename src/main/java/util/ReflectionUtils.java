package util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {

	// Cache global pour stocker les Field par classe et nom de champ
	private static final Map<Class<?>, Map<String, Field>> FIELD_CACHE = new HashMap<>();

	/**
	 * Récupère un Field d'une classe en utilisant un cache pour éviter des appels coûteux.
	 *
	 * @param clazz        Classe dans laquelle chercher le champ.
	 * @param nomAttribut  Nom du champ à récupérer.
	 * @return Le Field correspondant ou null si le champ n'existe pas.
	 */
	private static Field getCachedField(Class<?> clazz, String nomAttribut) {
		return FIELD_CACHE
				.computeIfAbsent(clazz, c -> new HashMap<>()) // Initialise la Map pour la classe si elle n'existe pas
				.computeIfAbsent(nomAttribut, key -> {
					try {
						Field field = clazz.getDeclaredField(key); // Recherche du champ
						field.setAccessible(true); // Rendre le champ accessible
						return field;
					} catch (NoSuchFieldException e) {
						return null; // Retourne null si le champ n'existe pas
					}
				});
	}

	/**
	 * Navigue dynamiquement dans les objets en fonction d'un chemin imbriqué.
	 *
	 * @param objet          L'objet de départ.
	 * @param nomsAttributs  Tableau des noms de champs représentant le chemin.
	 * @return La valeur finale ou null si un champ intermédiaire est null ou inexistant.
	 */
	public static Object getValeurAvecChemin(Object objet, String[] nomsAttributs) {
		try {
			for (String nomAttribut : nomsAttributs) {
				if (objet == null) return null; // Arrêt si un objet intermédiaire est null
				Field field = getCachedField(objet.getClass(), nomAttribut); // Utilisation du cache
				if (field == null) return null; // Retourne null si le champ n'existe pas
				objet = field.get(objet); // Mise à jour de l'objet courant
			}
			return objet;
		} catch (IllegalAccessException e) {
			// Gestion simplifiée des exceptions liées à l'accès
			return null;
		}
	}
}
