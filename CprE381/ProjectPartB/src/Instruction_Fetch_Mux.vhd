-------------------------------------------------------------------------
-- Group 9  :  Scott Connell, Brian Reber, Arjay Vander Velden
-- Project Part B
-- Due: November 9th, 2011
-------------------------------------------------------------------------

-- Instruction_Fetch.vhd
-------------------------------------------------------------------------
-- DESCRIPTION:  This is the Instruction Fetch.
--               It chooses where to put the PC next.
--               This is not the whole instruction fetching process, it is
--               really just the mux that determines which place to actually
--               put the PC at next, given WhichJB control signal.
--               It determines between types of jumps and branches.
--
--               See the "Omniscient Spreadsheet" for more information
--               about what each instruction requires.
--               We are not using processes!
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;

entity Instruction_Fetch_Mux is
  
  port( normal_increment    :   in  std_logic_vector( 31 downto 0 );
        jump                :   in  std_logic_vector( 31 downto 0 );
        jumpReg             :   in  std_logic_vector( 31 downto 0 );
        beq                 :   in  std_logic_vector( 31 downto 0 );
        bgez                :   in  std_logic_vector( 31 downto 0 );
        bgtz                :   in  std_logic_vector( 31 downto 0 );
        blez                :   in  std_logic_vector( 31 downto 0 );
        bltz                :   in  std_logic_vector( 31 downto 0 );
        bne                 :   in  std_logic_vector( 31 downto 0 );
        WhichJB             :   in  std_logic_vector( 3  downto 0 );      
        next_instruction    :   out std_logic_vector( 31 downto 0 )  );
        
end Instruction_Fetch_Mux;

architecture dataflow of Instruction_Fetch_Mux is

begin
  
  with WhichJB select
  next_instruction  <=  jump    when  "0001",
                        jumpReg when  "0010",
                        beq     when  "0011",
                        bgez    when  "0101",
                        bgtz    when  "0111",
                        blez    when  "1000",
                        bltz    when  "0110",
                        bne     when  "0100",
                        normal_increment when others; -- if no branch/jump, it's normal (0000)
  
end dataflow;