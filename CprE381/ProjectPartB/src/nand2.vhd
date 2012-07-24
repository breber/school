-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Lab Date: 09/28/2011 + 10/5/2011
-------------------------------------------------------------------------


-- nand2.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an implementation of a 2-input NAND 
-- gate.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity nand2 is

  port(i_A          : in std_logic;
       i_B          : in std_logic;
       o_F          : out std_logic);

end nand2;

architecture dataflow of nand2 is
begin

  o_F <= not ( i_A and i_B );
  
end dataflow;
