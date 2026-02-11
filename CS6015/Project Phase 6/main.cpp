//#define CATCH_CONFIG_MAIN
//#include "catch.h"
// #define CATCH_CONFIG_RUNNER
#include "catch.h"
#include "cmdline.h"
#include "expr.h"
#include "parse.hpp"

int main(int argc, char **argv) {
  Expr *e = parse_expr(std::cin); /// Parses user input.

  std::cout << "Expression: ";
  e->printExp(std::cout);
  std::cout << "\n";
  std::cout << "Value: " << e->interp() << "\n";

  use_arguments(argc, argv); /// Reads arguments from the commandline into the use_arguments method.
  return 0;
}
