
#ifndef parse_hpp
#include "expr.h"
#include "pointer.h"
#define parse_hpp

#include <stdio.h>
#include <sstream>

#endif


PTR(Expr) parse_expr(std::istream &in);

PTR(Expr) parse_num(std::istream &in);

PTR(Expr) parse_addend(std::istream &in);

PTR(Expr) parse_multicand(std::istream &in);

PTR(Expr) parse_str(std::string str);

static void consume(std::istream &inn, char expected);

static void skip_whitespace(std::istream &inn);

std::string parse_keyword(std::istream &in);

PTR(Expr) parse_let(std::istream &inn);

PTR(Expr) parse_var(std::istream &inn);

PTR(Expr) parse_if(std::istream &inn);

PTR(Expr) parse_fun(std::istream &inn);

PTR(Expr) parse_inner(std::istream &inn);