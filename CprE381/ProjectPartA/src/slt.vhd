-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Lab Date: 09/28/2011 + 10/5/2011
-------------------------------------------------------------------------

-- slt.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an implementation of a 2-input "slt gate"
--              This is just for the one-bit version and just returns 0.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity slt is

  port ( i_A    :   in  std_logic;
         i_B    :   in  std_logic;
         o_F    :   out std_logic );

end slt;


architecture dataflow of slt is

begin
  
  o_F <=  '0';
  
end dataflow;