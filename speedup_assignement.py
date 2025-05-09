import matplotlib.pyplot as plt
import re

# Chemin du fichier contenant les résultats
file_path = "result_assignement102.txt"

# Listes pour stocker les valeurs de speedup et de nombre de workers
speedup_values = []
nworkers_values = []
time_single_worker = None

# Lire le fichier et extraire les valeurs
with open(file_path, "r") as file:
    for line in file:
        # Chercher le temps pour 1 worker
        match_single = re.search(r"Temps 1 worker: ([0-9.,]+)", line)
        if match_single and time_single_worker is None:
            speedup_values.append(1.0)  # Speedup initial = 1 pour 1 worker
            nworkers_values.append(1)
            time_single_worker = float(match_single.group(1).replace(',', '.'))

        # Chercher les valeurs de speedup, nombre de workers et temps
        match = re.search(r"Speedup: ([0-9.,]+), Temps 1 worker: ([0-9.,]+), Temps avec (\d+) worker: ([0-9.,]+)", line)
        if match:
            speedup = float(match.group(1).replace(',', '.'))
            nworkers = int(match.group(3))
            speedup_values.append(speedup)
            nworkers_values.append(nworkers)

# Vérifier si des données ont été extraites
if not speedup_values or not nworkers_values:
    print("Aucune donnée valide trouvée dans le fichier.")
else:
    # Tracer le graphe
    plt.plot(nworkers_values, speedup_values, marker='o', linestyle='-', label="Speedup")

    # Ligne de référence idéale (Speedup parfait: y = x)
    max_workers = max(nworkers_values)
    plt.plot([1, max_workers], [1, max_workers], 'r--', label="Speedup parfait")

    # Labels et titre
    plt.xlabel("Nombre de processus")
    plt.ylabel("Speedup")
    plt.title("Scalabilité forte du programme Assignement102")
    plt.legend()
    plt.grid(True)
    plt.show()
