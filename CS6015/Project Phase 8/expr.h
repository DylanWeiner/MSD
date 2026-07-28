#include <string>
#include <sstream>
#include <iostream>
// #include <fstream>

class Val;

typedef enum {
  prec_none,      // = 0
  prec_add,       // = 1
  prec_mult,       // = 2
  prec_start
} precedence_t;

class Expr {
public:
    virtual bool equals(Expr *e) = 0;
    virtual Val* interp() = 0;
    virtual bool has_variable() = 0;
    virtual Expr* subst(std::string name, Expr* newVal) = 0;
    virtual void printExp(std::ostream &ot) = 0;
    std::string to_string_p();
    void pretty_print(std::ostream &ot);
    virtual void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) = 0;
    std::string to_pretty_string();
};

class NumExpr : public Expr {
public:
    int val;
    NumExpr(int val);
    bool equals(Expr *e) override;
    Val* interp() override;
    bool has_variable() override;
    Expr* subst(std::string name, Expr* newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;
};

class VarExpr : public Expr {
public:
    std::string val;
    VarExpr(std::string val);
    bool equals(Expr *e) override;
    Val* interp() override;
    bool has_variable() override;
    Expr* subst(std::string name, Expr* newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;

};

class AddExpr : public Expr {
public:
    Expr *lhs;
    Expr *rhs;
    AddExpr(Expr *lhs, Expr *rhs);
    bool equals(Expr *e) override;
    Val* interp() override;
    bool has_variable() override;
    Expr* subst(std::string name, Expr* newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;
};

class MultExpr : public Expr {
public:
    Expr *lhs;
    Expr *rhs;
    MultExpr(Expr *lhs, Expr *rhs);
    bool equals(Expr *e) override;
    Val* interp() override;
    bool has_variable() override;
    Expr* subst(std::string name, Expr* newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;
};

class LetExpr : public Expr {
public:
    std::string var;
    Expr *rhs;
    Expr *body;
    LetExpr(std::string var, Expr *rhs, Expr *body);
    bool equals(Expr *e) override;
    Val* interp() override;
    bool has_variable() override;
    Expr* subst(std::string name, Expr* newVal) override;
    void printExp(std::ostream &ot) override;
    void pretty_print_at(std::ostream &ot, precedence_t prec, std::streampos& pos, bool paren) override;
};