-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- sixteen_to_32_extender.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a 16 bit to 32 bit extender
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity sixteen_to_32_extender is
	port(
		i_In : in std_logic_vector(15 downto 0);
		i_S	 : in std_logic;										-- 0 = zero extend, 1 = sign extend
		o_F  : out std_logic_vector(31 downto 0)
		);
end sixteen_to_32_extender;

architecture dataflow of sixteen_to_32_extender is

signal sel : std_logic_vector(1 downto 0);

begin
	sel(0) <= i_S;
	sel(1) <= i_In(15);
	
	with sel select
		o_F <= x"0000" & i_In when "01",
					 x"FFFF" & i_In when "11",
					 x"0000" & i_In when others;

end dataflow;
