#include <stdio.h>

int main()
{
  char word1[30] = " 185";
  char word2[30] = " Is";
  char word3[30] = " The";
  char word4[30] = " Absolute";
  char word5[30] = " Best Class";
  
  char retword[30] = "";
  
  int i;
  for(i=0; i<2; i++)
  {
    strcat(retword, word1);
    strcat(retword, word2);
    if(i==1)
    {
      strcat(retword, word3);
      if(i==2)
      {
        strcat(retword, word4);
      }
    }
    strcat(retword, word5);
    printf("%s\n", retword);
    clear_retword(retword); // Pseudo code: resets retword to be blank -> kind of like retword = "";
  }
}

What does the output look like?

 185 Is Best Class
 185 Is The Best Class