#pragma once
#include "pointer.h"
#include <string>
#include "val.h"
#include "expr.h"

class Val;

CLASS(Env) {
public:
    virtual ~Env() { }
    virtual PTR(Val) lookup(std::string var_name) = 0;
    static PTR(Env) empty;
};

class EmptyEnv : public Env {
public:
    PTR(Val) lookup(std::string var_name);
};

class ExtendedEnv : public Env {
public:
    std::string lhs;
    PTR(Val) rhs_val;
    PTR(Env) env;
    ExtendedEnv(std::string name, PTR(Val) val, PTR(Env) rest);
    PTR(Val) lookup(std::string var_name);
};
