import matplotlib.pyplot as plt
import re

# Chemin du fichier contenant les résultats
file_path = "result_pi.txt"

# Listes pour stocker les valeurs de speedup et de nombre de workers
speedup_values = []
nworkers_values = []
time_single_worker = None

# Lire le fichier et extraire les valeurs
with open(file_path, "r") as file:
    for line in file:
        match_single = re.search(r"Temps 1 worker: ([0-9.]+)", line)
        if match_single and time_single_worker is None:
            speedup = 1
            speedup_values.append(speedup)
            nworkers = 1
            nworkers_values.append(nworkers)
            time_single_worker = float(match_single.group(1))

        match = re.search(r"Speedup: ([0-9.]+), Temps 1 worker: [0-9.]+, Temps avec ([0-9]+) worker", line)
        if match:
            speedup = float(match.group(1))
            nworkers = int(match.group(2))
            speedup_values.append(speedup)
            nworkers_values.append(nworkers)

# Vérifier si des données ont été extraites
if not speedup_values:
    print("Aucune donnée valide trouvée dans le fichier.")
else:
    # Tracer le graphe
    plt.plot(nworkers_values, speedup_values, marker='o', linestyle='-', label="speedup")

    max_workers = max(nworkers_values) if nworkers_values else 1
    plt.plot([1, max_workers], [1, max_workers], 'r--', label="sp=p")


    plt.xlabel("Nombre de processus")
    plt.ylabel("SP")
    plt.title("Scalabilité faible du programme Pi")
    plt.grid()
    plt.show()