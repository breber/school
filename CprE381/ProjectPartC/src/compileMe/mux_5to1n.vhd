-------------------------------------------------------------------------
-- Scott Connell
-------------------------------------------------------------------------


-- FourToOneMuxNBit.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is an N-bit 4:1 MUX implemented dataflowally.
--    A: 000
--    B: 001
--    C: 010
--    D: 011
--    E: 100
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity mux_5to1n is
  
  generic( N  :   integer := 32 );
  port( i_A   :   in   std_logic_vector( N-1 downto 0 );
        i_B   :   in   std_logic_vector( N-1 downto 0 );
        i_C   :   in   std_logic_vector( N-1 downto 0 );
        i_D   :   in   std_logic_vector( N-1 downto 0 );
        i_E   :   in   std_logic_vector( N-1 downto 0 );
        sel   :   in   std_logic_vector( 2   downto 0 );
        o_R   :   out  std_logic_vector( N-1 downto 0 )   );
end mux_5to1n;


architecture dataflow of mux_5to1n is
  
begin
    
  with sel select
  o_R      <=  i_A when "000",
               i_B when "001",
               i_C when "010",
               i_D when "011",
               i_E when others; --assume that all others will only be "100"
                   
end dataflow;
