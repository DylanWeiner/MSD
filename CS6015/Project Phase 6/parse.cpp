#include <iostream>
#include "parse.hpp"



Expr *parse_expr(std::istream &inn) {
    Expr *e;
    e = parse_addend(inn);
    skip_whitespace(inn);
    int c = inn.peek();

    if (c == '+') {
        consume(inn, '+');
        Expr *rhs = parse_expr(inn);
        return new Add(e, rhs);
    }
     else
        return e;
}

Expr *parse_num(std::istream &inn) {
    int n = 0;

    skip_whitespace(inn);
    
    while (1) {
        int c = inn.peek();

        if (isdigit(c))
        {
            consume(inn, c);
            n = n*10 + (c - '0');
        }
        else
            break;
    }
    
    return new Num(n);
}

Expr *parse_addend(std::istream &inn) {
    Expr *e;
    e = parse_multicand(inn);
    skip_whitespace(inn);
    // int n = 0;

    int c = inn.peek();

    if( c == '*') {
        consume(inn, '*');
        Expr *rhs = parse_addend(inn);
        return new Mult(e, rhs);
    } else {
        return e;
    }
}

Expr *parse_multicand(std::istream &inn) {
    skip_whitespace(inn);

	int c = inn.peek();
	if ((c == '-') || isdigit(c))
		return parse_num(inn); // if number is negative
     // else if(isalpha(c))
     // else if c == '_', parse the keyword _let.
    else if (c == '(') {
		consume(inn, '(');
		Expr *e = parse_expr(inn);
		skip_whitespace(inn);
		c = inn.get();
		if (c != ')') {
		    throw std::runtime_error("missing close parenthesis");
        }
		return e;
	} else {
		consume(inn, c);
		throw std::runtime_error("invalid input multicand");
	} 
}

bool isAlpha(int c) {
    // if
}

Expr* parse_str(std::string str) {
    std::stringstream st(str);
    return(parse_expr(st));
}

static void consume(std::istream &inn, char expected) {
    int c = inn.get();
    if(c != expected) {
        throw std::runtime_error("consume mismatch");
    }
}

static void skip_whitespace(std::istream &inn) {
    while(1) {
        if(inn.peek() == ' ') {
            inn.get();
        }
        else {
            break;
        }
    }
}
