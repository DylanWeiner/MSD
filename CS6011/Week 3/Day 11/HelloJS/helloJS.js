function main() {
    console.log("Hello World!");
    document.writeln("<p> Hello World! </p>");

    x = { "name": "Dylan", gpa: 3.7, year: 5 };

    myArray = ["Hey!", true, 25, 163.4, x];

    console.log(myArray);

    myArray = ["Yo", false, 26, 170.2, x];

    console.log(myArray);

    function s(a, b) {
        return a + b;
    };

    let sum = function(a, b) {
        return a + b
    };
};

window.onload = main;