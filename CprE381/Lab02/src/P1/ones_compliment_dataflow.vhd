-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- ones_compliment_dataflow.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an N-bit One's Complimenter using
-- dataflow VHDL
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity ones_complimenter_df is
	generic(N : integer := 16);
	port(
		i_A : in std_logic_vector(N-1 downto 0);
		o_F : out std_logic_vector(N-1 downto 0)
		);
end ones_complimenter_df;

architecture dataflow of ones_complimenter_df is

begin

-- We loop through and instantiate and perform the inversion
G1: for i in 0 to N-1 generate
	o_F(i) <= not i_A(i);
end generate;

end dataflow;
