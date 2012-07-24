-------------------------------------------------------------------------
-- Group 9
-- Project Part A 
-- Lab Date: 09/28/11 +10/5/2011
-------------------------------------------------------------------------

-- barrel_shifter.vhd
-------------------------------------------------------------------------
-- DESCRIPTION:  This is our barrel shifter :)
-- 
--
-------------------------------------------------------------------------


library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;

entity barrel_shifter is
  port(i_in         : in std_logic_vector(31 downto 0);     
       shiftAmount  : in std_logic_vector(4 downto 0);
       log_arith    : in std_logic;--- 0=logical 1= arithmatic
       leftOrRight  : in std_logic;--- 0=left    1= right
       o_F          : out std_logic_vector(31 downto 0));
 end barrel_shifter;



architecture structural of barrel_shifter is

component mux_2to1n
  generic( N  :   integer := 32 );
  port( i_A   :   in   std_logic_vector( N-1 downto 0);
        i_B   :   in   std_logic_vector( N-1 downto 0);
        sel   :   in   std_logic;
        o_R   :   out  std_logic_vector( N-1 downto 0));
       
    end component;
  



component mux_2to1 
  port( i_A   :   in   std_logic;
        i_B   :   in   std_logic;
        sel   :   in   std_logic;
        o_R   :   out  std_logic);
       
end component;

signal s_way, s_one_F, s_two_F, s_three_F, s_four_F   : std_logic_vector(31 downto 0);
signal s_init_F                                  : std_logic;
signal s_F_premux     :   std_logic_vector( 31 downto 0 );
signal s_F_premuxFLIP :   std_logic_vector( 31 downto 0 );

signal s_right : std_logic_vector( 31 downto 0 );


begin 
  
  
  G0: for a in 0 to 31 generate
    s_right(a) <= i_In(31-a);
  end generate;
  
  
  
  
  whichwayquestionmark: mux_2to1n
  port map( i_A => i_In( 31 downto 0),  ----left shifting
            i_B => s_right,  ----right shifting
            sel => leftOrRight,
            o_R => s_way);
  
  
  initmux : mux_2to1
  port map(i_A => '0',
           i_B => i_In(31),
           sel => log_arith,
           o_R => s_init_F);

 
 ---------------------------------------
 --            COLUMN 1  
 ---------------------------------------  
  muxOne_0: mux_2to1
  port map( i_A => s_way(0),
            i_B => s_init_F,
            sel => shiftAmount(0),
            o_R => s_one_F(0));
  
  
 G1: for i in 1 to 31 generate
   muxOne_i: mux_2to1
   port map(i_A => s_way(i),
            i_B => s_way(i-1),
            sel => shiftAmount(0),
            o_R => s_one_F(i));
 end generate;

 ---------------------------------------
 --             COLUMN 2  
 --------------------------------------- 
 
sub_G2 : for w in 0 to 1 generate
        muxtwo_w: mux_2to1
        port map( i_A => s_one_F(w),
                  i_B => s_init_F,
                  sel => shiftAmount(1),
                  o_R => s_two_F(w));
 end generate;

G2: for j in 2 to 31 generate
   muxtwo_j: mux_2to1
   port map(i_A => s_one_F(j),
            i_B => s_one_F(j-2),
            sel => shiftAmount(1),
            o_R => s_two_F(j));
 end generate;
 
---------------------------------------
--             COLUMN 3  
---------------------------------------  

sub_G3 : for x in 0 to 3 generate
        muxthree_x: mux_2to1
        port map( i_A => s_two_F(x),
                  i_B => s_init_F,
                  sel => shiftAmount(2),
                  o_R => s_three_F(x));
  end generate;
 
 G3: for k in 4 to 31 generate
      muxthree_x: mux_2to1
      port map( i_A => s_two_F(k),
                i_B => s_two_F(k-4),
                sel => shiftAmount(2),
                o_R => s_three_F(k));
  end generate;
 
 
 
---------------------------------------
--             COLUMN 4  
---------------------------------------  
 
 sub_G4 : for y in 0 to 7 generate
        muxfour_y: mux_2to1
        port map( i_A => s_three_F(y),
                  i_B => s_init_F,
                  sel => shiftAmount(3),
                  o_R => s_four_F(y));
   end generate;                
                  
                  
                
  G4: for l in 8 to 31 generate
      muxfour_l: mux_2to1
      port map( i_A => s_three_F(l),
                i_B => s_three_F(l-8),
                sel => shiftAmount(3),
                o_R => s_four_F(l));
                
   end generate;
 
 
---------------------------------------
--             COLUMN 5 
---------------------------------------  
 
 
  sub_G5 : for z in 0 to 15 generate
        muxfive_z: mux_2to1
        port map( i_A => s_four_F(z),
                  i_B => s_init_F,
                  sel => shiftAmount(4),
                  o_R => s_F_premux(z));
                  
 end generate;
                
  G5: for m in 16 to 31 generate
      muxfive_m: mux_2to1
      port map( i_A => s_four_F(m),
                i_B => s_four_F(m-16),
                sel => shiftAmount(4),
                o_R => s_F_premux(m));
  end generate;
  
  G6: for t in 0 to 31 generate
      s_F_premuxFLIP( t ) <= s_F_premux( 31 - t );
  end generate;
  
  muxDone:  mux_2to1n
  port MAP( i_A   =>    s_F_premux,
            i_B   =>    s_F_premuxFLIP,
            sel   =>    leftOrRight,
            o_R   =>    o_F );
  
  end structural;