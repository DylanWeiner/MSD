#include <iostream>

class Expr {
public:
    virtual bool equals(Expr *e) = 0;
    virtual int interp() = 0;
    virtual bool has_variable() = 0;
    virtual Expr* subst(std::string name, Expr* newVal) = 0;
};

class Num : public Expr {
public:
    int val;
    Num(int val);
    bool equals(Expr *e) override;
    int interp() override;
    bool has_variable() override;
    Expr* subst(std::string name, Expr* newVal) override;
};

class VarExpr : public Expr {
public:
    std::string val;
    VarExpr(std::string val);
    bool equals(Expr *e) override;
    int interp() override;
    bool has_variable() override;
    Expr* subst(std::string name, Expr* newVal) override;
};

class Add : public Expr {
public:
    Expr *lhs;
    Expr *rhs;
    Add(Expr *lhs, Expr *rhs);
    bool equals(Expr *e) override;
    int interp() override;
    bool has_variable() override;
    Expr* subst(std::string name, Expr* newVal) override;
};

class Mult : public Expr {
public:
    Expr *lhs;
    Expr *rhs;
    Mult(Expr *lhs, Expr *rhs);
    bool equals(Expr *e) override;
    int interp() override;
    bool has_variable() override;
    Expr* subst(std::string name, Expr* newVal) override;
};