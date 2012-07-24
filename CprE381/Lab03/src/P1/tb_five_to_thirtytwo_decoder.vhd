-------------------------------------------------------------------------
-- Brian Reber
-- Department of Electrical and Computer Engineering
-- Iowa State University
-------------------------------------------------------------------------


-- tb_five_to_thirtytwo_decoder.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains a simple VHDL testbench for the
-- 5:32 decoder.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity tb_five_to_thirtytwo_decoder is

end tb_five_to_thirtytwo_decoder;

architecture behavior of tb_five_to_thirtytwo_decoder is

  component five_to_thirtytwo_decoder
		port(i_A : in std_logic_vector(4 downto 0);
         o_F : out std_logic_vector(31 downto 0));
  end component;

  -- Temporary signals to connect to the decoder component.
	signal s_A : std_logic_vector(4 downto 0);
  signal s_F : std_logic_vector(31 downto 0);

begin

  DUT: five_to_thirtytwo_decoder 
  port map(i_A => s_A,
					 o_F => s_F);
  
  -- Testbench process  
  P_TB: process
  begin
    s_A <= "00100";
    wait for 100 ns;

    s_A <= "00001";
    wait for 100 ns;  

    s_A <= "00000";
    wait for 100 ns;  

    s_A <= "11111";
    wait for 100 ns; 

    wait;
  end process;
  
end behavior;
