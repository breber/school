-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- tb_two_to_one_mux.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a simple VHDL testbench for the
-- 2:1 Muxes
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

-- This is an empty entity so we don't need to declare ports
entity tb_two_to_one_mux is

end tb_two_to_one_mux;

architecture behavior of tb_two_to_one_mux is

-- Declare the components we are going to instantiate
component nbit_2_1_mux
  generic(N : integer := 32);
	port(
			i_A  : in std_logic_vector(N-1 downto 0);
			i_B  : in std_logic_vector(N-1 downto 0);
			i_S  : in std_logic;
			o_F  : out std_logic_vector(N-1 downto 0)
			);
end component;

component nbit_2_1_mux_df
	generic(N : integer := 32);
	port(
			i_A  : in std_logic_vector(N-1 downto 0);
			i_B  : in std_logic_vector(N-1 downto 0);
			i_S  : in std_logic;
			o_F  : out std_logic_vector(N-1 downto 0)
			);
end component;

signal s_A, s_B, s_OutStructural, s_OutDataflow : std_logic_vector(31 downto 0);
signal s_S : std_logic;

begin

Structural: nbit_2_1_mux
  port map(
					i_A  => s_A,
					i_B  => s_B,
					i_S  => s_S,
  	      o_F  => s_OutStructural
					);

Dataflow: nbit_2_1_mux_df
  port map(
					i_A  => s_A,
					i_B  => s_B,
					i_S  => s_S,
  	      o_F  => s_OutDataflow
					);

  process
  begin

    s_A <= "01110111011101110111011101110111";
		s_B <= "10001000100010001000100010001000";
		s_S <= '0';
    wait for 200 ns;
		-- Output should be s_A

		s_S <= '1';
    wait for 200 ns;
		-- Output should be s_B

    s_A <= "01110111011101110111011101110111";
		s_B <= "10101010101010101010101010011001";
		s_S <= '1';
    wait for 200 ns;
		-- Output should be s_B

  end process;

end behavior;
