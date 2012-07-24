-------------------------------------------------------------------------
-- Scott Connell
-- Lab 03
-- Lab Date: 9/7/2011
-------------------------------------------------------------------------


-- 32to1Mux.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is a 32:1 mux implemented with with-select-when
-- statements.  It is a 32-bit mux.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;
use WORK.MIPS_package.all;

entity mux_32to1 is
  
  port( o_data   :   out std_logic_vector( 31 downto 0 );    -- the output of the Mux
        i_data   :   in  array32_bit( 31 downto 0 );    -- the 32inputs
        sel      :   in  std_logic_vector( 4 downto 0));    -- selector 5-bits
        
end mux_32to1;


architecture dataflow of mux_32to1 is

begin
  
  -------------------------------------------------------------------------
  -- i_data(0) when 0, i_data(31) when 11111
  -------------------------------------------------------------------------
  with sel select
    o_data <=  i_data( 0 )  when "00000",
               i_data( 1 )  when "00001",
               i_data( 2 )  when "00010",
               i_data( 3 )  when "00011",
               i_data( 4 )  when "00100",
               i_data( 5 )  when "00101",
               i_data( 6 )  when "00110",
               i_data( 7 )  when "00111",
               i_data( 8 )  when "01000",
               i_data( 9 )  when "01001",
               i_data( 10 ) when "01010",
               i_data( 11 ) when "01011",
               i_data( 12 ) when "01100",
               i_data( 13 ) when "01101",
               i_data( 14 ) when "01110",
               i_data( 15 ) when "01111",
               i_data( 16 ) when "10000",
               i_data( 17 ) when "10001",
               i_data( 18 ) when "10010",
               i_data( 19 ) when "10011",
               i_data( 20 ) when "10100",
               i_data( 21 ) when "10101",
               i_data( 22 ) when "10110",
               i_data( 23 ) when "10111",
               i_data( 24 ) when "11000",
               i_data( 25 ) when "11001",
               i_data( 26 ) when "11010",
               i_data( 27 ) when "11011",
               i_data( 28 ) when "11100",
               i_data( 29 ) when "11101",
               i_data( 30 ) when "11110",
               i_data( 31 ) when "11111",
               x"FFFFFFFF" when others; --this shouldn't ever happen, so needs nothing
                
end dataflow;