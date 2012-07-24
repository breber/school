-------------------------------------------------------------------------
-- Scott Connell
-- Lab-02
-- Lab Date: 08/31/2011
-------------------------------------------------------------------------


-- onesComplimenter.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: An N-bit one's complimenter. For integers.
--              Uses the structrual approach.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;

entity ones_complimenter is
  
  generic( N  : integer := 32 );  --N is configurable here. For now N=32
  port   ( i_A  : in  std_logic_vector( N-1 downto 0 );
           o_F  : out std_logic_vector( N-1 downto 0 ) );

end ones_complimenter;


architecture structure of ones_complimenter is

  -- Describe the component entities as defined in inv.vhd 
  component inv is

    port(i_A          : in std_logic;
         o_F          : out std_logic);

  end component;
  
begin
  
  -- Send to the inverter by looping through
  G1: for i in 0 to N-1 generate
  inv_i : inv 
    port map(i_A  => i_A(i),
  	          o_F  => o_F(i));
  end generate;

end structure;