
#ifndef parse_hpp
#include "expr.h"
#define parse_hpp

#include <stdio.h>

#endif


Expr *parse_expr(std::istream &in);

Expr *parse_num(std::istream &in);

Expr *parse_addend(std::istream &in);

Expr *parse_multicand(std::istream &in);

