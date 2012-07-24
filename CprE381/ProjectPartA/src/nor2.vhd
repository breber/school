-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Lab Date: 09/28/2011 + 10/5/2011
-------------------------------------------------------------------------

-- nor2.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an implementation of a 2-input NOR 
-- gate.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity nor2 is

  port(i_A          : in std_logic;
       i_B          : in std_logic;
       o_F          : out std_logic);

end nor2;

architecture dataflow of nor2 is
begin

  o_F <= not ( i_A or i_B );
  
end dataflow;
