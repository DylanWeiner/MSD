#include <iostream>
#include <stdlib.h>
#include "exec.h"
#include <chrono>
#include <thread>

int depth_count = 0;

static std::string random_expr_string_add();
static std::string random_expr_string_mult();
static std::string random_expr_string_let_add();
static std::string random_expr_string_let_mult();

int main(int argc, char **argv) {
        const char * const interp1_argv[] = { argv[1], "--interp" };
        const char * const interp2_argv[] = {argv[2], "--interp" };

        const char * const print1_argv[] = { argv[1], "--print" };
        const char * const print2_argv[] = {argv[2], "--print" };

        const char * const pretty_print1_argv[] = { argv[1], "--pretty-print" };
        const char * const pretty_print2_argv[] = {argv[2], "--pretty-print" };

        for (int i = 0; i < 100; i++) {
            std::string in = random_expr_string_add();
            std::cout << "Trying "<< in << "\n";

            ExecResult interp1_result = exec_program(2, interp1_argv, in);
            ExecResult interp2_result = exec_program(2, interp2_argv, in);

            ExecResult print1_result = exec_program(2, print1_argv, in);
            ExecResult print2_result = exec_program(2, print2_argv, in);

            ExecResult pretty_print1_result = exec_program(2, pretty_print1_argv, in);
            ExecResult pretty_print2_result = exec_program(2, pretty_print2_argv, in);

            if (interp1_result.out != interp2_result.out) {
                throw std::runtime_error("different interp results");
                break;
            }
                        
            if (print1_result.out != print2_result.out) {
                throw std::runtime_error("different print results");
                break;
            }

            if (pretty_print1_result.out != pretty_print2_result.out) {
                throw std::runtime_error("different pretty-print results");
                break;
            }
        }

        for (int i = 0; i < 100; i++) {
            std::string in = random_expr_string_mult();
            std::cout << "Trying "<< in << "\n";

            ExecResult interp1_result = exec_program(2, interp1_argv, in);
            ExecResult interp2_result = exec_program(2, interp2_argv, in);

            ExecResult print1_result = exec_program(2, print1_argv, in);
            ExecResult print2_result = exec_program(2, print2_argv, in);

            ExecResult pretty_print1_result = exec_program(2, pretty_print1_argv, in);
            ExecResult pretty_print2_result = exec_program(2, pretty_print2_argv, in);

            if (interp1_result.out != interp2_result.out) {
                throw std::runtime_error("different interp results");
                break;
            }
                        
            if (print1_result.out != print2_result.out) {
                throw std::runtime_error("different print results");
                break;
            }

            if (pretty_print1_result.out != pretty_print2_result.out) {
                throw std::runtime_error("different pretty-print  results");
                break;
            }
        }

        for (int i = 0; i < 100; i++) {
            std::string in = random_expr_string_let_add();
            std::cout << "Trying "<< in << "\n";

            ExecResult interp1_result = exec_program(2, interp1_argv, in);
            ExecResult interp2_result = exec_program(2, interp2_argv, in);

            ExecResult print1_result = exec_program(2, print1_argv, in);
            ExecResult print2_result = exec_program(2, print2_argv, in);

            ExecResult pretty_print1_result = exec_program(2, pretty_print1_argv, in);
            ExecResult pretty_print2_result = exec_program(2, pretty_print2_argv, in);

            if (interp1_result.out != interp2_result.out) {
                throw std::runtime_error("different interp results");
                break;
            }

            if (print1_result.out != print2_result.out) {
                throw std::runtime_error("different print results");
                break;
            }

            if (pretty_print1_result.out != pretty_print2_result.out) {
                throw std::runtime_error("different pretty-print results");
                break;
            }
        }

        for (int i = 0; i < 100; i++) {
            std::string in = random_expr_string_let_mult();
            std::cout << "Trying "<< in << "\n";

            ExecResult interp1_result = exec_program(2, interp1_argv, in);
            ExecResult interp2_result = exec_program(2, interp2_argv, in);

            ExecResult print1_result = exec_program(2, print1_argv, in);
            ExecResult print2_result = exec_program(2, print2_argv, in);

            ExecResult pretty_print1_result = exec_program(2, pretty_print1_argv, in);
            ExecResult pretty_print2_result = exec_program(2, pretty_print2_argv, in);

            if (interp1_result.out != interp2_result.out) {
                throw std::runtime_error("different interp results");
                break;
            }

            if (print1_result.out != print2_result.out) {
                throw std::runtime_error("different print results");
                break;
            }

            if (pretty_print1_result.out != pretty_print2_result.out) {
                throw std::runtime_error("different pretty-print results");
                break;
            }
        }
std::cout << "All Comparisons successful!\n";
return 0;
}

std::string random_expr_string_add() {
    if ((rand() % 10) < 6)
        return std::to_string(rand());
    else
        return random_expr_string_add() + "+" + random_expr_string_add();
}

std::string random_expr_string_mult() {
    if ((rand() % 10) < 6)
        return std::to_string(rand());
    else
        return random_expr_string_mult() + "*" + random_expr_string_mult();
}

std::string random_expr_string_let_add() {
    if(depth_count == 0) {
        depth_count++;
        return "_let x = " + random_expr_string_add() + " _in x + " + random_expr_string_let_add();
    }
    else if(depth_count > 0 && depth_count < 3) {
        depth_count++;
        return random_expr_string_add() + " + _let x = " + random_expr_string_add() + " _in x + " + random_expr_string_let_add();
    }
    else
        depth_count = 0;
        return random_expr_string_add();
}

std::string random_expr_string_let_mult() {
    if(depth_count == 0) {
        depth_count++;
        return "_let x = " + random_expr_string_mult() + " _in x * " + random_expr_string_let_mult();
    }
    else if(depth_count > 0 && depth_count < 3) {
        depth_count++;
        return random_expr_string_mult() + " * _let x = " + random_expr_string_mult() + " _in x * " + random_expr_string_let_mult();
    }
    else
        depth_count = 0;
        return random_expr_string_mult();
}