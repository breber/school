-------------------------------------------------------------------------
-- Group 9  :  Scott Connell, Brian Reber, Arjay Vander Velden
-- Project Part C
-- Due: December 8th, 2011
-------------------------------------------------------------------------

-- tb_Pipeline_Registers.vhd
-------------------------------------------------------------------------
-- DESCRIPTION:  This is the Pipeline_Registers test bench, this shows
-- that the values stored in the inital IF/ID register are available as 
-- expected four cycles later, also that new values can be inserted into 
-- pipeline every single cycle. The last thing we will test is the 
--ability to individually stall or flush a register.
--               
-------------------------------------------------------------------------


library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;


entity tb_Pipeline_Registers is

end tb_Pipeline_Registers;

architecture behavioral of tb_Pipeline_Registers is




Component Pipeline_Registers is
  port(   i_clk             :     in std_logic;
          i_stall           :     in std_logic;
          i_flush           :     in std_logic;
          i_instruction     :     in std_logic_vector( 31 downto 0 );
          i_ALU_op          :     in std_logic_vector( 2 downto 0 );
          i_ALU_src         :     in std_logic;
          i_log_arith       :     in std_logic;
          i_leftOrRight     :     in std_logic;
          i_IsJump          :     in std_logic;
          i_IsBranch        :     in std_logic;
          i_RegWrite        :     in std_logic;
          i_MemWrite        :     in std_logic;
          i_MemRead         :     in std_logic;
          i_MemToReg        :     in std_logic_vector( 1 downto 0 );
          i_RegDest         :     in std_logic_vector( 1 downto 0 );
          i_W_H_B           :     in std_logic_vector( 2 downto 0 );
          i_WhichJB         :     in std_logic_vector( 3 downto 0 );
          i_shftAmount      :     in std_logic_vector( 1 downto 0 );
          i_isLui           :     in std_logic;
          i_NeedsLink       :     in std_logic;
          i_RegValue        :     in std_logic_vector( 31 downto 0 );
          i_RF_op_2         :     in std_logic_vector( 31 downto 0 );
          i_sextend_op      :     in std_logic_vector( 31 downto 0 );
          i_RS              :     in std_logic_vector( 4  downto 0 );
          i_RT              :     in std_logic_vector( 4  downto 0 );
          i_RD              :     in std_logic_vector( 4  downto 0 );
          i_EXResult        :     in std_logic_vector( 31 downto 0 );
          i_fromLoad        :     in std_logic_vector( 31 downto 0 );
          i_nextPC          :     in std_logic_vector( 31 downto 0 );
          o_instruction     :     out std_logic_vector( 31 downto 0 );
          o_ALU_op          :     out std_logic_vector( 2 downto 0 );
          o_ALU_src         :     out std_logic;
          o_log_arith       :     out std_logic;
          o_leftOrRight     :     out std_logic;
          o_IsJump          :     out std_logic;
          o_IsBranch        :     out std_logic;
          o_RegWrite        :     out std_logic;
          o_MemWrite        :     out std_logic;
          o_MemRead         :     out std_logic;
          o_MemToReg        :     out std_logic_vector( 1 downto 0 );
          o_RegDest         :     out std_logic_vector( 1 downto 0 );
          o_W_H_B           :     out std_logic_vector( 2 downto 0 );
          o_WhichJB         :     out std_logic_vector( 3 downto 0 );
          o_shftAmount      :     out std_logic_vector( 1 downto 0 );
          o_isLui           :     out std_logic;
          o_NeedsLink       :     out std_logic;
          o_RegValue        :     out std_logic_vector( 31 downto 0 );
          o_RF_op_2         :     out std_logic_vector( 31 downto 0 );
          o_sextend_op      :     out std_logic_vector( 31 downto 0 );
          o_RS              :     out std_logic_vector( 4  downto 0 );
          o_RT              :     out std_logic_vector( 4  downto 0 );
          o_RD              :     out std_logic_vector( 4  downto 0 );
          o_EXResult        :     out std_logic_vector( 31 downto 0 );
          o_fromLoad        :     out std_logic_vector( 31 downto 0 );
          o_nextPC          :     out std_logic_vector( 31 downto 0 )     );

