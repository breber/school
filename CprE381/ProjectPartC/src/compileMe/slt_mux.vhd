-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Lab Date: 09/28/2011 + 10/5/2011
-------------------------------------------------------------------------


-- slt_mux.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an implementation a custom mux.
--							It takes in a 3 bit selection signal (corresponding to the
--							ALU opcode. If it is the opcode for SLT (110), it will 
--							select the MSB, otherwise it will pass the input through.
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;

entity slt_mux is
  
  port(i_Sel 		: in  std_logic_vector(2 downto 0);
       i_Input  : in  std_logic_vector(31 downto 0);
       o_F			: out std_logic_vector(31 downto 0)
       );
       
end slt_mux;


architecture dataflow of slt_mux is

begin

  -- If our selection indicates a SLT operation, select the MSB
  -- otherwise, pass the input through
  with i_Sel select
  o_F	 <= "0000000000000000000000000000000" & i_Input(31) when "110",
          i_Input when others;
          
end dataflow;
