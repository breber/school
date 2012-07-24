-------------------------------------------------------------------------
-- Scott Connell
-- Lab-02
-- Lab Date: 08/31/2011
-------------------------------------------------------------------------


-- FullAdder.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is a 3-input full adder.  With 2 operands and one
--              carry-in.  It outputs a sum and a carry-out. Structrually.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity fullAdder is
  
  port( i_A       :   in   std_logic;
        i_B       :   in   std_logic;
        i_CryIn   :   in   std_logic;
        o_Sum     :   out  std_logic;
        o_CryOut  :   out  std_logic);
        
end fullAdder;


architecture structure of fullAdder is
  
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
  
  -- signals to store the logic
  signal  ABxored   : std_logic;
  signal  ABanded   : std_logic;
  signal  cryABx    : std_logic;
  
  
begin
    
    ----------------------------------------------------
    -- A xor B
    ----------------------------------------------------
    g_Xor1:  xor2
    port MAP( i_A     =>      i_A,
              i_B     =>      i_B,
              o_F     =>      ABxored    );

    ----------------------------------------------------
    -- (AxorB) xor CryIn = sum
    ----------------------------------------------------
    g_Xor2:   xor2
    port MAP( i_A     =>      i_CryIn,
              i_B     =>      ABxored,
              o_F     =>      o_Sum      );
    
    ----------------------------------------------------
    -- A + B
    ----------------------------------------------------
    g_And1:   and2
    port MAP( i_A     =>      i_A,
              i_B     =>      i_B,
              o_F     =>      ABanded    );
    
    ----------------------------------------------------
    -- CryIn + (AxorB)
    ----------------------------------------------------
    g_And2:   and2
    port MAP( i_A     =>      ABxored,
              i_B     =>      i_CryIn,
              o_F     =>      cryABx     );
                  
    ----------------------------------------------------
    -- (A+B) | [(AxorB) + CryIn]
    ----------------------------------------------------
    g_Or1:   or2
    port MAP( i_A     =>      cryABx,
              i_B     =>      ABanded,
              o_F     =>      o_CryOut   );
    
end structure;