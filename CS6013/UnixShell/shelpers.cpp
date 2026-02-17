#include "shelpers.hpp"
#include <filesystem>


//////////////////////////////////////////////////////////////////////////////////
//
// Author: Ben Jones (I think) with a lot of clean up by J. Davison de St. Germain
// Dylan finished the code (Can't believe they left code half finished)
// Date:   2019?
//         Jan 2022 - Cleanup
//
// Class: CS 6013 - Systems I
//
//////////////////////////////////////////////////////////////////////////////////

using namespace std;

////////////////////////////////////////////////////////////////////////
// Example test commands you can try once your shell is up and running:
//
// ls
// ls | nl
// cd [dir]
// cat < shelpers.cpp
// cat < shelpers.cpp | nl
// cat shelpers.cpp | nl
// cat shelpers.cpp | nl | head -50 | tail -10
// cat shelpers.cpp | nl | head -50 | tail -10 > ten_lines.txt 
// 
// - The following two commands are equivalent.  [data.txt is sent into nl and the
//   output is saved to numbered_data.txt.]
//
// nl > numbered_data.txt < data.txt
// nl < data.txt > numbered_data.txt 
//
// - Assuming numbered_data.txt has values in it... try running:
//   [Note this probably doesn't work like one might expect...
//    does it behave the same as your normal shell?]
//
// nl < numbered_data.txt > numbered_data.txt
//
// - The following line is an error (input redirection at end of line).
//   It should fail gracefully (ie, 1) without doing anything, 2) cleaning
//   up any file descriptors that were opened, 3) giving an appropriate
//   message to the user).
//
// cat shelpers.cpp | nl | head -50 | tail -10 > ten_lines.txt < abc
// 

////////////////////////////////////////////////////////////////////////
// This routine is used by tokenize().  You do not need to modify it.

bool splitOnSymbol( vector<string> & words, int i, char c )
{
   if( words[i].size() < 2 ){
      return false;
   }
   int pos;
   if( (pos = words[i].find(c)) != string::npos ){
      if( pos == 0 ){
         // Starts with symbol.
         words.insert( words.begin() + i + 1, words[i].substr(1, words[i].size() -1) );
         words[i] = words[i].substr( 0, 1 );
      }
      else {
         // Symbol in middle or end.
         words.insert( words.begin() + i + 1, string{c} );
         string after = words[i].substr( pos + 1, words[i].size() - pos - 1 );
         if( !after.empty() ){
            words.insert( words.begin() + i + 2, after );
         }
         words[i] = words[i].substr( 0, pos );
      }
      return true;
   }
   else {
      return false;
   }
}

////////////////////////////////////////////////////////////////////////
// You do not need to modify tokenize().  

vector<string> tokenize( const string& s )
{
   vector<string> ret;
   int pos = 0;
   int space;

   // Split on spaces:

   while( (space = s.find(' ', pos) ) != string::npos ){
      string word = s.substr( pos, space - pos );
      if( !word.empty() ){
         ret.push_back( word );
      }
      pos = space + 1;
   }

   string lastWord = s.substr( pos, s.size() - pos );

   if( !lastWord.empty() ){
      ret.push_back( lastWord );
   }

   for( int i = 0; i < ret.size(); ++i ) {
      for( char c : {'&', '<', '>', '|'} ) {
         if( splitOnSymbol( ret, i, c ) ){
            --i;
            break;
         }
      }
   }
   return ret;
}

////////////////////////////////////////////////////////////////////////

ostream& operator<<( ostream& outs, const Command& c )
{
   outs << c.execName << " [argv: ";
   for( const auto & arg : c.argv ){
      if( arg ) {
         outs << arg << ' ';
      }
      else {
         outs << "NULL ";
      }
   }
   outs << "] -- FD, in: " << c.inputFd << ", out: " << c.outputFd << " "
        << (c.background ? "(background)" : "(foreground)");
   return outs;
}

////////////////////////////////////////////////////////////////////////
//
// getCommands()
//
// Parses a vector of command line tokens and places them into (as appropriate)
// separate Command structures.
//
// Returns an empty vector if the command line (tokens) is invalid.
//
// You'll need to fill in a few gaps in this function and add appropriate error handling
// at the end.  Note, most of the gaps contain "assert( false )".
//

