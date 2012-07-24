-------------------------------------------------------------------------
-- Scott Connell
-- Lab-02
-- Lab Date: 08/31/2011
-------------------------------------------------------------------------


-- TwoToOneMuxNBit.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is an N-bit 2:1 MUX implemented structrually.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity mux_2to1n is
  
  generic( N  :   integer := 32 );
  port( i_A   :   in   std_logic_vector( N-1 downto 0);
        i_B   :   in   std_logic_vector( N-1 downto 0);
        sel   :   in   std_logic;
        o_R   :   out  std_logic_vector( N-1 downto 0));
        
end mux_2to1n;


architecture structure of mux_2to1n is
  
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
  signal  Snot  : std_logic_vector( N-1 downto 0 );
  signal  ASnot : std_logic_vector( N-1 downto 0 );
  signal  BS    : std_logic_vector( N-1 downto 0 );
  
begin
    
  G1: for i in 0 to N-1 generate
    
    ----------------------------------------------------
    -- Invert Select bit (!S)
    ----------------------------------------------------
    g_Not_i:  inv
    port MAP( i_A     =>      sel,
              o_F     =>      Snot(i)  );

    ----------------------------------------------------
    -- A + !S
    ----------------------------------------------------
    g_And1_i:   and2
    port MAP( i_A     =>      i_A(i),
              i_B     =>      Snot(i),
              o_F     =>      ASnot(i) );
    
    ----------------------------------------------------
    -- B + S
    ----------------------------------------------------
    g_And2_i:   and2
    port MAP( i_A     =>      i_B(i),
              i_B     =>      sel,
              o_F     =>      BS(i)    );
    
    ----------------------------------------------------
    -- (A+!S) | (B+S)
    ----------------------------------------------------
    g_Or_i:   or2
    port MAP( i_A     =>      ASnot(i),
              i_B     =>      BS(i),
              o_F     =>      o_R(i)   );
              
  end generate;
    
end structure;