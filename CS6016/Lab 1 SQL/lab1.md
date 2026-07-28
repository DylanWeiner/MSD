# Part 1:
## Q1:
Inventory [__SKU (integer)__, name (string), quantity (integer), price (money)]

## Q2:
Inventory [__SKU (integer)__, name (string), price (money)]
aisles [__SKU (integer)__, aisle (integer)]

## Q3:
Car [__VIN (string)__, make (string), model (string), year (integer), color (string)]
Dealer [__SSN (integer)__, name (string)]
Inventory [__SSN (integer), VIN (string)__]

# Part 2:
CREATE TABLE Patrons (
  UNIQUE PatronID INT PRIMARY KEY,
  Name (string),
  CardNum (integer)
)

CREATE TABLE CheckedOut (
  UNIQUE SerialNum INT PRIMARY KEY,
  CardNum (integer)
)

# Part 3:
Cars:
    Red Toyota Tacoma, 2008
    Green Toyota Tacoma, 1999
    White Telsa Model 3, 2018
    Blue Subaru WRX, 2016
    Red Ford F150, 2004

Salespeople:

    Arnold, trying to sell all Toyotas
    Hannah, trying to sell all red cars
    Steve, selling the Tesla

Cars:
VIN | make | model | year | color
000 | Toyota | Tacoma | 2008 | red
001 | Toyota | Tacoma | 1999 | green
002 | Tesla | Model 3 | 2018 | white
003 | Subaru | WRX | 2016 | blue
004 | Ford | F150 | 2004 | red

Dealer:
SSN | name
432-67-0000 | Arnold
432-67-0001 | Hannah
432-67-0002 | Steve

Inventory:
SSN | VIN
432-67-0000 | 000
432-67-0000 | 001
432-67-0001 | 000
432-67-0001 | 004
432-67-0002 | 002

I needed to create an SSN for each dealer and a VIN for each car. Without these it's impossible to assign a car to a dealer with the system in place in our tables. Conversely, the information like the color, or even the year, while not necessary, can be used as a tool to track which dealers are available for which tasks in the inventory.

# Part 4:
Attribute Sets 	Superkey? 	Proper Subsets 	Key?
{A1} 		                     {} 	
{A2} 			                 {}
{A3} 			                 {}
{A1, A2} 			           
{A1, A3} 			         {A1}, {A3}
{A2, A3} 		             {A2}, {A3} 	
{A1, A2, A3} 			    {A1, A2}, {A1, A3}

Attribute Set A1:
Superkey?: False
Proper Subsets: {}
Key?: False

Attribute Set A2:
Superkey?: False
Proper Subsets: {}
Key?: False

Attribute Set A3:
Superkey?: False
Proper Subsets: {}
Key?: False

Attribute Set A1, A2:
Superkey?: True
Proper Subsets: {A1}, {A2}
Key?: True

Attribute Set A1, A3:
Superkey?: False
Proper Subsets: {A1}, {A3}
Key?: False

Attribute Set A2, A3:
Superkey?: False
Proper Subsets: {A2}, {A3}
Key?: False

Attribute Set A1, A2, A3:
Superkey?: True
Proper Subsets: {A1}, {A2}, {A3}, {A1, A2}, {A1, A3}, {A2, A3}
Key?: False

# Part 5:
If {x} is a superkey, then any set containing x is also a superkey.
    True, if x allows the row to be uniquely identified then any set containing this unique identifier would fall into the category of a super key.
If {x} is a key, then any set containing x is also a key.
    False, a key is the most minimal set that makes a row unique. Any set that uses both {x} and any other attribute would be a super key but not a key.
If {x} is a key, then {x} is also a superkey.
    True, as stated before, since {x} allows the attribute set to be unique, anything containing {x} should also be a super key.
If {x, y, z} is a superkey, then one of {x}, {y}, or {z} must also be a superkey.
    False, {x}, {y}, or {z} might, alone, be repeated and needs one of the other, or both of the other attributes in order to be a super key.
If an entire schema consists of the set {x, y, z}, and if none of the proper subsets of {x, y, z} are keys, then {x, y, z} must be a key.
    True, a schema requires a key to be a proper schema and work appropriately. As a result, one of the attribute subsets needs to act as the key.