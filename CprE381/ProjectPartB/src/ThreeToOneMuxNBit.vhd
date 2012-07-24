-------------------------------------------------------------------------
-- Scott Connell
-- Lab-02
-- Lab Date: 08/31/2011
-------------------------------------------------------------------------


-- ThreeToOneMuxNBit.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is an N-bit 3:1 MUX implemented dataflowally.
--    A: 00
--    B: 01
--    C: 10
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity mux_3to1n is
  
  generic( N  :   integer := 5 );
  port( i_A   :   in   std_logic_vector( N-1 downto 0 );
        i_B   :   in   std_logic_vector( N-1 downto 0 );
        i_C   :   in   std_logic_vector( N-1 downto 0 );
        sel   :   in   std_logic_vector( 1   downto 0 );
        o_R   :   out  std_logic_vector( N-1 downto 0 )   );
        
end mux_3to1n;


architecture dataflow of mux_3to1n is
  
begin
    
  with sel select
  o_R      <=  i_A when "00",
               i_B when "01",
               i_C when others; --assume that all others will only be "10"              
    
end dataflow;
