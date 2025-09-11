//
//  Fraction.h
//  FractionClass
//
//  Created by Dylan Weiner on 9/9/25.
//

#ifndef FRACTION_H

#define FRACTION_H

#include <iostream>

#include <string>

#include <cmath>

#include <cassert>

using namespace std;

class Fraction {
private:
    long n;
    
    long d;
    
    void reduce();
    
    long GCD();
    
public:
    Fraction();
    
    Fraction(long n, long d);
    
    Fraction operator+(const Fraction & rhs);
    
    Fraction operator+=(const Fraction & rhs);
    
    Fraction operator-(const Fraction & rhs);
    
    Fraction operator-=(const Fraction & rhs);
    
    Fraction operator*(const Fraction & rhs);
    
    Fraction operator*=(const Fraction & rhs);
    
    Fraction operator/(const Fraction & rhs);
    
    Fraction operator/=(const Fraction & rhs);
    
    bool operator==(const Fraction & rhs);
    
    bool operator!=(const Fraction & rhs);
    
    bool operator<(const Fraction & rhs);
    
    bool operator<=(const Fraction & rhs);
    
    bool operator>(const Fraction & rhs);
    
    bool operator>=(const Fraction & rhs);
    
    Fraction plus(Fraction rhs);
    
    Fraction minus(Fraction rhs);
    
    Fraction times(Fraction rhs);
    
    Fraction dividedBy(Fraction rhs);
    
    string toString()const;
    
    double toDouble();
    
    Fraction reciprocal();
};

#endif
