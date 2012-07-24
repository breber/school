-------------------------------------------------------------------------
-- Group 9
-- Project Part C
-- Due December 8th, 2011
-------------------------------------------------------------------------

-- tb_ForwardingUnit.vhd
-------------------------------------------------------------------------
-- DESCRIPTION: This is the unit that deals with control hazards, and
--              detects hazards.
-------------------------------------------------------------------------
library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;


entity tb_exexfwd is
  
end tb_exexfwd;




architecture behavior of tb_exexfwd is

component ex_ex_fwd
  port( i_EXMEM_RegWrite       :   in  std_logic;
        i_EXMEM_rd             :   in  std_logic_vector(4 downto 0);
        i_IDEX_rs              :   in  std_logic_vector(4 downto 0);
        i_IDEX_rt              :   in  std_logic_vector(4 downto 0);
        o_EXDataOne            :   out std_logic_vector( 1 downto 0 );
        o_EXDataTwo            :   out std_logic_vector( 1 downto 0 )    ); 
end component;

  signal s_EXMEM_RegWrite                       :   std_logic;
  signal s_EXMEM_rd, s_IDEX_rs, s_IDEX_rt   :   std_logic_vector ( 4 downto  0 );
  signal s_EXDataOne, s_EXDataTwo                             :   std_logic_vector ( 1 downto 0 );

begin
  
  testmeplz : ex_ex_fwd
  port MAP( i_EXMEM_RegWrite   =>    s_EXMEM_RegWrite,
            i_EXMEM_rd         =>    s_EXMEM_rd,
            i_IDEX_rs          =>    s_IDEX_rs,
            i_IDEX_rt          =>    s_IDEX_rt,
            o_EXDataOne        =>    s_EXDataOne,
            o_EXDataTwo        =>    s_EXDataTwo    );
            
   
   
   process
     begin
   
            
  -------------------------
  -- EX --> EX
  -------------------------
  report "EX->EX";
  -- EX/MEM.RD == ID/EX.RS
  s_EXMEM_RegWrite <=  '1';
  s_EXMEM_rd       <=  "00001";
  s_IDEX_rs        <=  "00001";
  s_IDEX_rt        <=  "00000";
  wait for 100 ns;
  
  -- MEM/ID.RD == IF/ID.RT
  s_EXMEM_RegWrite <=  '1';
  s_EXMEM_rd       <=  "00001";
  s_IDEX_rt        <=  "00001";
  s_IDEX_rs        <=  "00000";
  wait for 100 ns;
  
  -- MEM/ID.RD == IF/ID.RT && MEM/ID.RD == IF/ID.RS
  s_EXMEM_RegWrite <=  '1';
  s_EXMEM_rd       <=  "00001";
  s_IDEX_rt        <=  "00001";
  s_IDEX_rs        <=  "00001";
  wait for 100 ns;
  
  -- IF/ID.RegWrite == 0
  s_EXMEM_RegWrite <=  '0';
  s_EXMEM_rd       <=  "00001";
  s_IDEX_rs        <=  "00001";
  s_IDEX_rt        <=  "00000";
  wait for 100 ns;
  
  -- MEM/ID.RD != IF/ID.RS
  s_EXMEM_RegWrite <=  '1';
  s_EXMEM_rd       <=  "00001";
  s_IDEX_rs        <=  "00010";
  wait for 100 ns;
  
  -- MEM/ID.RD != IF/ID.RT
  s_EXMEM_RegWrite <=  '1';
  s_EXMEM_rd       <=  "00001";
  s_IDEX_rs        <=  "00010";
  wait for 100 ns;
            
end process;          
  
end behavior;