//
//  Fraction.cpp
//  FractionClass
//
//  Created by Dylan Weiner on 9/9/25.
//

#include "Fraction.h"

Fraction::Fraction(){
    n = 0;
    d = 1;
}

long Fraction::GCD() {
    long gcd = abs(n);
    long remainder = abs(d);
    while(remainder != 0) {
        long temp = remainder;
        remainder = gcd % remainder;
        gcd = temp;
    }
    return gcd;
}

void Fraction::reduce() {
    long val = GCD();
    n = n/val;
    d = d/val;
}

Fraction::Fraction(long numerator, long denominator) {
    n = numerator;
    d = denominator;
    
    if(d <0 || (d < 0 && n <0)) {
        n = n * -1;
        d = d * -1;
    }
    assert(d != 0);
    reduce();
}

Fraction Fraction::operator+(const Fraction & rhs) {
    Fraction temp;
    
    temp.n = ((*this).n * rhs.d) + (rhs.n * (*this).d);
    temp.d = (*this).d * rhs.d;
    temp.reduce();
    return temp;
}

Fraction Fraction::plus(const Fraction rhs) {
    return (*this) + rhs;
}

Fraction Fraction::operator-(const Fraction & rhs) {
    Fraction temp;
    
    temp.n = (this->n * rhs.d) - (rhs.n * this->d);
    temp.d = (*this).d * rhs.d;
    temp.reduce();
    return temp;
}

Fraction Fraction::minus(const Fraction rhs) {
    return (*this) - rhs;
}

Fraction Fraction::operator*(const Fraction & rhs) {
    Fraction temp;
    
    temp.n = (*this).n * rhs.n;
    temp.d = (*this).d * rhs.d;
    temp.reduce();
    return temp;
}

Fraction Fraction::times(const Fraction rhs) {
    return (*this) * rhs;
}

Fraction Fraction::operator/(const Fraction & rhs) {
    Fraction temp;
    
    temp.n = (*this).n * rhs.d;
    temp.d = (*this).d * rhs.n;
    temp.reduce();
    return temp;
}

Fraction Fraction::dividedBy(const Fraction rhs) {
    return (*this) / rhs;
}



string Fraction::toString()const {
    return to_string(n) + "/" + to_string(d);
}

double Fraction::toDouble() {
    double num = n;
    double den = d;
    
    return num/den;
}

Fraction Fraction::reciprocal() {
    Fraction temp;
    temp.n = d;
    temp.d = n;
    
    if(temp.d <0) {
        temp.n *= -1;
        temp.d *= -1;
    }
    
    temp.reduce();
    return temp;
}

Fraction Fraction::operator+=(const Fraction & rhs) {
    (*this) = (*this) + rhs;
    return (*this);
}



Fraction Fraction::operator-=(const Fraction & rhs) {
    (*this) = (*this) - rhs;
    return (*this);
}

Fraction Fraction::operator*=(const Fraction & rhs) {
    (*this) = (*this) * rhs;
    return (*this);
}


Fraction Fraction::operator/=(const Fraction & rhs) {
    (*this) = (*this) / rhs;
    return (*this);
}

bool Fraction::operator==(const Fraction & rhs) {
    if((*this).n == rhs.n && (*this).d == rhs.d) {
        return true;
    }
    return false;
}

bool Fraction::operator!=(const Fraction & rhs) {
    if((*this) == rhs) {
        return false;
    }
    return true;
}

bool Fraction::operator<(const Fraction & rhs) {
    if((*this).n > rhs.n && (*this).d > rhs.d) {
        return false;
    }
    return true;
}

bool Fraction::operator<=(const Fraction & rhs) {
    if((*this) < rhs || (*this) == rhs) {
        return true;
    }
    return false;
}

bool Fraction::operator>(const Fraction & rhs) {
    if((*this) < rhs) {
        return false;
    }
    return true;
}

bool Fraction::operator>=(const Fraction & rhs) {
    if((*this) > rhs || (*this) == rhs) {
        return true;
    }
    return false;
}
