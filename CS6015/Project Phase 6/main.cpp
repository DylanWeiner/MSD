//#define CATCH_CONFIG_MAIN
//#include "catch.h"
// #define CATCH_CONFIG_RUNNER
// #ifndef expr_h
#include "catch.h"
#include "cmdline.h"
#include "parse.hpp"
// #define expr_h
// #endif

int main(int argc, char **argv) {
  // Expr *e = parse_str("1+2+3+4"); /// Parses user input.

  // std::cout << "Expression: ";
  // e->printExp(std::cout);
  // std::cout << "\n";
  // std::cout << "Value: " << e->interp() << "\n";

  int action = use_arguments(argc, argv); /// Reads arguments from the commandline into the use_arguments method.

  if(action == do_interp) {
    Expr *e = parse_expr(std::cin); /// Parses user input.

    std::cout << "Expression: ";
    e->printExp(std::cout);
    std::cout << "\n";
    std::cout << "Value: " << e->interp() << "\n";
  }
  else if(action == do_print) {
    Expr *e = parse_expr(std::cin); /// Parses user input.

    std::cout << "Expression: ";
    e->printExp(std::cout);
    std::cout << "\n";
    // e->printExp(std::cout);
    // std::cout << "\n";
  }
  else if(action == do_pretty_print) {
    Expr *e = parse_expr(std::cin); /// Parses user input.

    std::cout << "Expression: ";
    // e->printExp(std::cout);
    // std::cout << "\n";
    e->pretty_print(std::cout);
    std::cout << "\n";
  }
  else if(action == do_nothing) {
    return 0;
  }

  return 0;
}
