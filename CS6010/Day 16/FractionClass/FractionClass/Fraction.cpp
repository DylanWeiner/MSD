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
    n /= val;
    d /= val;
}

void Fraction::normalizeSign() {
    if(d < 0) {
        n *= -1;
        d *= -1;
    }
}

Fraction::Fraction(long numerator, long denominator) {
    assert(denominator != 0);
    n = numerator;
    d = denominator;
    normalizeSign();
    reduce();
}

Fraction Fraction::operator+(const Fraction & rhs)const {
    Fraction temp;
    
    temp.n = ((*this).n * rhs.d) + (rhs.n * (*this).d);
    temp.d = (*this).d * rhs.d;
    temp.reduce();
    return temp;
}

Fraction Fraction::plus(const Fraction rhs)const {
    return (*this) + rhs;
}

Fraction Fraction::operator-(const Fraction & rhs)const {
    Fraction temp;
    
    temp.n = (this->n * rhs.d) - (rhs.n * this->d);
    temp.d = (*this).d * rhs.d;
    temp.reduce();
    return temp;
}

Fraction Fraction::minus(const Fraction rhs)const {
    return (*this) - rhs;
}

Fraction Fraction::operator*(const Fraction & rhs)const {
    Fraction temp;
    
    temp.n = (*this).n * rhs.n;
    temp.d = (*this).d * rhs.d;
    temp.reduce();
    return temp;
}

Fraction Fraction::times(const Fraction rhs)const {
    return (*this) * rhs;
}

Fraction Fraction::operator/(const Fraction & rhs)const {
    Fraction temp;
    
    temp.n = (*this).n * rhs.d;
    temp.d = (*this).d * rhs.n;
    temp.reduce();
    return temp;
}

Fraction Fraction::dividedBy(const Fraction rhs)const {
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
    temp.normalizeSign();
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
    return ((*this).n == rhs.n && (*this).d == rhs.d);
}

bool Fraction::operator!=(const Fraction & rhs) {
    return !((*this) == rhs);
}

bool Fraction::operator<(const Fraction & rhs) {
    return !((*this).n > rhs.n && (*this).d > rhs.d);
}

bool Fraction::operator<=(const Fraction & rhs) {
    return ((*this) < rhs || (*this) == rhs);
}

bool Fraction::operator>(const Fraction & rhs) {
    return !((*this) < rhs);
}

bool Fraction::operator>=(const Fraction & rhs) {
    return((*this) > rhs || (*this) == rhs);
}
