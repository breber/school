-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- four_to_one_mux_nbit.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an N-bit 4:1 Mux using structural VHDL
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity nbit_4_1_mux is
	generic(N : integer := 32);
	port(
		i_A  : in std_logic_vector(N-1 downto 0);
		i_B  : in std_logic_vector(N-1 downto 0);
		i_C  : in std_logic_vector(N-1 downto 0);
		i_D  : in std_logic_vector(N-1 downto 0);
		i_S  : in std_logic_vector(1 downto 0);
		o_F  : out std_logic_vector(N-1 downto 0)
		);
end nbit_4_1_mux;

architecture dataflow of nbit_4_1_mux is

begin

with i_S select
	o_F <=  i_A  when "00",
					i_B  when "01",
					i_C  when "10",
					i_D  when "11",
					(others => '0') when others;

end dataflow;
