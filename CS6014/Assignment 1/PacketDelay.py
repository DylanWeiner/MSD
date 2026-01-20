import matplotlib.pyplot as plt

file_paths = ["Packet1.txt", "Packet2.txt"] # Saves filenames recorded values.

def contains_char_once_count(main_string, char_to_check):
    return main_string.count(char_to_check) == 1 # looks for exactly one value of a specified char in a string.

def contains_ip(main_string):
    return main_string.count('.') == 3 and main_string.count('(') == 1 & main_string.count(')') == 1 # looks for IP Addresses.


try:
    doc_count = 1
    for file_path in file_paths:
        with open(file_path, 'r') as file: # Reads files for recorded values.
            x_axis = [] 
            y_axis = [] # Creates holding place for array of x and y axes.
            with open(f'HopData{doc_count}.txt', 'w') as address_list:
                address_list.write("") # Resets each address list upon each run of the program.
            print(f"Recorded Packet File {doc_count}")
            line_count = 0 # counts the line
            for line in file:
                delay_count = 0
                lines_avg = 0
                line_count += 1
                content = line.strip() # Strips formatting of each line
                words = content.split() # Splits each line into individual words
                for word in words:
                    if contains_ip(word) is True:
                        ip_add = word
                        ip_add = word.replace("(", "")
                        ip_add = ip_add.replace(")", "")
                        x_axis.append(ip_add) # Adds each IP Address to the X axis.
                    if contains_char_once_count(word, '.') is True: # Looks for values with a single '.' in them
                        delay_count = delay_count + 1 # Counts number of delay values per line.
                        lines_avg += float(word)
                        lines_avg = lines_avg/delay_count # Takes the average
                if lines_avg == 0:
                    line_count -= 1 # Subtracts the line that is just 3 '*'
                if lines_avg > 0:
                    y_axis.append(lines_avg) # Add each delay time to the Y axis.
                    with open(f'HopData{doc_count}.txt', 'a') as address_list:
                            address_list.write(f"Hop: {line_count}\nIP Address - {ip_add}\nAverage Delay: {lines_avg} ms\n") # Prints each IP and average delay.
            zipped_axes = zip(x_axis, y_axis)
            sorted_axes = sorted(zipped_axes)
            sorted_x, sorted_y = zip(*sorted_axes)
            print(f"x:{sorted_x} y:{sorted_y}\n")

            plt.bar(x_axis, y_axis, width=doc_count-1.65, align='center', label=f'Delay Time at Each IP Test {doc_count}')
        
            plt.xlabel("IP Addresses")
            plt.ylabel("Delay Time (ms)")
            plt.title("Delay Time Across IP Addresses")
            doc_count += 1
    plt.legend()
    plt.xticks(rotation=45, ha='right') # ha='right' aligns the right edge of the text with the tick mark and tilts the x axis labels.
    plt.tight_layout() # Fixes Spacing Issues
    plt.savefig("DelaysvsIP.pdf")
except FileNotFoundError:
    print("Error, the file in this file path was not found!")

# Part B (3 points): Suppose one of the three traceroute delay values between the source and a given router hop turns out to be unusually high.
# What are two possible causes for this unusually high delay?
#
# One could be caused by a substantial number of packet losses that required extra attempts of delivering packets.
# Another could be caused by increased traffic intensity causing a spike in Queueing Delay.