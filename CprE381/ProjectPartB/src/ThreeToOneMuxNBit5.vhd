-------------------------------------------------------------------------
-- Scott Connell
-- Lab-02
-- Lab Date: 08/31/2011
-------------------------------------------------------------------------


-- ThreeToOneMuxNBit5.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is an N-bit 2:1 MUX implemented structrually.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity mux_3to1n5 is
  
  generic( N  :   integer := 5 );
  port( i_A   :   in   std_logic_vector( N-1 downto 0 );
        i_B   :   in   std_logic_vector( N-1 downto 0 );
        i_C   :   in   std_logic_vector( N-1 downto 0 );
        sel   :   in   std_logic_vector( 1   downto 0 );
        o_R   :   out  std_logic_vector( N-1 downto 0 ));
        
end mux_3to1n5;


architecture dataflow of mux_3to1n5 is

begin
  
  with sel select
  o_R     <=    i_A when "00",
                i_B when "01",
                i_C when others; --This is for all other cases, bro!
                

end dataflow;
