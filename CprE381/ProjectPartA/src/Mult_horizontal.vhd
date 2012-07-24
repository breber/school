-------------------------------------------------------------------------
-- Group 9
-- Project Part A
-- Lab Date: 09/28/2011 + 10/5/2011
-------------------------------------------------------------------------


-- Mult_horizontal.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is the horizontal component to the array multiplier,
--              that is that it is each horizontal row in the cascading
--              schematic.
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;

entity  mult_horizontal is

  port( i_X           :   in  std_logic_vector( 31 downto 0 );
        i_Y           :   in  std_logic;
        i_prev_level  :   in  std_logic_vector( 31 downto 0 );
        o_next_level  :   out std_logic_vector( 31 downto 0 );
        o_ResultBit   :   out std_logic );

end mult_horizontal;


architecture structrual of mult_horizontal is

  ---------------------------------------------------------
  --     Components:

  -- Full Adder
  component fullAdder is
      port(i_A       :   in   std_logic;
           i_B       :   in   std_logic;
           i_CryIn   :   in   std_logic;
           o_Sum     :   out  std_logic;
           o_CryOut  :   out  std_logic  );
  end component;

  -- 2 input and
  component and2 is
      port(i_A          : in std_logic;
           i_B          : in std_logic;
           o_F          : out std_logic );
  end component;
  
  ---------------------------------------------------------
  --     Signals:
  signal  s_AfterAnd    :   std_logic_vector( 31 downto 0 );
  signal  s_Carry       :   std_logic_vector( 32 downto 0 );
  signal  s_Result      :   std_logic_vector( 31 downto 0 );

begin
  
  s_Carry( 0 )  <=  '0';
  
  I1: for i in 0 to 31 generate   
    
    g_and_i : and2
    port MAP( i_A   =>    i_Y,
              i_B   =>    i_X( i ),
              O_F   =>    s_AfterAnd( i )  );
              
    g_fulladder_i : fullAdder
    port MAP( i_A       =>    s_AfterAnd( i ),
              i_B       =>    i_prev_level( i ),
              i_CryIn   =>    s_Carry( i ),
              o_Sum     =>    s_Result( i ),
              o_CryOut  =>    s_Carry( i + 1 )  );
    
  end generate;
  
  o_next_level( 30 downto 0 )   <=    s_Result( 31 downto 1 );
  o_next_level( 31 )            <=    s_Carry( 32 );
  o_ResultBit                   <=    s_Result( 0 );
  
end structrual;