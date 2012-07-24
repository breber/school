-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- eight_to_32_extender.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a 8 bit to 32 bit extender
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity eight_to_32_extender is
	port(
		i_In : in std_logic_vector(7 downto 0);
		i_S	 : in std_logic;										-- 0 = zero extend, 1 = sign extend
		o_F  : out std_logic_vector(31 downto 0)
		);
end eight_to_32_extender;

architecture dataflow of eight_to_32_extender is

signal sel : std_logic_vector(1 downto 0);

begin
	sel(0) <= i_S;
	sel(1) <= i_In(7);
	
	with sel select
		o_F <= x"000000" & i_In when "01",
					 x"FFFFFF" & i_In when "11",
					 x"000000" & i_In when others;

end dataflow;
