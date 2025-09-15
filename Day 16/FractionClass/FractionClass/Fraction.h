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
public:
    Fraction();
    Fraction(long n, long d);
    
    Fraction operator+(const Fraction & rhs)const;
    Fraction operator-(const Fraction & rhs)const;
    Fraction operator*(const Fraction & rhs)const;
    Fraction operator/(const Fraction & rhs)const;
    
    Fraction operator+=(const Fraction & rhs);
    Fraction operator-=(const Fraction & rhs);
    Fraction operator*=(const Fraction & rhs);
    Fraction operator/=(const Fraction & rhs);
    
    bool operator==(const Fraction & rhs);
    bool operator!=(const Fraction & rhs);
    bool operator<(const Fraction & rhs);
    bool operator<=(const Fraction & rhs);
    bool operator>(const Fraction & rhs);
    bool operator>=(const Fraction & rhs);
    
    Fraction plus(Fraction rhs)const;
    Fraction minus(Fraction rhs)const;
    Fraction times(Fraction rhs)const;
    Fraction dividedBy(Fraction rhs)const;
    
    string toString()const;
    double toDouble();
    Fraction reciprocal();
    void normalizeSign();
    
private:
    long n;
    long d;
    
    void reduce();
    long GCD();
};

#endif
