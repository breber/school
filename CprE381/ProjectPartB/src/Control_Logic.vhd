-------------------------------------------------------------------------
-- Group 9  :  Scott Connell, Brian Reber, Arjay Vander Velden
-- Project Part B
-- Due: November 9th, 2011
-------------------------------------------------------------------------

-- Control_Logic.vhd
-------------------------------------------------------------------------
-- DESCRIPTION:  This is the Control Logic.
--               See the "Omniscient Spreadsheet" for more information
--               about what each instruction requires.
--               We are not using processes!
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;

entity Control_Logic is
  
  port(   instruction     :     in  std_logic_vector( 31 downto 0 );
          ALU_op          :     out std_logic_vector( 2 downto 0 );
          ALU_src         :     out std_logic;
          log_arith       :     out std_logic;
          leftOrRight     :     out std_logic;
          IsJump          :     out std_logic;
          IsBranch        :     out std_logic;
          RegWrite        :     out std_logic;
          MemWrite        :     out std_logic;
          MemRead         :     out std_logic;
          MemToReg        :     out std_logic_vector( 1 downto 0 );
          RegDest         :     out std_logic_vector( 1 downto 0 );
          W_H_B           :     out std_logic_vector( 2 downto 0 );
          WhichJB         :     out std_logic_vector( 3 downto 0 );
          shftAmount      :     out std_logic_vector( 1 downto 0 );
          isLui           :     out std_logic;
          NeedsLink       :     out std_logic   );
        
end entity;


architecture dataflow of Control_Logic is

  -- Intermediate signals  to distinguish R-types with same opcode
  signal s_jump_r_type                 : std_logic;  -- R-type jumps
  signal s_regWrite, s_regWrite2       : std_logic;  -- For the regWrite logic distinguishing
  signal s_RegDest                     : std_logic_vector( 1 downto 0 );  -- For the RegDest logic distinguishing
  signal s_ALU_op                      : std_logic_vector( 2 downto 0 );  -- For the ALU_Op logic distinguishing
  signal s_jalr_sllv                   : std_logic_vector( 1 downto 0 );  -- To tell between jalr and sllv for RegDest
  signal s_branchtype                  : std_logic_vector( 3 downto 0 );  -- To tell between branch types (with al)
  signal s_shift                       : std_logic_vector( 1 downto 0 );  -- To know if this is a shift or not
  signal s_jalr                        : std_logic;  -- is this jalr?
  signal s_andLink                     : std_logic;  -- bgezal and bltzal
  signal s_jumpOrNot                   : std_logic_vector( 3 downto 0 ); --jalr/jr vs. all other 000000
  signal s_shiftVar                    : std_logic_vector( 1 downto 0 ); -- srlv, srav, sllv
  signal s_shiftNotLui                 : std_logic; -- all shifts that aren't LUI!
  signal s_logArithNotLui              : std_logic; -- all shifts that aren't LUI!

