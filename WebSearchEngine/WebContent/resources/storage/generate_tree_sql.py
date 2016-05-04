f = open('/Users/ryancsmith/Downloads/new.txt', 'w')
letters = "abcdefghijklmnopqrstuvwx"
counter = 0
for i in range(1,13):
	for j in range (0,2):
		f.write("insert into ii_table values('" + str(letters[counter]) + "', " + str(i) + ");\n")
		counter += 1

# counter = 1
# for i in "abcdefghijklmnopqrstuvwx":
# 	f.write("insert into word_table values (" + str(counter) + ", '" + str(i)+ "'" + ");\n")
# 	counter += 1

# counter = 1
# letter_counter = 0
# letters = "abcdefghijklmnopqrstuvwx"
# for i in range(1,13):
# 	f.write("insert into node_table values (" + str(i) + ", '" + str(letters[letter_counter]) + "', '" + str(letters[letter_counter + 1]) + "', " + "1);\n")
# 	letter_counter += 2

# for i in range(0,11):
# 	f.write("insert into edge_table values (\n")

f.close