end Component;



Type array_332 is array( 2 downto 0) of std_logic_vector(31 downto 0);
Type array_35 is array( 2 downto 0) of std_logic_vector(4 downto 0);
Type array_34 is array( 2 downto 0) of std_logic_vector(3 downto 0);
Type array_33 is array( 2 downto 0) of std_logic_vector(2 downto 0);
Type array_32 is array( 2 downto 0) of std_logic_vector(1 downto 0);
Type array_41 is array( 3 downto 0) of std_logic;
Type array_31 is array( 2 downto 0) of std_logic;



            
       
signal so_instruction, so_RegValue, so_RF_op_2, so_sextend_op, 
       so_EXResult, so_fromLoad, 
       so_nextPC                                           : array_332;

signal  so_RS, so_RT, so_RD                                 : array_35;

signal  so_WhichJB                                          : array_34;

signal so_ALU_op,so_W_H_B                                   : array_33;

signal so_MemToReg, so_RegDest,so_shftAmount               : array_32;

signal s_stall, s_flush                                     : array_41;
       
signal so_ALU_src, so_log_arith, so_leftOrRight, so_IsJump,
       so_IsBranch, so_RegWrite, so_MemWrite, so_MemRead, so_isLui,
       so_NeedsLink                                         : array_31;

signal s_instruction, s_RegValue, s_RF_op_2, s_sextend_op, 
       s_EXResult, s_shift_op, s_Mult_op, s_fromLoad, s_nextPC  
                                        :std_logic_vector(31 downto 0);

signal s_RS, s_RT, s_RD                 :std_logic_vector( 4 downto 0);

signal s_WhichJB                        :std_logic_vector( 3 downto 0);

signal s_ALU_op, s_W_H_B                :std_logic_vector( 2 downto 0);

signal s_MemToReg, s_RegDest, s_shftAmount
                                        :std_logic_vector( 1 downto 0);

signal s_ALU_src, s_log_arith, s_leftOrRight, 
       s_IsJump, s_IsBranch, s_RegWrite, s_MemWrite, s_MemRead, 
       s_isLui, s_NeedsLink, s_clk                           :std_logic;
       
signal sf_instruction, sf_RegValue, sf_RF_op_2, sf_sextend_op, 
       sf_EXResult, sf_shift_op, sf_Mult_op, sf_fromLoad, sf_nextPC  
                                        :std_logic_vector(31 downto 0);

signal sf_RS, sf_RT, sf_RD                 :std_logic_vector( 4 downto 0);

signal sf_WhichJB                        :std_logic_vector( 3 downto 0);

signal sf_ALU_op, sf_W_H_B                :std_logic_vector( 2 downto 0);

signal sf_MemToReg, sf_RegDest, sf_shftAmount
                                        :std_logic_vector( 1 downto 0);

signal sf_ALU_src, sf_log_arith, sf_leftOrRight, 
       sf_IsJump, sf_IsBranch, sf_RegWrite, sf_MemWrite, sf_MemRead, 
       sf_isLui, sf_NeedsLink                            :std_logic;

