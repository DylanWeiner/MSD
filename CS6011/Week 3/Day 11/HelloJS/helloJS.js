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
    } //I prefer the function as it feels more dynamic and versatile

    let sum = function(a, b) {
        return a + b
    };

    console.log(s(5, 12) + " " + s(7.2,4.7) + " " + s("I'm ","Dying") + " " + s(7.3, " Erm, Acshually"));
    //They aren't assigned data types so they automatically change to whichever data type fits best and prevents errors.

}

window.onload = main;