begin
  
  -----------------------------------------------------------------------
  --         First, let's create our intermediate signals to           --
  --             distinguish signals with same opcode.                 --
  -----------------------------------------------------------------------
  
  -- s_ALU_Op
  with instruction( 5 downto 0 ) select
  s_ALU_Op      <=  "101" when "100000",
                    "101" when "100001",
                    "000" when "100100",
                    "100" when "100111",
                    "001" when "100101",
                    "111" when "100010",
                    "111" when "100011",
                    "010" when "100110",
                    "110" when "101010",
                    "110" when "101011",
                    "000" when others;  -- this doesn't matter!

  -- s_jump_r_type
  with instruction( 5 downto 0 ) select
  s_jump_r_type <=  '1' when "001001",
                    '1' when "001000",
                    '0' when others;

  -- s_regWrite                    
  with instruction( 20 downto 16 ) select
  s_regWrite  <=  '1' when "10001",
                  '1' when "10000",
                  '0' when others;

  -- s_regWrite2
  with instruction( 4 downto 0 ) select
  s_regWrite2   <=  '0' when "01000",
                    '1' when others;

  -- s_RegDest
  with instruction( 5 downto 0 ) select
  s_RegDest     <=  "01" when "100000",
                    "01" when "100001",
                    "01" when "100100",
                    "10" when "001001",
                    "01" when "100111",
                    "01" when "100101",
                    "01" when "000000",
                    "01" when "000100",
                    "01" when "101010",
                    "01" when "101011",
                    "01" when "000011",
                    "01" when "000111",
                    "01" when "000010",
                    "01" when "000110",
                    "01" when "100010",
                    "01" when "100011",
                    "01" when "100110",
                    "00" when others; -- does not matter!
                    
  -- s_branchtype
  with instruction( 20 downto 16 ) select
  s_branchtype  <=  "0101" when "00001",
                    "0101" when "10001",
                    "0110" when "00000",
                    "0110" when "10000",
                    "0000" when others; -- does not matter!
                    
  -- s_shift
  with instruction( 5 downto 0 ) select
  s_shift       <=  "10" when "000000",
                    "10" when "000100",
                    "10" when "000011",
                    "10" when "000111",
                    "10" when "000010",
                    "10" when "000110",
                    "01" when others; -- does not matter!
                    
  -- s_shiftVar
  with instruction( 5 downto 0 ) select
  s_shiftVar    <=  "10" when "000100",
                    "10" when "000111",
                    "10" when "000110",
                    "01" when others;
                    
  -- s_jalr
  with instruction( 5 downto 0 ) select
  s_jalr        <=  '1' when "001001",
                    '0' when others;
     
  -- s_andLink
  with instruction( 20 downto 16 ) select
  s_andLink     <=  '1' when "10001",
                    '1' when "10000",
                    '0' when others;
                    
  -- s_jumpOrNot
  with instruction( 5 downto 0 ) select
  s_jumpOrNot   <=  "0010" when "001001",
                    "0010" when "001000",
                    "0000" when others;

  -- s_shiftNotLui
  with instruction( 5 downto 0 ) select
  s_shiftNotLui <=  '1' when "000011",
                    '1' when "000111",
                    '1' when "000010",
                    '1' when "000110",
                    '0' when others; -- takes care of all lefts and don't cares
                    
  -- s_logArithNotLui
  with instruction( 5 downto 0 ) select
  s_logArithNotLui <=  '1' when "000011",
                       '1' when "000111",
                       '0' when others; -- this will take care of all logical and don't cares

  -----------------------------------------------------------------------
  --         Each output requires a with-select statement for          --
  --               each instruction. It's kinda crazy.                 --
  -----------------------------------------------------------------------
  
  -- ALU_Op
  with instruction( 31 downto 26) select
  ALU_Op  <=    "101" when "001000",
                "101" when "001001",
                "000" when "001100",
                "111" when "000100",
                "111" when "000101",
                "001" when "001101",
                "010" when "001110",
                "110" when "000001",
                "111" when "000111",
                "111" when "000110",
                "110" when "001010",
                "110" when "001011",
                "101" when "100000",
                "101" when "100100",
                "101" when "100001",
                "101" when "100101",
                "101" when "100011",
                "101" when "101000",
                "101" when "101001",
                "101" when "101011",
                s_ALU_Op when "000000",
                "000" when others;  -- this doesn't matter
  
  -- ALU_src
  with instruction( 31 downto 26 ) select
  ALU_src <=  '0' when "000000",
              '0' when "011100",
              '0' when "000100",
              '0' when "000001",
              '0' when "000111",
              '0' when "000110",
              '0' when "000101",
              '1' when others;
  
  -- log_arith
  with instruction( 31 downto 26 ) select
  log_arith <=  s_logArithNotLui when "000000",
                '0' when others; -- this will take care of all logical and don't cares
  
  -- leftOrRight
  with instruction( 31 downto 26 ) select
  leftOrRight <=  s_shiftNotLui when "000000",
                  '0' when others; -- takes care of all lefts and don't cares
  
  -- isJump
  with instruction( 31 downto 26 ) select
  isJump  <=  '1' when "000010",
              '1' when "000011",
              s_jump_r_type when "000000",
              '0' when others;
   
  -- isBranch
  with instruction( 31 downto 26 ) select
  isBranch  <=  '1' when "000100",
                '1' when "000001",
                '1' when "000111",
                '1' when "000110",
                '1' when "000101",
                '0' when others;

  -- RegWrite
  with instruction( 31 downto 26 ) select
  RegWrite  <=  '0' when "000100",
                s_regWrite when "000001",
                '0' when "000111",
                '0' when "000110",
                '0' when "000101",
                '0' when "000010",
                '0' when "101000",
                '0' when "101001",
                '0' when "101011",
                s_regWrite2 when "000000",
                '1' when others;
  
  -- MemWrite
  with instruction( 31 downto 28 ) select
  MemWrite  <=  '1' when "1010",
                '0' when others;
  
  -- MemRead
  with instruction( 31 downto 26 ) select
  MemRead   <=  '1' when "100000",
                '1' when "100100",
                '1' when "100001",
                '1' when "100101",
                '1' when "100011",
                '0' when others;
  
  -- MemToReg
  with instruction( 31 downto 26 ) select
  MemToReg   <= "00" when "100000",
                "00" when "100100",
                "00" when "100001",
                "00" when "100101",
                "00" when "100011",
                "11" when "011100",
                "10" when "001111",
                s_shift when "000000",
                "01" when others;
  
  -- RegDest
  with instruction( 31 downto 26 ) select
  RegDest    <=   "10" when "000001",
                  "10" when "000011",
                  "01" when "011100",
                  s_RegDest when "000000",
                  "00" when others; -- lots of others. even don't cares

  
  -- W_H_B
  with instruction( 31 downto 26 ) select
  W_H_B      <= "100" when "100000",
                "101" when "100100",
                "010" when "100001",
                "010" when "101001",
                "011" when "100101",
                "011" when "101000",
                "000" when "100011",
                "000" when "101011",
                "000" when others;  -- This does not matter
                
  -- WhichJB
  with instruction( 31 downto 26 ) select
  WhichJB    <= "0011" when "000100",
                "0111" when "000111",
                "1000" when "000110",
                "0100" when "000101",
                "0001" when "000010",
                "0001" when "000011",
                s_branchtype when "000001",
                s_jumpOrNot  when "000000",
                "0000" when others; -- does matter!
                
  -- shftAmount
  with instruction( 31 downto 26 ) select
  shftAmount <=  "00" when "001111",
                  s_shiftVar when "000000",
                  "01" when others;
                
  -- NeedsLink
  with instruction( 31 downto 26 ) select
  NeedsLink   <=  '1' when "000011",
                  s_jalr when "000000",
                  s_andlink when "000001",
                  '0' when others;
                  
  -- isLui
  with instruction( 31 downto 26 ) select
  isLui       <=  '1' when "001111",
                  '0' when others; -- all others
                
end dataflow;