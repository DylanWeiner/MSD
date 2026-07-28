//
//  main.cpp
//  DateFormats
//
//  Created by Dylan Weiner on 8/21/25.
//

#include <iostream>
#include <string>

int main(int argc, const char * argv[]) {
    
    std::string date;
    std::string m;
    std::string month;
    std::string day;
    std::string y;
    
    std::cout << "Please input a date in (mm/dd/yyyy) format. ";
    std::cin >> date;
    
    //int pos = date.find("/");
    
    m = date.substr(0, 2);
    day = date.substr(3, 2);
    y = date.substr(6, 9);
    
    int d = std::stoi(day);
    int year = std::stoi(y);
    
    if(d <= 1) {
        std::cout << "Not a valid date. ::(";
    }
    else if(year <= 0) {
        std::cout << "Not a valid date. ::(";
    }
    else if(m == "01" && d <= 31) {
        month = "January";
    }
    else if(m == "02" && d <= 28) {
        month = "February";
    }
    else if(m == "03" && d <= 31) {
        month = "March";
    }
    else if(m == "04" && d <= 30) {
        month = "April";
    }
    else if(m == "05" && d <= 31) {
        month = "May";
    }
    else if(m == "06" && d <= 30) {
        month = "June";
    }
    else if(m == "07" && d <= 31) {
        month = "July";
    }
    else if(m == "08" && d <= 31) {
        month = "August";
    }
    else if(m == "09" && d <= 30) {
        month = "September";
    }
    else if(m == "10" && d <= 31) {
        month = "October";
    }
    else if(m == "11" && d <= 30) {
        month = "November";
    }
    else if(m == "12" && d <= 31) {
        month = "December";
    }
    else {
        std::cout << "Not a valid date. ::(";
    }
    
    std::cout << "That date is: " << month << " " << day << ", " << y << "\n";
    
    return 0;
}
