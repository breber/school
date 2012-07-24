-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- tb_ones_compliment.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a simple VHDL testbench for the
-- One's Complimenters
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

-- This is an empty entity so we don't need to declare ports
entity tb_ones_complimenter is

end tb_ones_complimenter;

architecture behavior of tb_ones_complimenter is

-- Declare the components we are going to instantiate
component ones_complimenter
  generic(N : integer := 32);
  port(
			i_A  : in std_logic_vector(N-1 downto 0);
			o_F  : out std_logic_vector(N-1 downto 0)
			);
end component;

component ones_complimenter_df
  generic(N : integer := 32);
  port(
			i_A  : in std_logic_vector(N-1 downto 0);
      o_F  : out std_logic_vector(N-1 downto 0)
			);
end component;

signal s_A, s_OutStructural, s_OutDataflow : std_logic_vector(31 downto 0);

begin

Structural: ones_complimenter 
  port map(
					i_A  => s_A,
  	      o_F  => s_OutStructural
					);

Dataflow: ones_complimenter_df
  port map(
					i_A  => s_A,
  	      o_F  => s_OutDataflow
					);

  process
  begin

    s_A <= "01110111011101110111011101110111";
    wait for 200 ns;

    s_A <= "10110101011101110111011101110111";
    wait for 200 ns;

  end process;

end behavior;
