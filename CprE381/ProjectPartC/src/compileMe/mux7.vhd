-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Lab Date: 09/28/2011 + 10/5/2011
-------------------------------------------------------------------------


-- mux7.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an implementation a seven to one mux 
-- using structural VHDL
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;

entity mux7 is
  
  port(i_A  : in  std_logic;
       i_B  : in  std_logic;
       i_C  : in  std_logic;
       i_D  : in  std_logic;
       i_E  : in  std_logic;
       i_F  : in  std_logic;
       i_G  : in  std_logic;
       i_Sel: in  std_logic_vector(2 downto 0);
       o_F  : out std_logic);
       
end mux7;


architecture dataflow of mux7 is

begin

  with i_sel select
  o_F <=   i_A when "000",   --0
           i_B when "001",   --1 
           i_C when "010",   --2
           i_D when "011",   --3 
           i_E when "100",   --4
           i_F when "101",   --5
           i_G when others;  --6
        
end dataflow;
