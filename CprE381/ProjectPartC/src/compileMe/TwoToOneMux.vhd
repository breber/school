-------------------------------------------------------------------------
-- Scott Connell
-- Lab-02
-- Lab Date: 08/31/2011
-------------------------------------------------------------------------


-- TwoToOneMux.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is a 2:1 MUX implemented structrually.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity mux_2to1 is
  
  port( i_A   :   in   std_logic;
        i_B   :   in   std_logic;
        sel   :   in   std_logic;
        o_R   :   out  std_logic);
        
end mux_2to1;


architecture structure of mux_2to1 is
  
  -- Describe the components that will be used
  
  -- not gate
  component inv is

    port(i_A          : in  std_logic;
         o_F          : out std_logic);

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
  signal  Snot  : std_logic;
  signal  ASnot : std_logic;
  signal  BS    : std_logic;
  
begin
    
    ----------------------------------------------------
    -- Invert Select bit (!S)
    ----------------------------------------------------
    g_Not:  inv
    port MAP( i_A     =>      sel,
              o_F     =>      Snot  );

    ----------------------------------------------------
    -- A + !S
    ----------------------------------------------------
    g_And1:   and2
    port MAP( i_A     =>      i_A,
              i_B     =>      Snot,
              o_F     =>      ASnot );
    
    ----------------------------------------------------
    -- B + S
    ----------------------------------------------------
    g_And2:   and2
    port MAP( i_A     =>      i_B,
              i_B     =>      sel,
              o_F     =>      BS    );
    
    ----------------------------------------------------
    -- (A+!S) | (B+S)
    ----------------------------------------------------
    g_Or:   or2
    port MAP( i_A     =>      ASnot,
              i_B     =>      BS,
              o_F     =>      o_R   );
    
end structure;