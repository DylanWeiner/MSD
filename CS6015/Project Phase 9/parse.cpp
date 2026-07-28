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
        return new AddExpr(e, rhs);
    } else if(c == '=') {
        consume(inn, '=');
        if(inn.peek() == '=') {
            consume(inn, '=');
        }
        skip_whitespace(inn);
        Expr *rhs = parse_expr(inn);
        return new EqExpr(e, rhs);
    }
     else
        return e;
}

Expr *parse_num(std::istream &inn) {
    int n = 0;

    skip_whitespace(inn);
    bool isNeg = false;
    int c = inn.peek();

    if(c == '-') {
            consume(inn, '-');
            isNeg = true;
        }  // if number is negative
    
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

    if (isNeg) {
        n *= -1;
    }
    
    return new NumExpr(n);
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
        return new MultExpr(e, rhs);
    } else {
        return e;
    }
}

Expr *parse_multicand(std::istream &inn) {
    skip_whitespace(inn);

	int c = inn.peek();
	if (c == '-' || isdigit(c))
		return parse_num(inn);
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
	} else if(isalpha(c)) {
        return parse_var(inn);
    }
    else if(c == '_') {
        consume(inn, '_');

        std::string keyword = parse_keyword(inn);
        if(keyword == "let") {
            return parse_let(inn);
        }
        else if(keyword == "true") {
            return new BoolExpr(true);
        }
        else if(keyword == "false") {
            return new BoolExpr(false);
        }
        else if(keyword == "if") {
            return parse_if(inn);
        }
        else {
            throw std::runtime_error("invalid input keyword");
        }
    }
    else {
		consume(inn, c);
		throw std::runtime_error("invalid input multicand");
	} 
}

Expr *parse_var(std::istream &in){
    std::string variable = "";
    while (1) {
        int c = in.peek();
        if (isalpha(c)) {
          consume(in, c);
          variable += static_cast<char>(c); 
        }
        else if (isdigit(c) || c == '_' || c == '-'){
            throw std::runtime_error("invalid input");
        } else
          break;
      }
      if (variable.empty()){
        throw std::runtime_error("invalid input");
      }
    return new VarExpr(variable);
}

std::string parse_keyword(std::istream &inn) {
    std::string keyword = "";
    while (1) {
        int c = inn.peek();
        if (isalpha(c)) {
          consume(inn, c);
          keyword += static_cast<char>(c); 
        } else
          break;
      }
      
      return keyword;
}

Expr *parse_let(std::istream &inn) {
    std::string let_keyword = parse_keyword(inn);
    int c = 0;
    std::string oldVal = "";
    Expr * rhs;
    Expr * body;

    skip_whitespace(inn);
    c = inn.peek();
    if(!isalpha(c)) {
        throw std::runtime_error("incorrect _let variable");
    }
    
    oldVal = static_cast<char>(c);

    consume(inn, c);

    skip_whitespace(inn);
    c = inn.peek();

    if(c != '=') {
        throw std::runtime_error("incorrect _let solution");
    }

    consume(inn, c);
    skip_whitespace(inn);
    rhs = parse_expr(inn);
    skip_whitespace(inn);

    c = inn.peek();
    if(c != '_') {
        throw std::runtime_error("incorrect _let followup");
    }

    consume(inn, '_');
    let_keyword = parse_keyword(inn);
    if(let_keyword != "in") {
        throw std::runtime_error("incorrect _in call");
    }
    skip_whitespace(inn);

    body = parse_expr(inn);
    skip_whitespace(inn);
    return new LetExpr(oldVal, rhs, body);
}

Expr *parse_if(std::istream &inn) {
    std::string if_keyword = "";
    int c = 0;
    Expr * cond;
    Expr * lhs;
    Expr * rhs;

    skip_whitespace(inn);
    cond = new EqExpr(parse_expr(inn), new BoolExpr(true));

    skip_whitespace(inn);
    c = inn.peek();
    consume(inn, '_');

    if_keyword = parse_keyword(inn);
    skip_whitespace(inn);
    lhs = parse_expr(inn);
    skip_whitespace(inn);

    if(c != '_' || if_keyword != "then") {
        throw std::runtime_error("incorrect _then statement");
    }

    skip_whitespace(inn);
    c = inn.peek();
    consume(inn, '_');

    if_keyword = parse_keyword(inn);
    if(c != '_' || if_keyword != "else") {
        throw std::runtime_error("incorrect _else statement");
    }

    skip_whitespace(inn);
    rhs = parse_expr(inn);
    skip_whitespace(inn);
    return new IfExpr(cond, lhs, rhs);
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
