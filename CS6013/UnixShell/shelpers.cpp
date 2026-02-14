#include "shelpers.hpp"


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
            // cout << command.argv[last];
            
            if(cmdNumber == 0 && tokens[j] == "<") {
                if((command.inputFd = open(tokens[j+1].c_str(), O_RDONLY, 0644)) < 0) {
                    perror("File is not opening");
                }
            }

            else if(cmdNumber == commands.size()-1 && tokens[j] == ">") {
                if((command.outputFd = open(tokens[j+1].c_str(), O_WRONLY | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR, 0644)) < 0) {
                    perror("File is not opening");
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
            // Fill this in if you choose to do the optional "background command" part.
            assert(false);
         }
         else {
            // Otherwise this is a normal command line argument! Add to argv.
            command.argv.push_back( tokens[j].c_str() );
         }
      }

      if( !error ) {
        int previousPipe = STDIN_FILENO;
            
            if(cmdNumber < commands.size()-1) {
                if (pipe(fds) < 0) { // This means something is wrong when trying to pipe.
                    perror("pipe error");
                    exit(EXIT_FAILURE);
                }
                command.inputFd = previousPipe;
                command.outputFd = fds[1];
                previousPipe = fds[0];
            }
            else {
                command.inputFd  = previousPipe;
                command.outputFd = STDOUT_FILENO;
            }

            if( cmdNumber > 0 ){
                if (pipe(fds) < 0) {
                    perror("pipe error");
                    exit(EXIT_FAILURE);
                }
                command.inputFd = fds[0];
                commands[cmdNumber - 1].outputFd = fds[1];
                commands[cmdNumber].outputFd = STDOUT_FILENO;
            }

         // Exec wants argv to have a nullptr at the end!
         command.argv.push_back( nullptr );

         // Find the next pipe character
         first = last + 1;

         if( first < tokens.size() ){
            last = find( tokens.begin() + first, tokens.end(), "|" ) - tokens.begin();
        }
        if(strcmp(commands[cmdNumber+1].execName.c_str(), nullptr)) {
            error = true;
        }
      } // end if !error
   } // end for( cmdNumber = 0 to commands.size )

//    close(fds[0]);
//     close(fds[1]);
    if( error ){
        if(fds[0] >= 0) {
            close(fds[0]);
        }
        if(fds[1] >= 0) {
            close(fds[1]);
        }
      // Close any file descriptors you opened in this function and return the appropriate data!

      // Note, an error can happen while parsing any command. However, the "commands" vector is
      // pre-populated with a set of "empty" commands and filled in as we go.  Because
      // of this, a "command" name can be blank (the default for a command struct that has not
      // yet been filled in).  (Note, it has not been filled in yet because the processing
      // has not gotten to it when the error (in a previous command) occurred.

      assert(false);
   }

   return commands;

} // end getCommands()

void runCommands( const vector<Command> & allCommands ) {
    for(int i = 0; i < allCommands.size(); i++) {
            pid_t pID = fork();
            vector<pid_t> childPids;
            if(pID < 0) { // This means something is wrong when trying to fork.
                perror("\nfork error");
                exit(EXIT_FAILURE);
            }
            if(pID == 0) { // This marks the child process.
                
                if(allCommands[i].inputFd != STDIN_FILENO) {
                    if(dup2(allCommands[i].inputFd, STDIN_FILENO) < 0) {
                        perror("dup2 is failing input");
                    }
                    close(allCommands[i].inputFd);
                }
                if(allCommands[i].outputFd != STDOUT_FILENO) {
                    if(dup2(allCommands[i].outputFd, STDOUT_FILENO) < 0) {
                        perror("dup2 is failing output");
                    }
                    close(allCommands[i].outputFd);
                }
                
                if(execvp(allCommands[i].argv[0], const_cast<char *const*>(allCommands[i].argv.data())) < 0)
                    perror("\nexecvp failed!");
                exit(EXIT_SUCCESS);
            }
            else {
                childPids.push_back(pID);
            }
            for (pid_t pid : childPids) {
                int status;
                waitpid(pid, &status, 0);
            }
        }
}
