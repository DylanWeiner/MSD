import matplotlib.pyplot as plt

def load_csv(filename):
    with open(filename, 'r') as f:
        lines = f.readlines()

    headers = lines[0].strip().split(",")
    data = {h: [] for h in headers}

    for line in lines[1:]:
        cols = line.strip().split(",")
        for i, col in enumerate(cols):
            data[headers[i]].append(float(col))

    return data


def main():
    files = {
        "Collisions": "Collision.csv",
    }

    plt.figure(figsize=(10,6))

    for label, filename in files.items():
        data = load_csv(filename)
        plt.plot(
            data["N"],
            data[" Optimized"],
            marker='o',
            label="Optimized Collisions",
        )
        plt.plot(
            data["N"],
            data[" Naive"],
            marker='o',
            label="Naive Collisions",
        )


    plt.xlabel("N (input size)")
    plt.ylabel("Time (ns)")
    plt.yscale('log')
    plt.title("Naive vs Optimized Collisions")

    plt.legend()
    plt.grid(True)

    plt.savefig("Naive vs Optimized Collisions.png", dpi = 150)
    plt.show()
    plt.close()


if __name__ == "__main__":
    main()