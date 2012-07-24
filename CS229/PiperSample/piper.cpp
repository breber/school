/* 
 * File:   piper.cpp
 * Author: Ezra
 *
 * This file acts as a piper between the Robots and the Simulation for the
 * Spring 2011 CS229 second project.
 *
 * It takes the following command-line arguments:
 * [0] The name of the file, as usual
 * [1] The name of the simulation file
 * [2]..[n+1]  The robot executables, where n is the number of robots
 * [1+n+1] The config file
 * [1+n+2] The number of turns
 * [1+n+3] The mode (-quiet or -verbose)
 *
 * It forks for each bot and the sim and executes correctly.
 *
 * Created on March 31, 2011, 1:11 AM
 */

#include <iostream>
#include <sstream>
#include <sys/wait.h>
#include <sys/types.h>
#include <signal.h>
#include <unistd.h>
using namespace std;

//A struct to represent all information about a single bot
//Name is not here because the program doesn't need to remember 
//the name of the bot, only its communication and process info
typedef struct
{
    int botReaderFD;
    int botWriterFD;
    int simReaderFD;
    int simWriterFD;

    pid_t id;

} botInfo;

//spawns a single bot
bool spawnBot(char * botExe, botInfo & info);
//spawns the sim
pid_t spawnSim (char * simExe, char ** simArgs, int numBots, botInfo * infos);
//converts an int to a c++-string
string intToString(int toConvert);
//converts an int to a c-string
char * intToCString (int toConvert);
//kills all the running processes
void killAllRunningBots (botInfo * infos, int numBots);

//Constant to represent trouble exectuing
const int INVALIDFILE = 142;
//Number of args the sim needs
const int NUMSIMARGS = 3;
//minimun number of robots
const int MINROBOTS = 1;
//The number of args besides for the sim args and robots
const int NUMSTARTARGS = 2;


/*
 * Executes the main program.
 */
int main(int argc, char** argv)
{
    if (argc < NUMSTARTARGS + NUMSIMARGS + MINROBOTS)
    {
        cout << "Usage: piper <simulate> <Robot1> ... <RobotN> <configFile> <Turns> <-quiet/-verbose>" << endl;
        return -1;
    }

    //make use of command-line arguments
    char * simExe = argv[1];

    int numBots = argc - (NUMSIMARGS + NUMSTARTARGS);

    //create and initialize botargs
    char ** bots = new char *[numBots];
    for (int i = 0; i < numBots; i++)
        bots[i] = argv[NUMSTARTARGS + i];

    //create and initialize simargs
    char ** simArgs = new char* [NUMSIMARGS];
    for (int i = 0; i < NUMSIMARGS; i++)
        simArgs[i] = argv[NUMSTARTARGS + numBots + i];

    //create and initlize bot infos
    botInfo * infos = new botInfo[numBots];
    for (int i = 0; i < numBots; i++)
    {
        infos[i].botReaderFD = infos[i].botWriterFD = 0;
        infos[i].simReaderFD = infos[i].simWriterFD = 0;
        infos[i].id = 0;
    }
    pid_t simID;

    //spawn bots, checking for failure on each
    for (int i = 0; i < numBots; i++)
    {
        if (spawnBot(bots[i], infos[i]) == false)
        {
            cerr << "Couldnt spawn necessary processes!" << endl;
            killAllRunningBots(infos, numBots);
            return -1;
        }
    }

    //spawn sim, checking for failure
    simID = spawnSim (simExe, simArgs, numBots, infos);
    if (simID == -1)
    {
        cerr << "Couldnt spawn necessary processes!" << endl;
        killAllRunningBots(infos, numBots);
        return -1;
    }

    //now we wait for a process to finish
    bool keepWaiting = true;
    do
    {
        int status;
        pid_t finishedProcess = wait (&status);
        if (finishedProcess == simID || (WIFEXITED(status) != 0  && WEXITSTATUS(status) == INVALIDFILE))
        {
            //In case sim finished but forgot to send out last signal,
            //or if an invalid file, so a child process exited
            //kill all the children spawned if they are still running
            killAllRunningBots(infos, numBots);
            if (finishedProcess != simID) //so it must have been bad file
                return -1;
            keepWaiting = false;
        }
    }while (keepWaiting == true);


    return 0;
}

//spawns a single bot
//botExe is the name of the executable
//populates info with the pipes and process ID once created
bool spawnBot(char * botExe, botInfo & info)
{

    //set up pipes
    int * simToBot = new int [2];
    int * botToSim = new int [2];

    pipe (simToBot);
//    pipe (botToSim);

    info.botReaderFD = simToBot[0];
    info.botWriterFD = botToSim[1];
    info.simReaderFD = botToSim[0];
    info.simWriterFD = simToBot[1];

    info.id = fork();
    if (info.id == -1)
    {
        //couldnt fork
        return false;
    }
    else if (info.id == 0)
    {
        //redirect and execute
        dup2(info.botReaderFD, 0);
        dup2(info.botWriterFD, 1);
        char ** argsToUse = new char* [2];
        argsToUse[0] = botExe;
        argsToUse[1] = NULL;
        
        execvp(argsToUse[0], argsToUse);
        
        //if get here, can't execute
        cerr << "Couldn't execute file " << botExe << endl;
        exit(INVALIDFILE);
        
    }
}

//kills all the running bots
//checks if each is running, if it is then kills it
void killAllRunningBots (botInfo * infos, int numBots)
{
    for (int i = 0; i < numBots; i++)
    {
        pid_t id = waitpid(infos[i].id, NULL, WNOHANG);
        if (id == 0)
            kill(infos[numBots].id, 0);
    }
}

//spawns the sim, using the given args, bots and executable
pid_t spawnSim (char * simExe, char ** simArgs, int numBots, botInfo * infos)
{
    pid_t toReturn =  fork();

    if (toReturn == 0)
    {
        //create array with all args and file descriptors
        int numArgs = 1 + NUMSIMARGS + (2 * numBots) + 1;
        char ** argsToUse = new char * [numArgs];
        argsToUse[0] = simExe;
        for (int i = 0; i < NUMSIMARGS; i++)
            argsToUse[1+i] = simArgs[i];

        for (int i = 0; i < numBots; i++)
        {
            argsToUse[1 + NUMSIMARGS + (2 * i)] = intToCString(infos[i].simWriterFD);
            argsToUse[1 + NUMSIMARGS + ((2 * i) + 1)] = intToCString(infos[i].simReaderFD);
        }
        argsToUse[numArgs - 1] = NULL;
        
        execvp(argsToUse[0], argsToUse);

        //if get here, can't execute
        cerr << "Couldn't execute file " << simExe << endl;
        exit(INVALIDFILE);
    }
    else
    {
        return toReturn;
    }
}

//converts an int to a cstring
char * intToCString (int toConvert)
{
    char * toReturn;
    string writer = intToString (toConvert);
    toReturn = new char [writer.length()];
    strcpy(toReturn, writer.c_str());
    return toReturn;
}

//converts an int to a c++ string
string intToString(int toConvert)
{
    stringstream out;
    out << toConvert;
    return out.str();
}

