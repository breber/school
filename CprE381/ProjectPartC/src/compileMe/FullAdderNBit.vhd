-------------------------------------------------------------------------
-- Scott Connell
-- Lab-02
-- Lab Date: 08/31/2011
-------------------------------------------------------------------------


-- FullAdderNBit.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is a 3-input full adder.  With 2 operands and one
--              carry-in.  It outputs a sum and a carry-out. N-bit. Structrual
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity fullAdder_n is
  
  generic( N      :   integer := 32 );
  port( i_A       :   in   std_logic_vector( N-1 downto 0 );
        i_B       :   in   std_logic_vector( N-1 downto 0 );
        i_CryIn   :   in   std_logic;
        o_Sum     :   out  std_logic_vector( N-1 downto 0 );
        o_CryOut  :   out  std_logic);
        
end fullAdder_n;


architecture structure of fullAdder_n is
  
  -- Describe the components that will be used
  
  -- xor gate
  component xor2 is
    
   port(i_A         : in  std_logic;
        i_B         : in  std_logic;
        o_F         : out std_logic);

  end component;
  
  -- and gate
  component and2 is

   port(i_A          : in  std_logic;
        i_B          : in  std_logic;
        o_F          : out std_logic);

  end component;

  -- or gate
  component or2 is

    port(i_A          : in  std_logic;
         i_B          : in  std_logic;
         o_F          : out std_logic);

  end component;
  
  component fulladder is

   port(  i_A       :   in   std_logic;
          i_B       :   in   std_logic;
          i_CryIn   :   in   std_logic;
          o_Sum     :   out  std_logic;
          o_CryOut  :   out  std_logic);
        
  end component;
  
  -- signals to store the logic
  signal  ABxored   : std_logic_vector( N-1 downto 0 );
  signal  ABanded   : std_logic_vector( N-1 downto 0 );
  signal  cryABx    : std_logic_vector( N-1 downto 0 );
  
  signal carries    : std_logic_vector( N-1 downto 0 );
  
begin
    
    g_fa_0:   fulladder
    port MAP( i_A      =>    i_A(0),
              i_B      =>    i_B(0),
              i_CryIn  =>    i_CryIn,
              o_Sum    =>    o_Sum(0),
              o_CryOut =>    carries(0));
    
  G1: for i in 1 to N-1 generate
    
    
    g_fa_i:   fulladder
    port MAP( i_A      =>    i_A(i),
              i_B      =>    i_B(i),
              i_CryIn  =>    carries( i - 1 ),
              o_Sum    =>    o_Sum(i),
              o_CryOut =>    carries(i));
    
    
--  
--    ----------------------------------------------------
--    -- A xor B
--    ----------------------------------------------------
--    g_Xor1_i:  xor2
--    port MAP( i_A     =>      i_A(i),
--              i_B     =>      i_B(i),
--              o_F     =>      ABxored(i)    );
--
--    ----------------------------------------------------
--    -- (AxorB) xor CryIn = sum
--    ----------------------------------------------------
--    g_Xor2_i:   xor2
--    port MAP( i_A     =>      i_CryIn,
--              i_B     =>      ABxored(i),
--              o_F     =>      o_Sum(i)      );
--    
--    ----------------------------------------------------
--    -- A + B
--    ----------------------------------------------------
--    g_And1_i:   and2
--    port MAP( i_A     =>      i_A(i),
--              i_B     =>      i_B(i),
--              o_F     =>      ABanded(i)    );
--    
--    ----------------------------------------------------
--    -- CryIn + (AxorB)
--    ----------------------------------------------------
--    g_And2_i:   and2
--    port MAP( i_A     =>      ABxored(i),
--              i_B     =>      i_CryIn,
--              o_F     =>      cryABx(i)     );
--                  
--    ----------------------------------------------------
--    -- (A+B) | [(AxorB) + CryIn]
--    ----------------------------------------------------
--    g_Or1_i:   or2
--    port MAP( i_A     =>      cryABx(i),
--              i_B     =>      ABanded(i),
--              o_F     =>      o_CryOut   );
              
  end generate;
  
  o_CryOut    <=    carries(N-1);
    
end structure;