vector<Command> getCommands( const vector<string> & tokens ) {
    vector<Command> commands( count( tokens.begin(), tokens.end(), "|") + 1 ); // 1 + num |'s commands

    int first = 0;
    int last = find( tokens.begin(), tokens.end(), "|" ) - tokens.begin();
    int fds[2] = {-1, -1};

    bool error = false;
    vector<int> openedFds;
    int previousPipe = STDIN_FILENO;

    for( int cmdNumber = 0; cmdNumber < commands.size(); ++cmdNumber ){
        
        const string & token = tokens[ first ];

      if( token == "&" || token == "<" || token == ">" || token == "|" ) {
         error = true;
         break;
      }

      Command & command = commands[ cmdNumber ]; // Get reference to current Command struct.
      command.execName = token;

      // Must _copy_ the token's string (otherwise, if token goes out of scope (anywhere)
      // this pointer would become bad...) Note, this fixes a security hole in this code
      // that had been here for quite a while.

      command.argv.push_back( strdup( token.c_str() ) ); // argv0 == program name

      command.inputFd  = STDIN_FILENO;
      command.outputFd = STDOUT_FILENO;

      command.background = false;
	
      for( int j = first + 1; j < last; ++j ) {
        

         if( tokens[j] == ">" || tokens[j] == "<" ) {
            // cout << command.argv[j];
            
            if(cmdNumber == 0 && tokens[j] == "<") {
                int fd = open(tokens[j+1].c_str(), O_RDONLY, 0644);
                if(fd >= 0) {
                    if((command.inputFd = fd) < 0)
                        perror("File is not opening");
                    openedFds.push_back(fd);
                } // Writes command output to preceding value's output.
            }

            else if(cmdNumber == commands.size()-1 && tokens[j] == ">") {
                int fd = open(tokens[j+1].c_str(), O_WRONLY | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH, 0644);
                if(fd >= 0) {
                    if((command.outputFd = fd) < 0) {
                        perror("File is not opening");
                    } // Writes command output to new file.
                    openedFds.push_back(fd);
                }
                j++;
            }
            // Handle I/O redirection tokens
            //
            // Note, that only the FIRST command can take input redirection
            // (all others get input from a pipe)
            // Only the LAST command can have output redirection!
            else {
                assert( false );
            }
         }
         else if( tokens[j] == "&" ){
            command.background = true;
         }
         else {
            // Otherwise this is a normal command line argument! Add to argv.
            command.argv.push_back( tokens[j].c_str() );
         }
      }

      if( !error ) {
        command.inputFd = previousPipe;

        if (cmdNumber < commands.size() - 1) {
            if (pipe(fds) < 0) { // This means something is wrong when trying to pipe.
                    perror("pipe error");
                    error = true;
                    break;
                }
            openedFds.push_back(fds[0]);
            openedFds.push_back(fds[1]);

            command.outputFd = fds[1];
            previousPipe = fds[0]; // for next command
        }

        // Exec wants argv to have a nullptr at the end!
        command.argv.push_back( nullptr );

        // Find the next pipe character
        first = last + 1;

        if( first < tokens.size() ){
            last = find( tokens.begin() + first, tokens.end(), "|" ) - tokens.begin();
        }
        
        } // end if !error
   } // end for( cmdNumber = 0 to commands.size )

    if (error) {
        for (int fd : openedFds) {
            if (fd >= 0) close(fd);
        }
        perror("File Descriptor Read Error");
    }

   return commands;

} // end getCommands()

void runCommands( const vector<Command> & allCommands ) {
    vector<pid_t> childPids;
    for(int i = 0; i < allCommands.size(); i++) {
        pid_t pID = fork(); // Forks and saves process id for future verification.
        if(pID < 0) { // This means something is wrong when trying to fork.
            perror("\nfork error");
            exit(EXIT_FAILURE); // Exits if fork fails.
        }
        if(pID == 0) { // This marks the child process.
            if(allCommands[i].inputFd != STDIN_FILENO) {
                if(dup2(allCommands[i].inputFd, STDIN_FILENO) < 0) {
                    perror("dup2 is failing input");
                close(allCommands[i].inputFd);
                } // Makes sure process swap occurs when stdin does not match the desired input.
            }
            if(allCommands[i].outputFd != STDOUT_FILENO) {
                if((dup2(allCommands[i].outputFd, STDOUT_FILENO)) < 0) {
                    perror("dup2 is failing output");
                close(allCommands[i].outputFd);
                } // Makes sure process swap occurs when stdout does not match the desired output.
            }
            for(int j = 0; j < allCommands.size(); j++) {
                if(i != j) {
                    if(allCommands[j].inputFd != STDIN_FILENO && allCommands[j].inputFd >= 0)
                        close(allCommands[j].inputFd);
                    if(allCommands[j].outputFd != STDOUT_FILENO && allCommands[j].outputFd >= 0)
                        close(allCommands[j].outputFd);
                }
            }
            if(execvp(allCommands[i].argv[0], const_cast<char *const*>(allCommands[i].argv.data())) < 0) { // Attempts to execute code in child process.
                // cout << (allCommands[i].argv) << "/n";
                perror("\nexecvp Failed!");
            }
            exit(EXIT_SUCCESS);
        }
        else {
            childPids.push_back(pID); // Saves all child processes to ensure they are all run.
            if(allCommands[i].inputFd != STDIN_FILENO) 
                close(allCommands[i].inputFd);
            if(allCommands[i].outputFd != STDOUT_FILENO) 
                close(allCommands[i].outputFd);
            if(allCommands[i].execName == "cd") {
                if(allCommands[i].argv.size() == 2) {
                    const char* home = getenv("HOME"); // Creates home directory environment.
                    chdir(home); // Takes us to home directory.
                }
                else {
                    const char* newDir = allCommands[i].argv[1];
                    if(chdir(newDir) != 0) { // Tries to take us to specified path.
                        perror("File Error"); // Errors out if file does not exist.
                    }
                }
            }
        }
    }
    for (pid_t pid : childPids) {
        int status;
        waitpid(pid, &status, 0); // Waits to return to parent until every child is done.
    }
}
