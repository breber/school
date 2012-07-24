-------------------------------------------------------------------------
-- Group 9
-- Project Part A 
-- Lab Date: 09/28/11 +10/5/2011
-------------------------------------------------------------------------

-- Array_Multiplier.vhd
-------------------------------------------------------------------------
-- DESCRIPTION:  This is our Array Muliplier it will take 32 bit vectors
-- 
--
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;

entity ArrayMult is
  port(i_x  : in std_logic_vector(31 downto 0);     
       i_y  : in std_logic_vector(31 downto 0);
       o_F  : out std_logic_vector(31 downto 0));
 end ArrayMult;


architecture structural of ArrayMult  is

component mult_horizontal

  port( i_X           :   in  std_logic_vector( 31 downto 0 );
        i_Y           :   in  std_logic;
        i_prev_level  :   in  std_logic_vector( 31 downto 0 );
        o_next_level  :   out std_logic_vector( 31 downto 0 );
        o_resultbit   :   out std_logic );

end component;

component and2
  port( i_A   :   in  std_logic;
        i_B   :   in  std_logic;
        o_F   :   out std_logic );
end component;

signal s_first          : std_logic_vector(31 downto 0);
signal s_F              : std_logic_vector( 63 downto 0 );
signal s_prev_level     : array32_bit(31 downto 1);
signal s_tempFirst      : std_logic_vector( 31 downto 0 );


begin 
 G1: for i in 0 to 31 generate
 and_i: and2
   port map(i_A => i_X(i),
            i_B => i_Y(0),
            o_F => s_first(i));
  end generate;
 
 s_F(0) <= s_first(0);
 s_tempFirst  <=  '0' & s_first( 31 downto 1 );
 
 mult_0 : mult_horizontal
 port map( i_X => i_X,
           i_Y => i_Y(1),
           i_prev_level => s_tempFirst,
           o_next_level => s_prev_level(1),
           o_resultbit => s_F(1));
 
G2: for i in 1 to 30 generate
  mult_i :mult_horizontal 
  port map(i_X => i_X,
           i_Y => i_Y(i+1),
           i_prev_level =>  s_prev_level(i),
           o_next_level =>  s_prev_level(i+1),
           o_resultbit => s_F(i+1));
  end generate;
  
  s_F( 63 downto 32 ) <= s_prev_level(31);
    
  -- CHOP OFF 32 bits
  o_F   <=    s_F( 31 downto 0 );
  
end structural;