-------------------------------------------------------------------------
-- Brian Reber
-- Iowa State University
-------------------------------------------------------------------------


-- Einstein.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This file contains an implementation of Einstein's
-- equation E = mc^2 using invidual adder and multiplier units.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity Einstein is

  port(iCLK             : in std_logic;
       iM               : in integer;
       oE               : out integer);

end Einstein;

architecture structure of Einstein is
  
  -- Describe the component entities as defined in 
  -- Multiplier.vhd (not strictly necessary).
  component Multiplier
    port(iCLK           : in std_logic;
         iA             : in integer;
         iB             : in integer;
         oC             : out integer);
  end component;

  -- C constant
  constant cC : integer := 9487;

  -- Signal to store c^2
  signal sVALUE_c2 : integer;

begin

  ---------------------------------------------------------------------------
  -- Calculate c^2
  ---------------------------------------------------------------------------
  g_Mult1: Multiplier
    port MAP(iCLK             => iCLK,
             iA               => cC,
             iB               => cC,
             oC               => sVALUE_c2);

  g_Mult2: Multiplier
    port MAP(iCLK             => iCLK,
             iA               => sVALUE_c2,
             iB               => iM,
             oC               => oE);
  
end structure;