begin
  IFslashID: Pipeline_Registers 

  port map(  i_clk           => s_clk,
             i_stall         => s_stall(0),
             i_flush         => s_flush(0),
             i_instruction   => s_instruction,
             i_ALU_op        => s_ALU_op,
             i_ALU_src       => s_ALU_src,
             i_log_arith     => s_log_arith,
             i_leftOrRight   => s_leftOrRight,
             i_IsJump        => s_IsJump,
             i_IsBranch      => s_IsBranch,
             i_RegWrite      => s_RegWrite,
             i_MemWrite      => s_MemWrite,
             i_MemRead       => s_MemRead,
             i_MemToReg      => s_MemToReg,
             i_RegDest       => s_RegDest,
             i_W_H_B         => s_W_H_B,
             i_WhichJB       => s_WhichJB,
             i_shftAmount    => s_shftAmount,
             i_isLui         => s_isLui,
             i_NeedsLink     => s_NeedsLink,
             i_RegValue      => s_RegValue,
             i_RF_op_2       => s_RF_op_2,
             i_sextend_op    => s_sextend_op,
             i_RS            => s_RS,
             i_RT            => s_RT,
             i_RD            => s_RD,
             i_EXResult     => s_EXResult,
             i_fromLoad      => s_fromLoad,
             i_nextPC        => s_nextPC,
             o_instruction   => so_instruction(0),
             o_ALU_op        => so_ALU_op(0),
             o_ALU_src       => so_ALU_src(0),
             o_log_arith     => so_log_arith(0),
             o_leftOrRight   => so_leftOrRight(0),
             o_IsJump        => so_IsJump(0),
             o_IsBranch      => so_IsBranch(0),
             o_RegWrite      => so_RegWrite(0),
             o_MemWrite      => so_MemWrite(0),
             o_MemRead       => so_MemRead(0),
             o_MemToReg      => so_MemToReg(0),
             o_RegDest       => so_RegDest(0),
             o_W_H_B         => so_W_H_B(0),
             o_WhichJB       => so_WhichJB(0),
             o_shftAmount    => so_shftAmount(0),
             o_isLui         => so_isLui(0),
             o_NeedsLink     => so_NeedsLink(0),
             o_RegValue      => so_RegValue(0),
             o_RF_op_2       => so_RF_op_2(0),
             o_sextend_op    => so_sextend_op(0),
             o_RS            => so_RS(0),
             o_RT            => so_RT(0),
             o_RD            => so_RD(0),
             o_EXResult     => so_EXResult(0),
             o_fromLoad      => so_fromLoad(0),
             o_nextPC        => so_nextPc(0)  );
  	        
