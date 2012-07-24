-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Lab Date: 09/28/2011 + 10/5/2011
-------------------------------------------------------------------------


-- tb_mux7.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: Mux 7 Test Bench
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity tb_mux7 is
end tb_mux7;

architecture behavioral of tb_mux7 is

  Component mux7 is
  port(i_A  : in std_logic;
       i_B  : in std_logic;
       i_C  : in std_logic;
       i_D  : in std_logic;
       i_E  : in std_logic;
       i_F  : in std_logic;
       i_G  : in std_logic;
       i_Sel: in std_logic_vector(2 downto 0);
       o_F  : out std_logic);

end Component;

signal s_A, s_B, s_C, s_D, s_E, s_F, s_G, s_oF  : std_logic;
signal s_Sel                                    : std_logic_vector(2 downto 0);

begin
  testme: mux7 

  port map(i_A => s_A,
           i_B => s_B,
           i_C => s_C,
           i_D => s_D,
           i_E => s_E,
           i_F => s_F,
           i_G => s_G,
           i_Sel => s_Sel,
  	        o_F  => s_oF);
  	        
  
process
begin
    s_A <= '0';
    s_B <= '1';
    s_C <= '1';
    s_D <= '1';
    s_E <= '1';
    s_F <= '1';
    s_G <= '1';
    s_Sel <= "000";
    wait for 100 ns;
    
    s_A <= '0';
    s_B <= '1';
    s_C <= '0';
    s_D <= '0';
    s_E <= '0';
    s_F <= '0';
    s_G <= '0';
    s_Sel <= "001";
    wait for 100 ns;
    
    s_A <= '0';
    s_B <= '0';
    s_C <= '0';
    s_D <= '0';
    s_E <= '0';
    s_F <= '0';
    s_G <= '0';
    s_Sel <= "001";
    wait for 100 ns;
    
    s_A <= '0';
    s_B <= '0';
    s_C <= '1';
    s_D <= '0';
    s_E <= '0';
    s_F <= '0';
    s_G <= '0';
    s_Sel <= "010";
    wait for 100 ns;
    
    s_A <= '0';
    s_B <= '0';
    s_C <= '0';
    s_D <= '1';
    s_E <= '0';
    s_F <= '0';
    s_G <= '0';
    s_Sel <= "011";
    wait for 100 ns;
    
    s_A <= '0';
    s_B <= '0';
    s_C <= '0';
    s_D <= '0';
    s_E <= '1';
    s_F <= '0';
    s_G <= '0';
    s_Sel <= "100";
    wait for 100 ns;
    
    s_A <= '0';
    s_B <= '0';
    s_C <= '0';
    s_D <= '0';
    s_E <= '0';
    s_F <= '1';
    s_G <= '0';
    s_Sel <= "101";
    wait for 100 ns;
    
    s_A <= '0';
    s_B <= '0';
    s_C <= '0';
    s_D <= '0';
    s_E <= '0';
    s_F <= '0';
    s_G <= '1';
    s_Sel <= "110";
    wait for 100 ns;
    
    s_A <= '0';
    s_B <= '0';
    s_C <= '0';
    s_D <= '0';
    s_E <= '0';
    s_F <= '0';
    s_G <= '0';
    s_Sel <= "111";
    wait for 100 ns;
    


  end process;
  
end behavioral;
