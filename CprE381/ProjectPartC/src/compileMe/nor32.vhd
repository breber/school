-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Lab Date: 09/28/2011 + 10/5/2011
-------------------------------------------------------------------------


-- nor32.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an implementation of a 32-bit nor
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;

entity nor32 is
  
  port(i_In 		: in  std_logic_vector(31 downto 0);
       o_F			: out std_logic
       );
       
end nor32;


architecture dataflow of nor32 is

begin

  -- Return 1 when our input is x"00000000", 0 otherwise
  with i_In select
  o_F	 <= '1' when x"00000000",
          '0' when others;
          
end dataflow;
