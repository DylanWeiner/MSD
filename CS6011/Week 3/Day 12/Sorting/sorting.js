let data = [7, 3, 25, -9, 5, 11];
let data2 = ["ddd", "zzz", "aaa", "mmm"];

let person1 = {
  firstName: "Dylan",
  lastName: "Weiner"
};

let person2 = {
  firstName: "Adam",
  lastName: "Sandler"
};

let person3 = {
  firstName: "Minecraft",
  lastName: "Steve"
};

let person4 = {
  firstName: "John",
  lastName: "Snow"
};

let person5 = {
  firstName: "John",
  lastName: "Cena"
};

let nameList = [person1, person2, person3, person4, person5];

function compareByLength(a, b) {
    return a.length < b.length;
}

function compareOrder(a, b) {
    return a < b;
}


function CompareTo(array, startPos, doComparison) {
    let smallestIndex = startPos;
    for(let pos = startPos + 1; pos < array.length; pos++) {
        if(array[pos] < array[smallestIndex]) {
            smallestIndex = pos;
        }
    return smallestIndex;
    }
}

//Finds smallest index by smallest size.
function findMinLocation(array, startPos) {
    let smallestIndex = startPos;
    for(let pos = startPos + 1; pos < array.length; pos++) {
        if(array[pos] < array[smallestIndex])
            smallestIndex = pos;
    }
    return smallestIndex;
}

// Selection sort for objects by size.
function selectionSort(array) {
    for(let pos = 0; pos < array.length-1; pos++) {
        let sI = findMinLocation(array, pos);
        if(sI !== pos) {
        let temp = array[pos];
        array[pos] = array[sI];
        array[sI] = temp;
    }
    }
    return array;
}

// Find smallest index by lastName field
function lastNameOrder(array, startPos) {
  let smallestIndex = startPos;
  for (let pos = startPos + 1; pos < array.length; pos++) {
    if (array[pos].lastName < array[smallestIndex].lastName) {
      smallestIndex = pos;
    }
  }
  return smallestIndex;
}

// Selection sort for objects by lastName
function lastNameSort(array) {
  for (let pos = 0; pos < array.length - 1; pos++) {
    let sI = lastNameOrder(array, pos);
    if (sI !== pos) [array[pos], array[sI]] = [array[sI], array[pos]];
  }
  return array;
}

selectionSort(data);
console.log("Sorted Index: " + data);

lastNameSort(nameList);
console.log("Sorted by last name:", nameList.map(p => `${p.firstName} ${p.lastName}`));