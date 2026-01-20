#include <iostream>

class Expr {
public:
    virtual bool equals(Expr *e) = 0;
};

// class VarExpr {
// public:
//     virtual bool equals(Expr *e) = 0;
// };

class Num : public Expr {
public:
    int val;
    Num(int val);
    bool equals(Expr *e) override;
};

class VarExpr : public Expr {
public:
    std::string val;
    VarExpr(std::string val);
    bool equals(Expr *e) override;
};

class Add : public Expr {
public:
    Expr *lhs;
    Expr *rhs;
    Add(Num *lhs, Num *rhs);
    bool equals(Expr *e) override;
};

class Mult : public Expr {
public:
    Expr *lhs;
    Expr *rhs;
    Mult(Expr *lhs, Expr *rhs);
    bool equals(Expr *e) override;
};