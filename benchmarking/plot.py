import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

df = pd.read_csv("results.csv")

# Convert time from ms to seconds to match the paper's y-axis
df["time_sec"] = df["time_ms"] / 1000.0

n_values = sorted(df["n"].unique())
algorithms = df["algorithm"].unique()

# One figure per n value, matching the paper's layout
for n in n_values:
    subset = df[df["n"] == n]
    k_values = sorted(subset["k"].unique())
    k_labels = [str(k) for k in k_values]

    fig, ax = plt.subplots(figsize=(10, 6))

    x = np.arange(len(k_values))
    width = 0.8 / len(algorithms)

    for i, algo in enumerate(algorithms):
        algo_data = subset[subset["algorithm"] == algo]
        times = [algo_data[algo_data["k"] == k]["time_sec"].values[0]
                 if k in algo_data["k"].values else 0
                 for k in k_values]
        ax.bar(x + i * width, times, width, label=algo)

    ax.set_xlabel("Value of maximum element (k)")
    ax.set_ylabel("Time elapsed (sec)")
    ax.set_title(f"Time Complexity Comparison for N={n}")
    ax.set_xticks(x + width * (len(algorithms) - 1) / 2)
    ax.set_xticklabels(k_labels, rotation=45, ha="right")
    ax.legend()
    ax.grid(True, axis="y", alpha=0.3)

    plt.tight_layout()
    plt.savefig(f"barcharts/results_n{n}.png", dpi=150)
    print(f"Saved results_n{n}.png")

    # Line chart excluding CountingSort (like paper Figure 4)
    # so the scaling differences between other algorithms are visible
    line_algos = [a for a in algorithms if a != "CountingSort"]
    fig2, ax2 = plt.subplots(figsize=(10, 6))

    for algo in line_algos:
        algo_data = subset[subset["algorithm"] == algo].sort_values("k")
        if algo_data.empty:
            continue

        ax2.plot(
            range(len(algo_data)),
            algo_data["time_ms"].to_numpy(),
            marker="o",
            linewidth=2,
            label=algo,
        )

    ax2.set_xlabel("Value of maximum element (k)")
    ax2.set_ylabel("Time elapsed (msec)")
    ax2.set_title(f"Time Complexity Comparison for N={n}")
    ax2.set_xticks(range(len(k_values)))
    ax2.set_xticklabels([str(k) for k in k_values], rotation=45, ha="right")
    ax2.grid(True, alpha=0.3)
    ax2.legend()

    plt.tight_layout()
    plt.savefig(f"linecharts/results_line_n{n}.png", dpi=150)
    print(f"Saved results_line_n{n}.png")

plt.show()
