import matplotlib.pyplot as plt

file_path = "PingPackets.txt" # Saves filenames recorded values.

def contains_time(main_string):
    return main_string.count("time=") == 1 # looks for exactly one value of a specified char in a string.


try:
    with open(file_path, 'r') as file: # Reads files for recorded values.
        all_pings = 0
        with open(f'PingData.txt', 'w') as address_list:
            address_list.write("") # Resets each address list upon each run of the program.
        line_count = 0 # counts the line
        for line in file:
            delay_count = 0
            lines_avg = 0
            line_count += 1
            content = line.strip() # Strips formatting of each line
            words = content.split() # Splits each line into individual words
            for word in words:
                if contains_time(word) is True: # Looks for values with "time=" in the words.
                    delay_count = delay_count + 1 # Counts number of delay values per line.
                    word = word.replace("time=", "")
                    lines_avg = float(word)
                    all_pings += lines_avg
            if lines_avg == 0:
                line_count -= 1
            else:
                with open(f'PingData.txt', 'a') as address_list:
                    address_list.write(f"Ping Delay #{line_count}: {lines_avg} ms\n") # Prints each ping delay.
    with open(f'PingData.txt', 'a') as address_list:
        pings_avg = all_pings/line_count        
        address_list.write(f"Average Delay: {pings_avg} ms\n") # Prints each ping delay.
except FileNotFoundError:
    print("Error, the file in this file path was not found!")