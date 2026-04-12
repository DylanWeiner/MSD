#include <iostream>
#include "parse.hpp"



PTR(Expr) parse_expr(std::istream &inn) {
    PTR(Expr) e;
    e = parse_addend(inn);
    skip_whitespace(inn);
    int c = inn.peek();
    

    if (c == '+') {
        consume(inn, '+');
        PTR(Expr) rhs = parse_expr(inn);
        return NEW(AddExpr)(e, rhs);
    } else if(c == '=') {
        consume(inn, '=');
        if(inn.peek() == '=') {
            consume(inn, '=');
        }
        skip_whitespace(inn);
        PTR(Expr) rhs = parse_expr(inn);
        return NEW(EqExpr)(e, rhs);
    }
     else
        return e;
}

PTR(Expr) parse_num(std::istream &inn) {
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
    
    return NEW(NumExpr)(n);
}

PTR(Expr) parse_addend(std::istream &inn) {
    PTR(Expr) e;
    e = parse_multicand(inn);
    skip_whitespace(inn);
    // int n = 0;

    int c = inn.peek();

    if( c == '*') {
        consume(inn, '*');
        PTR(Expr) rhs = parse_addend(inn);
        return NEW(MultExpr)(e, rhs);
    } else {
        return e;
    }
}

PTR(Expr) parse_multicand(std::istream &inn) {
    PTR(Expr) e = parse_inner(inn);
    while (inn.peek() == '(') {
        consume(inn, '(');
        PTR(Expr) actual_arg = parse_expr(inn);
        consume(inn, ')');
        e = NEW(CallExpr)(e, actual_arg);
    }
    return e;
}

PTR(Expr) parse_inner(std::istream &inn) {
    skip_whitespace(inn);

	int c = inn.peek();
    // std::cout << "Top c: " << static_cast<char>(c) << std::endl;
	if (c == '-' || isdigit(c))
		return parse_num(inn);
     // else if c == '_', parse the keyword _let.
    else if (c == '(') {
		consume(inn, '(');
		PTR(Expr) e = parse_expr(inn);
		skip_whitespace(inn);
		c = inn.get();
		if (c != ')') {
		    throw std::runtime_error("missing close parenthesis");
        }
		return e;
	} else if(isalpha(c)) {
        // std::cout << "Parsing variable: " << static_cast<char>(c) << std::endl;
        return parse_var(inn);
    }
    else if(c == '_') {
        consume(inn, '_');

        std::string keyword = parse_keyword(inn);
        // std::cout << "keyword: " << keyword << std::endl;
        if(keyword == "let") {
            return parse_let(inn);
        } else if(keyword == "true") {
            return NEW(BoolExpr)(true);
        } else if(keyword == "false") {
            return NEW(BoolExpr)(false);
        } else if(keyword == "if") {
            return parse_if(inn);
        } else if(keyword == "fun") {
            return parse_fun(inn);
        } else {
            throw std::runtime_error("invalid input keyword");
        }
    }
    else {
        consume(inn, c);
        throw std::runtime_error("invalid input multicand");
    }
}

PTR(Expr) parse_var(std::istream &inn){
    std::string variable = "";
    while (1) {
        int c = inn.peek();
        if (isalpha(c)) {
            variable = parse_keyword(inn);
            // consume(inn, c);
            // variable += static_cast<char>(c); 
        } else if (isdigit(c) || c == '_' || c == '-'){
            throw std::runtime_error("invalid input: " + std::string(1, c));
        } else
          break;
      }
      if (variable.empty()){
        throw std::runtime_error("empty variable");
      }
    return NEW(VarExpr)(variable);
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

PTR(Expr) parse_let(std::istream &inn) {
    std::string let_keyword = parse_keyword(inn);
    int c = 0;
    std::string oldVal = "";
    PTR(Expr) rhs;
    PTR(Expr) body;

    skip_whitespace(inn);

    c = inn.peek();
    if(!isalpha(c)) {
        throw std::runtime_error("incorrect _let variable");
    }

    oldVal = parse_keyword(inn);

    // std::cout << "keyword: " << oldVal << std::endl;
    skip_whitespace(inn);
    c = inn.peek();

    if(c != '=') {
        throw std::runtime_error("incorrect _let = sign");
    }
    
    consume(inn, c);
    skip_whitespace(inn);
    c = inn.peek();

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
    return NEW(LetExpr)(oldVal, rhs, body);
}

PTR(Expr) parse_if(std::istream &inn) {
    std::string if_keyword = "";
    int c = 0;
    PTR(Expr) cond;
    PTR(Expr) lhs;
    PTR(Expr) rhs;

    skip_whitespace(inn);
    cond = parse_expr(inn);

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

    return NEW(IfExpr)(cond, lhs, rhs);
}

PTR(Expr) parse_fun(std::istream &inn) {
    int c = 0;
    std::string arg = "";
    PTR(Expr) body;

    skip_whitespace(inn);
    c = inn.peek();
    if(c != '(') {
        throw std::runtime_error("incorrect _fun start scope");
    }
    consume(inn, '(');
    skip_whitespace(inn);

    arg = parse_keyword(inn);
    // std::cout << arg << std::endl;
    skip_whitespace(inn);
    c = inn.peek();

    if(c != ')') {
        throw std::runtime_error("incorrect _fun end scope");
    }

    consume(inn, ')');
    skip_whitespace(inn);

    body = parse_expr(inn);

    return NEW(FunExpr)(arg, body);
}

PTR(Expr) parse_str(std::string str) {
    std::stringstream st(str);
    return(parse_expr(st));
}

static void consume(std::istream &inn, char expected) {
    int c = inn.get();
    if(c != expected) {
        throw std::runtime_error("consume mismatch: " + std::string(1, c));
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
