
#ifndef parse_hpp
#include "expr.h"
#define parse_hpp

#include <stdio.h>
#include <sstream>

#endif


Expr *parse_expr(std::istream &in);

Expr *parse_num(std::istream &in);

Expr *parse_addend(std::istream &in);

Expr *parse_multicand(std::istream &in);

Expr* parse_str(std::string str);

static void consume(std::istream &inn, char expected);

static void skip_whitespace(std::istream &inn);

std::string parse_keyword(std::istream &in);

Expr *parse_let(std::istream &inn);

Expr *parse_var(std::istream &inn);

Expr *parse_if(std::istream &inn);