IDslashEX: Pipeline_Registers 

  port map(  i_clk           => s_clk,
             i_stall         => s_stall(1),
             i_flush         => s_flush(1),
             i_instruction   => so_instruction(0),
             i_ALU_op        => so_ALU_op(0),
             i_ALU_src       => so_ALU_src(0),
             i_log_arith     => so_log_arith(0),
             i_leftOrRight   => so_leftOrRight(0),
             i_IsJump        => so_IsJump(0),
             i_IsBranch      => so_IsBranch(0),
             i_RegWrite      => so_RegWrite(0),
             i_MemWrite      => so_MemWrite(0),
             i_MemRead       => so_MemRead(0),
             i_MemToReg      => so_MemToReg(0),
             i_RegDest       => so_RegDest(0),
             i_W_H_B         => so_W_H_B(0),
             i_WhichJB       => so_WhichJB(0),
             i_shftAmount    => so_shftAmount(0),
             i_isLui         => so_isLui(0),
             i_NeedsLink     => so_NeedsLink(0),
             i_RegValue      => so_RegValue(0),
             i_RF_op_2       => so_RF_op_2(0),
             i_sextend_op    => so_sextend_op(0),
             i_RS            => so_RS(0),
             i_RT            => so_RT(0),
             i_RD            => so_RD(0),
             i_EXResult     => so_EXResult(0),
             i_fromLoad      => so_fromLoad(0),
             i_nextPC        => so_nextPC(0),
             o_instruction   => so_instruction(1),
             o_ALU_op        => so_ALU_op(1),
             o_ALU_src       => so_ALU_src(1),
             o_log_arith     => so_log_arith(1),
             o_leftOrRight   => so_leftOrRight(1),
             o_IsJump        => so_IsJump(1),
             o_IsBranch      => so_IsBranch(1),
             o_RegWrite      => so_RegWrite(1),
             o_MemWrite      => so_MemWrite(1),
             o_MemRead       => so_MemRead(1),
             o_MemToReg      => so_MemToReg(1),
             o_RegDest       => so_RegDest(1),
             o_W_H_B         => so_W_H_B(1),
             o_WhichJB       => so_WhichJB(1),
             o_shftAmount    => so_shftAmount(1),
             o_isLui         => so_isLui(1),
             o_NeedsLink     => so_NeedsLink(1),
             o_RegValue      => so_RegValue(1),
             o_RF_op_2       => so_RF_op_2(1),
             o_sextend_op    => so_sextend_op(1),
             o_RS            => so_RS(1),
             o_RT            => so_RT(1),
             o_RD            => so_RD(1),
             o_EXResult     => so_EXResult(1),
             o_fromLoad      => so_fromLoad(1),
             o_nextPC        => so_nextPc(1)  );  
             
 EXslashMem: Pipeline_Registers 

  port map(  i_clk           => s_clk,
             i_stall         => s_stall(2),
             i_flush         => s_flush(2),
             i_instruction   => so_instruction(1),
             i_ALU_op        => so_ALU_op(1),
             i_ALU_src       => so_ALU_src(1),
             i_log_arith     => so_log_arith(1),
             i_leftOrRight   => so_leftOrRight(1),
             i_IsJump        => so_IsJump(1),
             i_IsBranch      => so_IsBranch(1),
             i_RegWrite      => so_RegWrite(1),
             i_MemWrite      => so_MemWrite(1),
             i_MemRead       => so_MemRead(1),
             i_MemToReg      => so_MemToReg(1),
             i_RegDest       => so_RegDest(1),
             i_W_H_B         => so_W_H_B(1),
             i_WhichJB       => so_WhichJB(1),
             i_shftAmount    => so_shftAmount(1),
             i_isLui         => so_isLui(1),
             i_NeedsLink     => so_NeedsLink(1),
             i_RegValue      => so_RegValue(1),
             i_RF_op_2       => so_RF_op_2(1),
             i_sextend_op    => so_sextend_op(1),
             i_RS            => so_RS(1),
             i_RT            => so_RT(1),
             i_RD            => so_RD(1),
             i_EXResult     => so_EXResult(1),
             i_fromLoad      => so_fromLoad(1),
             i_nextPC        => so_nextPC(1),
             o_instruction   => so_instruction(2),
             o_ALU_op        => so_ALU_op(2),
             o_ALU_src       => so_ALU_src(2),
             o_log_arith     => so_log_arith(2),
             o_leftOrRight   => so_leftOrRight(2),
             o_IsJump        => so_IsJump(2),
             o_IsBranch      => so_IsBranch(2),
             o_RegWrite      => so_RegWrite(2),
             o_MemWrite      => so_MemWrite(2),
             o_MemRead       => so_MemRead(2),
             o_MemToReg      => so_MemToReg(2),
             o_RegDest       => so_RegDest(2),
             o_W_H_B         => so_W_H_B(2),
             o_WhichJB       => so_WhichJB(2),
             o_shftAmount    => so_shftAmount(2),
             o_isLui         => so_isLui(2),
             o_NeedsLink     => so_NeedsLink(2),
             o_RegValue      => so_RegValue(2),
             o_RF_op_2       => so_RF_op_2(2),
             o_sextend_op    => so_sextend_op(2),
             o_RS            => so_RS(2),
             o_RT            => so_RT(2),
             o_RD            => so_RD(2),
             o_EXResult     => so_EXResult(2),
             o_fromLoad      => so_fromLoad(2),
             o_nextPC        => so_nextPc(2)  );  
             
  MemslashWB: Pipeline_Registers 

  port map(  i_clk           => s_clk,
             i_stall         => s_stall(3),
             i_flush         => s_flush(3),
             i_instruction   => so_instruction(2),
             i_ALU_op        => so_ALU_op(2),
             i_ALU_src       => so_ALU_src(2),
             i_log_arith     => so_log_arith(2),
             i_leftOrRight   => so_leftOrRight(2),
             i_IsJump        => so_IsJump(2),
             i_IsBranch      => so_IsBranch(2),
             i_RegWrite      => so_RegWrite(2),
             i_MemWrite      => so_MemWrite(2),
             i_MemRead       => so_MemRead(2),
             i_MemToReg      => so_MemToReg(2),
             i_RegDest       => so_RegDest(2),
             i_W_H_B         => so_W_H_B(2),
             i_WhichJB       => so_WhichJB(2),
             i_shftAmount    => so_shftAmount(2),
             i_isLui         => so_isLui(2),
             i_NeedsLink     => so_NeedsLink(2),
             i_RegValue      => so_RegValue(2),
             i_RF_op_2       => so_RF_op_2(2),
             i_sextend_op    => so_sextend_op(2),
             i_RS            => so_RS(2),
             i_RT            => so_RT(2),
             i_RD            => so_RD(2),
             i_EXResult     => so_EXResult(2),
             i_fromLoad      => so_fromLoad(2),
             i_nextPC        => so_nextPC(2),
             o_instruction   => sf_instruction,
             o_ALU_op        => sf_ALU_op,
             o_ALU_src       => sf_ALU_src,
             o_log_arith     => sf_log_arith,
             o_leftOrRight   => sf_leftOrRight,
             o_IsJump        => sf_IsJump,
             o_IsBranch      => sf_IsBranch,
             o_RegWrite      => sf_RegWrite,
             o_MemWrite      => sf_MemWrite,
             o_MemRead       => sf_MemRead,
             o_MemToReg      => sf_MemToReg,
             o_RegDest       => sf_RegDest,
             o_W_H_B         => sf_W_H_B,
             o_WhichJB       => sf_WhichJB,
             o_shftAmount    => sf_shftAmount,
             o_isLui         => sf_isLui,
             o_NeedsLink     => sf_NeedsLink,
             o_RegValue      => sf_RegValue,
             o_RF_op_2       => sf_RF_op_2,
             o_sextend_op    => sf_sextend_op,
             o_RS            => sf_RS,
             o_RT            => sf_RT,
             o_RD            => sf_RD,
             o_EXResult     => sf_EXResult,
             o_fromLoad      => sf_fromLoad,
             o_nextPC        => sf_nextPc  );              



