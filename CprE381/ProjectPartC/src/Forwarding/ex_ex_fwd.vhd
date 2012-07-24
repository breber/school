




library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;
entity ex_ex_fwd is

  port( 
        i_EXMEM_RegWrite       :   in  std_logic;
        i_EXMEM_rd             :   in  std_logic_vector(4 downto 0);
        i_IDEX_rs              :   in  std_logic_vector(4 downto 0);
        i_IDEX_rt              :   in  std_logic_vector(4 downto 0);
        o_EXDataOne                 :   out std_logic_vector( 1 downto 0 );
        o_EXDataTwo                 :   out std_logic_vector( 1 downto 0 )    ); 
end ex_ex_fwd;

architecture dataflow of ex_ex_fwd is


begin
  
  
  o_EXDataTwo <=  "01" after 1 ns when i_EXMEM_RegWrite = '1' and i_EXMEM_rd = i_IDEX_rt and i_EXMEM_rd /= "00000"
                  else  "10" after 1 ns when i_EXMEM_RegWrite = '0'
                  else  "00" after 1 ns;
  o_EXDataOne <=  "01" after 1 ns when i_EXMEM_RegWrite = '1' and i_EXMEM_rd = i_IDEX_rs and i_EXMEM_rd /= "00000"
        else  "00" after 1 ns;
        
end dataflow;