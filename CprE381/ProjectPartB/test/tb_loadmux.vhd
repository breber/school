-------------------------------------------------------------------------
-- Group 9  :  Scott Connell, Brian Reber, Arjay Vander Velden
-- Project Part B
-- Due: November 9th, 2011
-------------------------------------------------------------------------

-- tb_loadmux.vhd
-------------------------------------------------------------------------
-- DESCRIPTION:  This is the Load Mux testbench.
--               See the "Omniscient Spreadsheet" for more information
--               about what each instruction requires.
--               
-------------------------------------------------------------------------


library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;


entity tb_loadmux is

end tb_loadmux;

architecture behavioral of tb_loadmux is


Component load_mux is
  port(
			i_Addr 	 			: in std_logic_vector(1 downto 0);
			i_SelW_HW_B	: in std_logic_vector(2 downto 0);
			i_Word				  : in std_logic_vector(31 downto 0);
			o_S						: out std_logic_vector(31 downto 0)
			);
end Component;

signal s_Word, s_S                   : std_logic_vector( 31 downto 0 );
signal s_Addr                        : std_logic_vector( 1 downto 0 );
signal s_SelW_HW_B                   : std_logic_vector( 2 downto 0 );



begin
  testme: load_mux
   port map(  i_Addr      => s_Addr,
              i_SelW_HW_B => s_SelW_HW_B,
              i_Word      => s_Word,
              o_S	        => s_S);


process
begin
  
  -------------------------
  -- word 000
  -------------------------
  s_Addr <= "00";
  s_SelW_HW_B <= "000";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "01";
  s_SelW_HW_B <= "000";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "10";
  s_SelW_HW_B <= "000";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "11";
  s_SelW_HW_B <= "000";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  -------------------------
  --half word 010
  -------------------------
  s_Addr <= "00";
  s_SelW_HW_B <= "010";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "01";
  s_SelW_HW_B <= "010";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "10";
  s_SelW_HW_B <= "010";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "11";
  s_SelW_HW_B <= "010";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  -------------------------
  --half word unsigned 011
  -------------------------
  
  
  s_Addr <= "00";
  s_SelW_HW_B <= "011";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "01";
  s_SelW_HW_B <= "011";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "10";
  s_SelW_HW_B <= "011";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "11";
  s_SelW_HW_B <= "011";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  -------------------------
  --byte 100
  -------------------------  
   
  s_Addr <= "00";
  s_SelW_HW_B <= "100";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "01";
  s_SelW_HW_B <= "100";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "10";
  s_SelW_HW_B <= "100";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "11";
  s_SelW_HW_B <= "100";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  
  -------------------------
  --byte unsigned 101
  -------------------------
  
  s_Addr <= "00";
  s_SelW_HW_B <= "101";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "01";
  s_SelW_HW_B <= "101";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "10";
  s_SelW_HW_B <= "101";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  s_Addr <= "11";
  s_SelW_HW_B <= "101";
  s_Word <= x"aaaacccc";
  wait for 100 ns;
  
  
  

end process;
  
end behavioral;