process
begin

-------------------------------------------------------------------------
--
-- Show that values that are stored in the initial IF/ID register are 
--available as expected four cycles later.
--
------------------------------------------------------------------------- 

------------------------
-- Flush in beginning --
------------------------
s_flush <= "1111";
wait for 400 ns;
s_flush <= "0000";


---------
--Test #1
---------
s_ALU_src <= '0';
s_log_arith <= '0';
s_leftOrRight <= '0'; 
s_IsJump <= '0';
s_IsBranch <= '0';
s_RegWrite <= '0';
s_MemWrite <= '0';
s_MemRead <= '0'; 
s_isLui <= '0';
s_NeedsLink <= '0';
s_MemToReg <= "00";
s_RegDest <= "00";
s_shftAmount <= "00";
s_ALU_op <= "000";
s_W_H_B <= "000";
s_WhichJB <= "0000";
s_RS <= "00000";
s_RT <= "00000";
s_RD <= "00000";                
s_instruction <= x"00000000";
s_RegValue <= x"00000000";
s_RF_op_2 <= x"00000000";
s_sextend_op <= x"00000000"; 
s_EXResult <= x"00000000"; 
s_shift_op <= x"00000000";
s_Mult_op <= x"00000000"; 
s_fromLoad <= x"00000000"; 
s_nextPC <= x"00000000";
s_stall(0) <= '0';
s_stall(1) <= '0';
s_stall(2) <= '0';
s_stall(3) <= '0';
s_flush(0) <= '0';
s_flush(1) <= '0';
s_flush(2) <= '0';
s_flush(3) <= '0';
wait for 400 ns;

---------
--Test #2
---------
s_ALU_src <= '1';
s_log_arith <= '1';
s_leftOrRight <= '1'; 
s_IsJump <= '1';
s_IsBranch <= '0';
s_RegWrite <= '1';
s_MemWrite <= '1';
s_MemRead <= '1'; 
s_isLui <= '0';
s_NeedsLink <= '0';
s_MemToReg <= "01";
s_RegDest <= "01";
s_shftAmount <= "10";
s_ALU_op <= "101";
s_W_H_B <= "001";
s_WhichJB <= "1000";
s_RS <= "10000";
s_RT <= "10010";
s_RD <= "10011";                
s_instruction <= x"000011a1";
s_RegValue <= x"000000ac";
s_RF_op_2 <= x"11111111";
s_sextend_op <= x"aa000000"; 
s_EXResult <= x"cc000000"; 
s_shift_op <= x"bb000000";
s_Mult_op <= x"11000000"; 
s_fromLoad <= x"00000000"; 
s_nextPC <= x"11110000";
s_stall(0) <= '0';
s_stall(1) <= '0';
s_stall(2) <= '0';
s_stall(3) <= '0';
s_flush(0) <= '0';
s_flush(1) <= '0';
s_flush(2) <= '0';
s_flush(3) <= '0';
wait for 400 ns;



