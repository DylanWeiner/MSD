import matplotlib.pyplot as plt

def main():
    filename = "data.csv" #change me if desired
    with open(filename, 'r') as f:
        lines = f.readlines()
        headers = lines[0].split(",")
        data = {h: [] for h in headers}
        for line in lines[1:]:
            cols = line.split(",")
            for i, col in enumerate(cols):
                data[headers[i]].append(float(col))

        print(data)


        plt.figure(1)
        # Assumes X axis is column 1, plots all other columns on the same plot
        for h in headers[1:]:
            plt.plot(data[headers[0]], data[h])

        plt.xlabel(headers[0])
        plt.ylabel("Time(ns)") #change me
        plt.title("Amount of Time Searching for a Value Contained in an Array of Size N") #change me
        plt.legend(headers[1:]) # label the lines
        plt.savefig('Value_Contained.png') #change me

        plt.show()
        plt.close()



if __name__ == '__main__':
    main()
