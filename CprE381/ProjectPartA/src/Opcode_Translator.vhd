-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Lab Date: 09/28/2011 + 10/5/2011
-------------------------------------------------------------------------


-- opcode_translator.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an implementation a custom translator.
--              It will take in a 3-bit op code, and output the values
--              necessary for the one bit ALU. 
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;

entity opcodetranslator is
  
  port(i_OpCode : in  std_logic_vector(2 downto 0);
       o_CryIn  : out std_logic;
       o_AInv   : out std_logic;
       o_BInv   : out std_logic;
       o_OpCode : out std_logic_vector(2 downto 0)
       );
       
end opcodetranslator;


architecture dataflow of opcodetranslator is

begin

  -- Set the opcode output based on the input opcode
  with i_OpCode select
  o_OpCode <= "101" when "110",      -- Translate slt to add instruction
              "101" when "111",      -- Translate sub to add instruction
              i_OpCode when others;  -- Leave all other instructions alone
          
  -- Set the carry-in output based on the input opcode 
  -- Will be 1 when we need to perform a subtract operation, 0 otherwise
  with i_OpCode select
  o_CryIn <=  '1' when "110",   -- Translate slt to add instruction
              '1' when "111",   -- Translate sub to add instruction
              '0' when others;  -- Leave all other instructions alone
              
  -- Set the BInv output based on the input opcode 
  -- Will be 1 when we need to perform a subtract operation, 0 otherwise
  with i_OpCode select
  o_BInv <=   '1' when "110",   -- Translate slt to add instruction
              '1' when "111",   -- Translate sub to add instruction
              '0' when others;  -- Leave all other instructions alone
              
  -- As of now, we don't need to invert A, so hard-code it to 0
  o_AInv <= '0';         
        
end dataflow;