-------------------------------------------------------------------------
--
-- New values can be inserted into the pipeline every single cycle.
--
------------------------------------------------------------------------- 


---------
--Test #3
---------
s_ALU_src <= '0';
s_log_arith <= '0';
s_leftOrRight <= '0'; 
s_IsJump <= '0';
s_IsBranch <= '0';
s_RegWrite <= '0';
s_MemWrite <= '0';
s_MemRead <= '0'; 
s_isLui <= '0';
s_NeedsLink <= '0';
s_MemToReg <= "00";
s_RegDest <= "00";
s_shftAmount <= "00";
s_ALU_op <= "000";
s_W_H_B <= "000";
s_WhichJB <= "0000";
s_RS <= "00000";
s_RT <= "00000";
s_RD <= "00000";                
s_instruction <= x"00000000";
s_RegValue <= x"00000000";
s_RF_op_2 <= x"00000000";
s_sextend_op <= x"00000000"; 
s_EXResult <= x"00000000"; 
s_shift_op <= x"00000000";
s_Mult_op <= x"00000000"; 
s_fromLoad <= x"00000000"; 
s_nextPC <= x"00000000";
s_stall(0) <= '0';
s_stall(1) <= '0';
s_stall(2) <= '0';
s_stall(3) <= '0';
s_flush(0) <= '0';
s_flush(1) <= '0';
s_flush(2) <= '0';
s_flush(3) <= '0';
wait for 100 ns;

s_ALU_src <= '1';
s_log_arith <= '1';
s_leftOrRight <= '1'; 
s_IsJump <= '1';
s_IsBranch <= '1';
s_RegWrite <= '1';
s_MemWrite <= '1';
s_MemRead <= '1'; 
wait for 100 ns;

s_MemToReg <= "01";
s_RegDest <= "01";
s_shftAmount <= "01";
s_ALU_op <= "001";
s_W_H_B <= "001";
s_WhichJB <= "0001";
wait for 100 ns;

s_ALU_src <= '1';
s_log_arith <= '1';
s_leftOrRight <= '1'; 
s_IsJump <= '1';
s_IsBranch <= '1';
s_RegWrite <= '1';
s_MemWrite <= '1';
s_MemRead <= '1'; 
wait for 100 ns;




-------------------------------------------------------------------------
--
--  This testbench should also test that each pipeline register can be 
-- individually stalled or flushed
--
------------------------------------------------------------------------- 


---------
--Test #4
---------
s_ALU_src <= '1';
s_log_arith <= '1';
s_leftOrRight <= '1'; 
s_IsJump <= '1';
s_IsBranch <= '0';
s_RegWrite <= '1';
s_MemWrite <= '1';
s_MemRead <= '1'; 
s_isLui <= '0';
s_NeedsLink <= '0';
s_MemToReg <= "01";
s_RegDest <= "01";
s_shftAmount <= "10";
s_ALU_op <= "101";
s_W_H_B <= "001";
s_WhichJB <= "1000";
s_RS <= "10000";
s_RT <= "10010";
s_RD <= "10011";                
s_instruction <= x"000011a1";
s_RegValue <= x"000000ac";
s_RF_op_2 <= x"11111111";
s_sextend_op <= x"aa000000"; 
s_EXResult <= x"cc000000"; 
s_shift_op <= x"bb000000";
s_Mult_op <= x"11000000"; 
s_fromLoad <= x"00000000"; 
s_nextPC <= x"11110000";
s_stall(0) <= '1';
s_stall(1) <= '0';
s_stall(2) <= '0';
s_stall(3) <= '0';
s_flush(0) <= '0';
s_flush(1) <= '0';
s_flush(2) <= '0';
s_flush(3) <= '0';
wait for 500 ns;

---------
--Test #5
---------
s_ALU_src <= '1';
s_log_arith <= '1';
s_leftOrRight <= '1'; 
s_IsJump <= '1';
s_IsBranch <= '0';
s_RegWrite <= '1';
s_MemWrite <= '1';
s_MemRead <= '1'; 
s_isLui <= '0';
s_NeedsLink <= '0';
s_MemToReg <= "01";
s_RegDest <= "01";
s_shftAmount <= "10";
s_ALU_op <= "101";
s_W_H_B <= "001";
s_WhichJB <= "1000";
s_RS <= "10000";
s_RT <= "10010";
s_RD <= "10011";                
s_instruction <= x"000011a1";
s_RegValue <= x"000000ac";
s_RF_op_2 <= x"11111111";
s_sextend_op <= x"aa000000"; 
s_EXResult <= x"cc000000"; 
s_shift_op <= x"bb000000";
s_Mult_op <= x"11000000"; 
s_fromLoad <= x"00000000"; 
s_nextPC <= x"11110000";
s_stall(0) <= '0';
s_stall(1) <= '1';
s_stall(2) <= '0';
s_stall(3) <= '1';
s_flush(0) <= '0';
s_flush(1) <= '0';
s_flush(2) <= '0';
s_flush(3) <= '0';
wait for 600 ns;

---------
--Test #8
---------
s_ALU_src <= '1';
s_log_arith <= '1';
s_leftOrRight <= '1'; 
s_IsJump <= '1';
s_IsBranch <= '0';
s_RegWrite <= '1';
s_MemWrite <= '1';
s_MemRead <= '1'; 
s_isLui <= '0';
s_NeedsLink <= '0';
s_MemToReg <= "01";
s_RegDest <= "01";
s_shftAmount <= "10";
s_ALU_op <= "101";
s_W_H_B <= "001";
s_WhichJB <= "1000";
s_RS <= "10000";
s_RT <= "10010";
s_RD <= "10011";                
s_instruction <= x"000011a1";
s_RegValue <= x"000000ac";
s_RF_op_2 <= x"11111111";
s_sextend_op <= x"aa000000"; 
s_EXResult <= x"cc000000"; 
s_shift_op <= x"bb000000";
s_Mult_op <= x"11000000"; 
s_fromLoad <= x"00000000"; 
s_nextPC <= x"11110000";
s_stall(0) <= '0';
s_stall(1) <= '0';
s_stall(2) <= '0';
s_stall(3) <= '0';
s_flush(0) <= '0';
s_flush(1) <= '1';
s_flush(2) <= '0';
s_flush(3) <= '0';
wait for 400 ns;


---------
--Test #2
---------
s_ALU_src <= '1';
s_log_arith <= '1';
s_leftOrRight <= '1'; 
s_IsJump <= '1';
s_IsBranch <= '0';
s_RegWrite <= '1';
s_MemWrite <= '1';
s_MemRead <= '1'; 
s_isLui <= '0';
s_NeedsLink <= '0';
s_MemToReg <= "01";
s_RegDest <= "01";
s_shftAmount <= "10";
s_ALU_op <= "101";
s_W_H_B <= "001";
s_WhichJB <= "1000";
s_RS <= "10000";
s_RT <= "10010";
s_RD <= "10011";                
s_instruction <= x"000011a1";
s_RegValue <= x"000000ac";
s_RF_op_2 <= x"11111111";
s_sextend_op <= x"aa000000"; 
s_EXResult <= x"cc000000"; 
s_shift_op <= x"bb000000";
s_Mult_op <= x"11000000"; 
s_fromLoad <= x"00000000"; 
s_nextPC <= x"11110000";
s_stall(0) <= '1';
s_stall(1) <= '1';
s_stall(2) <= '0';
s_stall(3) <= '0';
s_flush(0) <= '0';
s_flush(1) <= '0';
s_flush(2) <= '1';
s_flush(3) <= '0';
wait for 600 ns;

end process;
  
end behavioral;




