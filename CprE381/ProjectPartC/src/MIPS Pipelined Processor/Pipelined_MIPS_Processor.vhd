-------------------------------------------------------------------------
-- Group 9
-- Project Part C 
-- Due December 8th, 2011
-------------------------------------------------------------------------

-- Pipelined_MIPS_Processor.vhd
-- THIS IS THE REAL ONE
-------------------------------------------------------------------------
-- DESCRIPTION: This is the pipelined register.  This register has 
--              pipelining registers in between stages.  It requires a
--              forwarding unit and a hazard unit, which control muxes
--              so that data and control logic can be forwarded.
------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;

-- MIPS Single-Cycle Processor
entity MIPS_Pipelined_Processor is
  
  port( i_clk   :   std_logic;
        i_rst   :   std_logic   );
      
end MIPS_Pipelined_Processor;

architecture structural of MIPS_Pipelined_Processor is

  -------------------------------------------
  --              Components :             --
  -------------------------------------------
  -- Mem (for DMEM only, IMEM is inside of IF)
  component mem is
	 generic(depth_exp_of_2 	: integer := 10;
			 mif_filename 	: string := "bubblesort_data.mif");    -- DATA MEM INIT HERE!
	 port   (address			: IN STD_LOGIC_VECTOR (depth_exp_of_2-1 DOWNTO 0) := (OTHERS => '0');
			 byteena			: IN STD_LOGIC_VECTOR (3 DOWNTO 0) := (OTHERS => '1');
			 clock			: IN STD_LOGIC := '1';
			 data			: IN STD_LOGIC_VECTOR (31 DOWNTO 0) := (OTHERS => '0');
			 wren			: IN STD_LOGIC := '0';
			 q				: OUT STD_LOGIC_VECTOR (31 DOWNTO 0));         
  end component;

  -- Control Logic component.
  component Control_Logic is
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
  end component;

  -- Instruction Fetch Part 1 Component
  component Instruction_Stuff_in_IF is
    port (  nextPC      :   in  std_logic_vector( 31 downto 0 );
            WE          :   in  std_logic;
            clock       :   in  std_logic;
            reset       :   in  std_logic;
            instruction :   out std_logic_vector( 31 downto 0 );
            PCPlus4     :   out std_logic_vector( 31 downto 0 )   );      
  end component;

  -- Instrucion Fetch Part 2 Component
  component Instruction_Stuff_in_ID is
    port( RegValue    :     in  std_logic_vector( 31 downto 0 );
          zeroFromALU :     in  std_logic;
          isBranch    :     in  std_logic;
          isJump      :     in  std_logic;
          ALUResult   :     in  std_logic_vector( 31 downto 0 );
          whichJB     :     in  std_logic_vector( 3  downto 0 );
          PCplus4     :     in std_logic_vector( 31 downto 0 );
          instruction :     in std_logic_vector( 31 downto 0 );
          o_nextPC    :     out std_logic_vector( 31 downto 0 )
        );
  end component;
 
  -- Load Mux (this goes right after the DMEM for load instructions)
  component load_mux is
    port(
			   i_Addr 	 			: in std_logic_vector(1 downto 0);
			   i_SelW_HW_B	: in std_logic_vector(2 downto 0);
			   i_Word				  : in std_logic_vector(31 downto 0);
			   o_S					   	: out std_logic_vector(31 downto 0)
			   );
  end component;
  
  -- Store Mux (this goes right before the DMEM for store instructions)
  component store_mux is
    port(
			   i_Addr 	 			: in std_logic_vector(1 downto 0);
			   i_SelW_HW_B	: in std_logic_vector(2 downto 0);
			   i_Word				  : in std_logic_vector(31 downto 0);
			   o_S						   : out std_logic_vector(31 downto 0);
			   o_ByteEn 			: out std_logic_vector(3 downto 0)
			   );
  end component;
  
  -- ALU 32
  component ALU32 is
    port( i_A       :   in  std_logic_vector( 31 downto 0 );
          i_B       :   in  std_logic_vector( 31 downto 0 );
          i_Opcode  :   in  std_logic_vector( 2  downto 0 );
          o_F       :   out std_logic_vector( 31 downto 0 );
          o_CryOut  :   out std_logic;
          o_Overflow:   out std_logic;
          o_Zero    :   out std_logic  );
  end component;
  
  -- Array Multiplier
  component ArrayMult is
    port( i_x  : in std_logic_vector(31 downto 0);     
          i_y  : in std_logic_vector(31 downto 0);
          o_F  : out std_logic_vector(31 downto 0));
  end component;
  
  -- Barrel Shifter
  component barrel_shifter is
    port( i_in         : in std_logic_vector(31 downto 0);     
          shiftAmount  : in std_logic_vector(4 downto 0);
          log_arith    : in std_logic;--- 0=logical 1= arithmatic
          leftOrRight  : in std_logic;--- 0=left    1= right
          o_F          : out std_logic_vector(31 downto 0));
   end component;
  
  -- Sign Extender (16-bit)
  component sixteen_to_32_extender is
    port(
		    i_In : in std_logic_vector(15 downto 0);
		    i_S	 : in std_logic;										-- 0 = zero extend, 1 = sign extend
		    o_F  : out std_logic_vector(31 downto 0)
		     );
  end component;

  -- 2-to-1 N-bit (N=32 for us) mux
  component mux_2to1n is
    generic( N  :   integer := 32 );
    port( i_A   :   in   std_logic_vector( N-1 downto 0); -- 0
          i_B   :   in   std_logic_vector( N-1 downto 0); -- 1
          sel   :   in   std_logic;
          o_R   :   out  std_logic_vector( N-1 downto 0)); 
  end component;
  
  -- 3-to-1 N-bit (N=5) mux
  component mux_3to1n5 is
    generic( N  :   integer := 5 );
    port( i_A   :   in   std_logic_vector( N-1 downto 0);
          i_B   :   in   std_logic_vector( N-1 downto 0);
          i_C   :   in   std_logic_vector( N-1 downto 0);
          sel   :   in   std_logic_vector( 1   downto 0);
          o_R   :   out  std_logic_vector( N-1 downto 0));
  end component;
  
  -- 3-to-1 N-bit (N=32 for us) mux
  component mux_3to1n is
    generic( N  :   integer := 32 );
    port( i_A   :   in   std_logic_vector( N-1 downto 0 ); -- 00
          i_B   :   in   std_logic_vector( N-1 downto 0 ); -- 01
          i_C   :   in   std_logic_vector( N-1 downto 0 ); -- 10
          sel   :   in   std_logic_vector( 1   downto 0 ); -- 11
          o_R   :   out  std_logic_vector( N-1 downto 0 )   );
  end component;

  -- 4-to-1 N-bit (N=32 for us) mux
  component mux_4to1n is
    generic( N  :   integer := 32 );
    port( i_A   :   in   std_logic_vector( N-1 downto 0 ); -- 00
          i_B   :   in   std_logic_vector( N-1 downto 0 ); -- 01
          i_C   :   in   std_logic_vector( N-1 downto 0 ); -- 10
          i_D   :   in   std_logic_vector( N-1 downto 0 ); -- 11
          sel   :   in   std_logic_vector( 1   downto 0 );
          o_R   :   out  std_logic_vector( N-1 downto 0 )   );
  end component;

  -- 5-to-1 N-bit (N=32 for us) mux
  component mux_5to1n is
  generic( N  :   integer := 32 );
  port( i_A   :   in   std_logic_vector( N-1 downto 0 );
        i_B   :   in   std_logic_vector( N-1 downto 0 );
        i_C   :   in   std_logic_vector( N-1 downto 0 );
        i_D   :   in   std_logic_vector( N-1 downto 0 );
        i_E   :   in   std_logic_vector( N-1 downto 0 );
        sel   :   in   std_logic_vector( 2   downto 0 );
        o_R   :   out  std_logic_vector( N-1 downto 0 )   );
  end component;

  -- Register File
  component register_file is
    port(
			   i_CLK 	 : in std_logic;     -- Clock input
         i_AdIn1  : in std_logic_vector(4 downto 0);
         i_AdIn2  : in std_logic_vector(4 downto 0);
         i_AdOut  : in std_logic_vector(4 downto 0);
         i_WData  : in std_logic_vector(31 downto 0);
         i_WE     : in std_logic;
         i_rst    : in std_logic;
         o_RData1 : out std_logic_vector(31 downto 0);-- Data value input
         o_RData2 : out std_logic_vector(31 downto 0)-- Data value input
			  );
  end component;
  
  -- Forwarding Unit
  component Forwarding_Unit_New is
  port(
        i_EXMEM_RegWrite       :   in  std_logic;
        i_EXMEM_rd             :   in  std_logic_vector(4 downto 0);
        i_EXMEM_rs             :   in  std_logic_vector(4 downto 0);
        i_EXMEM_rt             :   in  std_logic_vector(4 downto 0);
        i_IFID_rt              :   in  std_logic_vector(4 downto 0);
        i_IFID_rs              :   in  std_logic_vector(4 downto 0);
        i_IDEX_RegWrite        :   in  std_logic;
        i_MEMWB_RegWrite       :   in  std_logic;
        i_MEMWB_rd             :   in  std_logic_vector(4 downto 0);
        i_MEMWB_MemRead        :   in  std_logic;
        i_IDEX_rs              :   in  std_logic_vector(4 downto 0);
        i_IDEX_rt              :   in  std_logic_vector(4 downto 0);
        i_IDEX_rd              :   in  std_logic_vector(4 downto 0); 
        i_IFID_Isbranch        :   in  std_logic;
        o_EXdataOne            :   out std_logic_vector(1 downto 0);--rs
        o_EXdataTwo            :   out std_logic_vector(1 downto 0);--rt
        o_IDdataOne            :   out std_logic_vector(2 downto 0);--rs
        o_IDdataTwo            :   out std_logic_vector(2 downto 0);--rt
        o_MEMdataOne           :   out std_logic                  );
  end component;
  
  -- Hazard Detection Unit
  component HazardDetection_Unit_New is
  port( i_IDEX_MemRead    :     in    std_logic;
        i_IDEX_Rt         :     in    std_logic_vector( 4 downto 0 );
        i_IFID_Rs         :     in    std_logic_vector( 4 downto 0 );
        i_IFID_Rt         :     in    std_logic_vector( 4 downto 0 );
        o_PCWriteEnable   :     out   std_logic;
        o_IFID_stall      :     out   std_logic;
        o_IDEX_Flush      :     out   std_logic         );
  end component;
  
  -- The pipelining registers
  component Pipeline_Registers is
  port(   i_clk             :     in std_logic;
         -- i_flushORIDEXFlush :    in std_logic;
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
  end component;
  
  
  
  
  -----------------------------------------------------------------------------------------------------------------------------------------------------------------------
  
  component ex_ex_fwd
  port( i_EXMEM_RegWrite       :   in  std_logic;
        i_EXMEM_rd             :   in  std_logic_vector(4 downto 0);
        i_IDEX_rs              :   in  std_logic_vector(4 downto 0);
        i_IDEX_rt              :   in  std_logic_vector(4 downto 0);
        o_EXDataOne            :   out std_logic_vector( 1 downto 0 );
        o_EXDataTwo            :   out std_logic_vector( 1 downto 0 )    ); 
  end component;
  
  
  
  
  
  
  
  -------------------------------------------
  --               Signals :               --
  -------------------------------------------
  
  
  ------------------------------
  --      Control  Logic      --
  ------------------------------
  signal  s_ALU_src, s_log_arith, s_leftOrRight, s_IsJump, s_IsBranch, s_RegWrite,
          s_MemWrite, s_MemRead, s_NeedsLink, s_isLui       :   std_logic;
  signal  s_MemToReg, s_RegDest, s_shftAmount               :   std_logic_vector( 1 downto 0 );
  signal  s_ALU_Op, s_W_H_B                                 :   std_logic_vector( 2 downto 0 );
  signal  s_WhichJB                                         :   std_logic_vector( 3 downto 0 );
  ------------------------------ 
  
  
  ------------------------------
  --         Pre-IFID         --
  ------------------------------
  signal   s_IF_instruction     :     std_logic_vector( 31 downto 0 );
  signal   s_IF_isBranch        :     std_logic;
  ------------------------------ 
  
  
  ------------------------------
  --           IF/ID          --
  ------------------------------
  signal s_IFID_instruction, s_IFID_RegValue, s_IFID_RF_op_2, s_IFID_sextend_op, s_IFID_EXResult,
         s_IFID_fromLoad, s_IFID_nextPC                      :   std_logic_vector( 31 downto 0 );
  signal s_IFID_ALU_src, s_IFID_log_arith, s_IFID_leftOrRight, s_IFID_isJump, s_IFID_isBranch, 
         s_IFID_RegWrite, s_IFID_MemWrite, s_IFID_MemRead, s_IFID_isLui, s_IFID_needsLink     :   std_logic;
  signal s_IFID_MemToReg, s_IFID_RegDest, s_IFID_shiftAmount                                  :   std_logic_vector( 1 downto 0 );
  signal s_IFID_ALU_op, s_IFID_W_H_B                                                          :   std_logic_vector( 2 downto 0 );
  signal s_IFID_RS, s_IFID_RT, s_IFID_RD                                                      :   std_logic_vector( 4 downto 0 );
  signal s_IFID_WhichJB                                                                       :   std_logic_vector( 3 downto 0 );
  ------------------------------ 
  
    
  ------------------------------
  --           ID/EX          --
  ------------------------------
  signal s_IDEX_instruction, s_IDEX_RegValue, s_IDEX_RF_op_2, s_IDEX_sextend_op, s_IDEX_EXResult,
         s_IDEX_fromLoad, s_IDEX_nextPC                      :   std_logic_vector( 31 downto 0 );
  signal s_IDEX_ALU_src, s_IDEX_log_arith, s_IDEX_leftOrRight, s_IDEX_isJump, s_IDEX_isBranch, 
         s_IDEX_RegWrite, s_IDEX_MemWrite, s_IDEX_MemRead, s_IDEX_isLui, s_IDEX_needsLink     :   std_logic;
  signal s_IDEX_MemToReg, s_IDEX_RegDest, s_IDEX_shiftAmount                                  :   std_logic_vector( 1 downto 0 );
  signal s_IDEX_ALU_op, s_IDEX_W_H_B                                                          :   std_logic_vector( 2 downto 0 );
  signal s_IDEX_RS, s_IDEX_RT, s_IDEX_RD                                                      :   std_logic_vector( 4 downto 0 );
  signal s_IDEX_WhichJB                                                                       :   std_logic_vector( 3 downto 0 );
  ------------------------------ 
  
    
  ------------------------------
  --           EX/MEM         --
  ------------------------------
  signal s_EXMEM_instruction, s_EXMEM_RegValue, s_EXMEM_RF_op_2, s_EXMEM_sextend_op, s_EXMEM_EXResult,
         s_EXMEM_fromLoad, s_EXMEM_nextPC                      :   std_logic_vector( 31 downto 0 );
  signal s_EXMEM_ALU_src, s_EXMEM_log_arith, s_EXMEM_leftOrRight, s_EXMEM_isJump, s_EXMEM_isBranch, 
         s_EXMEM_RegWrite, s_EXMEM_MemWrite, s_EXMEM_MemRead, s_EXMEM_isLui, s_EXMEM_needsLink    :   std_logic;
  signal s_EXMEM_MemToReg, s_EXMEM_RegDest, s_EXMEM_shiftAmount                                   :   std_logic_vector( 1 downto 0 );
  signal s_EXMEM_ALU_op, s_EXMEM_W_H_B                                                            :   std_logic_vector( 2 downto 0 );
  signal s_EXMEM_RS, s_EXMEM_RT, s_EXMEM_RD                                                       :   std_logic_vector( 4 downto 0 );
  signal s_EXMEM_WhichJB                                                                          :   std_logic_vector( 3 downto 0 );
  ------------------------------ 
  
    
  ------------------------------
  --           MEM/WB         --
  ------------------------------
  signal s_MEMWB_instruction, s_MEMWB_RegValue, s_MEMWB_RF_op_2, s_MEMWB_sextend_op, s_MEMWB_EXResult,
         s_MEMWB_fromLoad, s_MEMWB_nextPC                     :   std_logic_vector( 31 downto 0 );
  signal s_MEMWB_ALU_src, s_MEMWB_log_arith, s_MEMWB_leftOrRight, s_MEMWB_isJump, s_MEMWB_isBranch, 
         s_MEMWB_RegWrite, s_MEMWB_MemWrite, s_MEMWB_MemRead, s_MEMWB_isLui, s_MEMWB_needsLink   :   std_logic;
  signal s_MEMWB_MemToReg, s_MEMWB_RegDest, s_MEMWB_shiftAmount                                  :   std_logic_vector( 1 downto 0 );
  signal s_MEMWB_ALU_op, s_MEMWB_W_H_B                                                           :   std_logic_vector( 2 downto 0 );
  signal s_MEMWB_RS, s_MEMWB_RT, s_MEMWB_RD                                                      :   std_logic_vector( 4 downto 0 );
  signal s_MEMWB_WhichJB                                                                         :   std_logic_vector( 3 downto 0 );
  ------------------------------ 
  
  
  ------------------------------
  --         Hazard           --
  ------------------------------
  signal  s_hazard_PCWE, s_hazard_IFID_stall, s_hazard_IDEX_flush    :   std_logic;
  ------------------------------   
  
  
  ------------------------------
  --         Forward          --
  ------------------------------
  signal s_fwd_IDdata1, s_fwd_IDdata2     :   std_logic_vector( 2 downto 0 );
  signal s_fwd_EXdata1, s_fwd_EXdata2     :   std_logic_vector( 1 downto 0 );
  signal s_fwd_MEMdata1                   :   std_logic;
  ------------------------------ 
  
  
  ------------------------------
  --      Intermediates       --
  ------------------------------
  signal  s_PCPlus4, s_OutPC, s_sextend_op, s_ALU2s_result, s_ALU2s_i_left, s_ALU2s_i_right,
          s_RF_i_top, s_FinalMux_op, s_storeToMem, s_FromLoad, s_ValueTop,
          s_ValueBottom, s_toShifter_data, s_toALU, s_FromALU, s_FromShifter,
          s_FromMultiplier, s_EXResult, s_reg_RDdata1, s_reg_RDdata2, s_memToLoad, s_toISinIF,
          s_inputToStoreMux                                                :  std_logic_vector( 31 downto 0 );         

  signal s_memByteEn     :   std_logic_vector( 3 downto 0 );
    
  signal s_toShifter_amt, s_IF_Rt, s_RF_i_bottom  :   std_logic_vector( 4 downto 0 );  
   
  signal s_zeroFromALU      :   std_logic; 
  
  signal s_isBranchOrJump   :   std_logic;
    
    -----------------
    -- Don't cares --
    -----------------
    signal s_dontCare, s_alsoDontCare, s_zero, s_overflow, s_carryout   :   std_logic;
    signal s_empty                      :   std_logic_vector( 31 downto 0 );
    -----------------

  ------------------------------ 
  
  begin
  
  --*********************************************************--
  --*  NOTE: Layout of file is divided into each section.   *--
  --*        Within each section, components are organized  *--
  --*        spatially from top to bottom, left to right.   *--
  --*        (according to Schematic.png)                   *--
  --*********************************************************--
    
    
    
    
  -------------------------------------------------------------
  --                 Instruction Fetch                       --
  -------------------------------------------------------------
  
  IFPart1 : Instruction_Stuff_in_IF
  port MAP(   nextPC        =>    s_toISinIF,
              WE            =>    s_hazard_PCWE,
              clock         =>    i_clk,
              reset         =>    i_rst,
              instruction   =>    s_IF_instruction,
              PCPlus4       =>    s_PCPlus4       );
  
  
  s_isBranchOrJump  <=   s_isBranch or s_isJump;
  
  branchMux  :   mux_2to1n
  port MAP(   i_A   =>   s_PCPlus4,
              i_B   =>   s_outPC,
              sel   =>   s_isBranchOrJump,
              o_R   =>   s_toISinIF   );
  
  
  -------------------------------------------------------------
  --                 Instruction Decode                      --
  -------------------------------------------------------------

  --Hazard Detection
  HazardDetector    :     HazardDetection_Unit_New
  port MAP( i_IDEX_MemRead    =>    s_IDEX_MemRead,
            i_IDEX_Rt         =>    s_IDEX_Rt,
            i_IFID_Rs         =>    s_IFID_Rs,
            i_IFID_Rt         =>    s_IFID_Rt,
            o_PCWriteEnable   =>    s_hazard_PCWE,
            o_IFID_stall      =>    s_hazard_IFID_stall,
            o_IDEX_Flush      =>    s_hazard_IDEX_Flush     );
        
  --Control Logic
  CtrlLogic : Control_logic
  port MAP(   instruction   =>    s_IFID_instruction,
              ALU_op        =>    s_ALU_op,
              ALU_src       =>    s_ALU_src,
              log_arith     =>    s_log_arith,
              leftOrRight   =>    s_leftOrRight,
              isJump        =>    s_isJump,
              isBranch      =>    s_isBranch,
              RegWrite      =>    s_RegWrite,
              MemWrite      =>    s_MemWrite,
              MemRead       =>    s_MemRead,
              MemToReg      =>    s_MemToReg,
              RegDest       =>    s_RegDest,
              W_H_B         =>    s_W_H_B,
              WhichJB       =>    s_WhichJB,
              shftAmount    =>    s_shftAmount,
              isLui         =>    s_isLui,
              NeedsLink     =>    s_NeedsLink     );

  -- Instruction Fetch Part 2
  IFPart2 : Instruction_Stuff_in_ID
  port MAP(   RegValue      =>    s_reg_RDdata1,
              zeroFromAlu   =>    s_zeroFromALU,
              isBranch      =>    s_isBranch,
              isJump        =>    s_isJump,
              ALUResult     =>    s_ALU2s_result,
              whichJB       =>    s_whichJB,
              PCplus4       =>    s_IFID_nextPC,
              instruction   =>    s_IFID_instruction,
              o_nextPC      =>    s_outPC      );

  -- ALU for the second stage
  ALU2s :   ALU32
  port MAP(   i_A         =>    s_ALU2s_i_left,
              i_B         =>    s_ALU2s_i_right,
              i_Opcode    =>    s_ALU_op,
              o_F         =>    s_ALU2s_Result,
              o_CryOut    =>    s_dontCare,
              o_Overflow  =>    s_alsoDontCare,
              o_Zero      =>    s_ZeroFromALU     );

  -- ALU2s mux left
  ALU2sMuxLeft  :   mux_5to1n
  port MAP(   i_A   =>  s_reg_RDdata1,
              i_B   =>  s_EXMEM_EXResult,
              i_C   =>  s_MEMWB_fromLoad,
              i_D   =>  s_MEMWB_EXResult,
              i_E   =>  s_EXResult,
              sel   =>  s_fwd_IDdata1,
              o_R   =>  s_ALU2s_i_left    );

  -- ALU2s mux right
  ALU2sMuxRight  :   mux_5to1n
  port MAP(   i_A   =>  s_reg_RDdata2,
              i_B   =>  s_EXMEM_EXResult,
              i_C   =>  s_MEMWB_fromLoad,
              i_D   =>  s_MEMWB_EXResult,
              i_E   =>  s_EXResult,
              sel   =>  s_fwd_IDdata2,
              o_R   =>  s_ALU2s_i_right    );
  
  -- Register File
  RegFile   :   register_file
 	port MAP(  i_CLK       =>      i_clk,
             i_AdIn1     =>      s_IFID_instruction( 25 downto 21 ), --rs
             i_AdIn2     =>      s_IFID_instruction( 20 downto 16 ), --rt
             i_AdOut     =>      s_MEMWB_RD,
             i_WData     =>      s_RF_i_top,
             i_WE        =>      s_MEMWB_RegWrite,
             i_rst       =>      i_rst,
             o_RData1    =>      s_reg_RDdata1,
             o_RData2    =>      s_reg_RDdata2     );

  -- 16-32 Extender
  Extendz :   sixteen_to_32_extender
  port MAP(   i_In      =>    s_IFID_instruction( 15 downto 0 ),  
              i_S       =>    '1',
              o_F       =>    s_sextend_op      );

  -- RF mux top (for data)
  RFMuxTop    :     mux_2to1n
  port MAP(   i_A   =>    s_FinalMux_op,
              i_B   =>    s_MEMWB_NextPC,
              sel   =>    s_MEMWB_NeedsLink,
              o_R   =>    s_RF_i_top      );
  
  -- RF mux bottom (for register)
  RFMuxBottom   :   mux_3to1n5
  port MAP(   i_A   =>    s_IFID_instruction( 20 downto 16 ),
              i_B   =>    s_IFID_instruction( 15 downto 11 ),
              i_C   =>    "11111", --31
              sel   =>    s_RegDest,
              o_R   =>    s_RF_i_bottom    );







  -------------------------------------------------------------
  --                      Execution                          --
  -------------------------------------------------------------
  

  
  
  -- ALU
  MainALU   :   ALU32
  port MAP(   i_A         =>    s_ValueTop,
              i_B         =>    s_toALU,
              i_Opcode    =>    s_IDEX_ALU_op,
              o_F         =>    s_FromALU,
              o_CryOut    =>    s_carryout,
              o_Overflow  =>    s_overflow,
              o_Zero      =>    s_zero        );
  
  -- ALU Input Mux
  ALUInputMux   :   mux_2to1n
  port MAP(   i_A   =>    s_ValueBottom,
              i_B   =>    s_IDEX_sextend_op,
              sel   =>    s_IDEX_ALU_src,
              o_R   =>    s_toALU       );
  
  -- EX Result Mux
  EXResultMux   :   mux_4to1n
  port MAP(   i_A   =>    s_FromALU,
              i_B   =>    s_FromALU,
              i_C   =>    s_FromShifter,
              i_D   =>    s_FromMultiplier,
              sel   =>    s_IDEX_MemToReg,
              o_R   =>    s_EXResult    );


  -- Barrel Shifter
  BarrelShifter   :   barrel_shifter
  port MAP(   i_in        =>    s_toShifter_data,
              shiftAmount =>    s_toShifter_amt,
              log_arith   =>    s_IDEX_log_arith,
              leftOrRight =>    s_IDEX_LeftOrRight,
              o_F         =>    s_fromShifter      );  
  
  -- Barrel Data Mux
  BarrelDataMux   :   mux_2to1n
  port MAP(   i_A   =>    s_valueBottom,
              i_B   =>    s_IDEX_sextend_op,
              sel   =>    s_IDEX_isLui,
              o_R   =>    s_toShifter_data       );
  
  -- Value Choose Mux Top
  ValueMuxTop   :   mux_3to1n
  port MAP(   i_A   =>    s_IDEX_RegValue,
              i_B   =>    s_EXMEM_EXResult,
              i_C   =>    s_finalmux_op,
              sel   =>    s_fwd_EXData1,
              o_R   =>    s_valueTop      );
  
  -- Barrel Amount Mux
  BarrelAmtMux    :   mux_3to1n5
  port MAP(   i_A   =>    "10000", -- 16
              i_B   =>    s_IDEX_instruction( 10 downto 6 ),
              i_C   =>    s_ValueTop( 4 downto 0 ),
              sel   =>    s_IDEX_shiftAmount,
              o_R   =>    s_toShifter_amt    );
  
  -- Value Choose Mux Bottom
  ValueMuxBottom    :   mux_3to1n
  port MAP(   i_A   =>    s_IDEX_RF_op_2,
              i_B   =>    s_EXMEM_EXResult,
              i_C   =>    s_finalmux_op,
              sel   =>    s_fwd_EXData2,
              o_R   =>    s_valueBottom      ); 
              
  -- Multiplier
  multiplier    :     arraymult
  port MAP(   i_X   =>    s_ValueTop,
              i_Y   =>    s_ValueBottom,
              o_F   =>    s_FromMultiplier      );
  
  
  
  
  -------------------------------------------------------------
  --                         Memory                          --
  -------------------------------------------------------------

  
  
  
  
  -- Store Mux
  storeMux    :   store_mux
  port MAP( i_Addr      =>    s_EXMEM_EXResult( 1 downto 0 ),
            i_SelW_HW_B =>    s_EXMEM_W_H_B,
            i_Word      =>    s_EXMEM_Rf_op_2,
            o_S         =>    s_storeToMem,
            o_ByteEn    =>    s_memByteEn     );
  
  -- DMEM
  dataMem   :     mem
  port MAP( 	address		=>   s_EXMEM_EXResult( 11 downto 2 ),
			       byteena		=>   s_memByteEn,
			       clock			 =>   i_clk,
			       data			  =>   s_storeToMem,
			       wren			  =>   s_EXMEM_MemWrite,
			       q				    =>   s_memToLoad   );
  
  -- Load Mux
  loadMux   :   load_mux
  port MAP( i_Addr        =>  s_EXMEM_EXResult( 1 downto 0 ),
            i_SelW_HW_B   =>  s_EXMEM_W_H_B,
            i_Word        =>  s_memToLoad,
            o_S           =>  s_FromLoad      );
  
  -- Mux that goes into store
  storeInputMux   :   mux_2to1n
  port MAP(   i_A   =>    s_EXMEM_RF_op_2,
              i_B   =>    s_MEMWB_fromLoad,
              sel   =>    s_fwd_MEMdata1,
              o_R   =>    s_inputToStoreMux   );
  
  
  -- Forwarding Unit
  forwardStuff  :   Forwarding_Unit_New
  port MAP(   i_EXMEM_RegWrite     =>    s_EXMEM_RegWrite,
              i_EXMEM_rd           =>    s_EXMEM_RD,
              i_EXMEM_rs           =>    s_EXMEM_RS,
              i_EXMEM_rt           =>    s_EXMEM_RT,
              i_IFID_rt            =>    s_IFID_RT,
              i_IFID_rs            =>    s_IFID_RS,
              i_MEMWB_RegWrite     =>    s_MEMWB_RegWrite,
              i_IDEX_RegWrite      =>    s_IDEX_RegWrite,
              i_MEMWB_rd           =>    s_MEMWB_RD,
              i_MEMWB_MemRead      =>    s_MEMWB_MemRead,
              i_IDEX_rs            =>    s_IDEX_RS,
              i_IDEX_rt            =>    s_IDEX_RT,
              i_IDEX_rd            =>    s_IDEX_RD,
              i_IFID_IsBranch      =>    s_IFID_isBranch,
              o_EXdataOne          =>    s_fwd_EXdata1,
              o_EXdataTwo          =>    s_fwd_EXdata2,
              o_IDdataOne          =>    s_fwd_IDdata1,
              o_IDdataTwo          =>    s_fwd_IDdata2,
              o_MEMdataOne         =>    s_fwd_MEMdata1        );
    


  
  -------------------------------------------------------------
  --                     Write Back                          --
  -------------------------------------------------------------
  
  
  
  
  -- Final Mux
  FinalMux  :  mux_4to1n
  port MAP( i_A     =>  s_MEMWB_fromLoad,
            i_B     =>  s_MEMWB_EXResult,
            i_C     =>  s_MEMWB_EXResult,
            i_D     =>  s_MEMWB_EXResult,
            sel     =>  s_MEMWB_MemToReg,
            o_R     =>  s_FinalMux_op   );
  
  
  

  
  
  
  -------------------------------------------------------------
  --                 Pipelined Registers                     --
  -------------------------------------------------------------
  
  --------------------------
  -- Need isBranch in IF/ID
  with s_IF_instruction( 31 downto 26 ) select
  s_IF_isBranch  <=    '1' when "000100", --
                       '1' when "000001",
                       '1' when "000111",
                       '1' when "000110",
                       '1' when "000101", --
                       '0' when others;

  --------------------------
  -- IF/ID.Rt 
  with s_IF_instruction( 31 downto 26 ) select
  s_IF_Rt <=    s_IF_instruction( 20 downto 16 ) when "000100",
                s_IF_instruction( 20 downto 16 ) when "000101",
                "00000" when others;
  
  ------------------------------------
  --            IF / ID             --
  ------------------------------------
  --This reg really doesn't have much going for it, so we can just set most things to 0.
  IFslashIDpreg   :   Pipeline_Registers
  port map(  i_clk           =>  i_clk,
       --      i_flushORIDEXFlush =>  '0',
             i_stall         =>  s_hazard_IFID_stall,
             i_flush         =>  i_rst,
             i_instruction   =>  s_IF_instruction,
             i_ALU_op        =>  "000",
             i_ALU_src       =>  '0',
             i_log_arith     =>  '0',
             i_leftOrRight   =>  '0',
             i_IsJump        =>  '0',
             i_IsBranch      =>  s_IF_isBranch,
             i_RegWrite      =>  '0',
             i_MemWrite      =>  '0',
             i_MemRead       =>  '0',
             i_MemToReg      =>  "00",
             i_RegDest       =>  "00",
             i_W_H_B         =>  "000",
             i_WhichJB       =>  "0000",
             i_shftAmount    =>  "00",
             i_isLui         =>  '0',
             i_NeedsLink     =>  '0',
             i_RegValue      =>  x"00000000",
             i_RF_op_2       =>  x"00000000",
             i_sextend_op    =>  x"00000000",
             i_RS            =>  s_IF_instruction( 25 downto 21 ),
             i_RT            =>  s_IF_instruction( 20 downto 16 ),
             i_RD            =>  "00000",
             i_EXResult      =>  x"00000000",
             i_fromLoad      =>  x"00000000",
             i_nextPC        =>  s_PCPlus4,
             o_instruction   =>  s_IFID_instruction,
             o_ALU_op        =>  s_IFID_ALU_op,
             o_ALU_src       =>  s_IFID_ALU_src,
             o_log_arith     =>  s_IFID_log_arith,
             o_leftOrRight   =>  s_IFID_leftOrRight,
             o_IsJump        =>  s_IFID_isJump,
             o_IsBranch      =>  s_IFID_isBranch,
             o_RegWrite      =>  s_IFID_RegWrite,
             o_MemWrite      =>  s_IFID_MemWrite,
             o_MemRead       =>  s_IFID_MemRead,
             o_MemToReg      =>  s_IFID_MemToReg,
             o_RegDest       =>  s_IFID_RegDest,
             o_W_H_B         =>  s_IFID_W_H_B,
             o_WhichJB       =>  s_IFID_WhichJB,
             o_shftAmount    =>  s_IFID_shiftAmount,
             o_isLui         =>  s_IFID_isLui,
             o_NeedsLink     =>  s_IFID_NeedsLink,
             o_RegValue      =>  s_IFID_RegValue,
             o_RF_op_2       =>  s_IFID_RF_op_2,
             o_sextend_op    =>  s_IFID_sextend_op,
             o_RS            =>  s_IFID_RS,
             o_RT            =>  s_IFID_RT,
             o_RD            =>  s_IFID_RD,
             o_EXResult      =>  s_IFID_EXResult,
             o_fromLoad      =>  s_IFID_fromLoad,
             o_nextPC        =>  s_IFID_nextPC          );  

  ------------------------------------
  --            ID / EX             --
  ------------------------------------
  IDslashEXpreg   :   Pipeline_Registers
  port map(  i_clk           =>  i_clk,
         --    i_flushORIDEXFlush =>  s_hazard_IDEX_flush,
             i_stall         =>  '0',
             i_flush         =>  i_rst,
             i_instruction   =>  s_IFID_instruction,
             i_ALU_op        =>  s_ALU_op,
             i_ALU_src       =>  s_ALU_src,
             i_log_arith     =>  s_log_arith,
             i_leftOrRight   =>  s_leftOrRight,
             i_IsJump        =>  s_IsJump,
             i_IsBranch      =>  s_IsBranch,
             i_RegWrite      =>  s_RegWrite,
             i_MemWrite      =>  s_MemWrite,
             i_MemRead       =>  s_MemRead,
             i_MemToReg      =>  s_MemToReg,
             i_RegDest       =>  s_RegDest,
             i_W_H_B         =>  s_W_H_B,
             i_WhichJB       =>  s_WhichJB,
             i_shftAmount    =>  s_shftAmount,
             i_isLui         =>  s_isLui,
             i_NeedsLink     =>  s_NeedsLink,
             i_RegValue      =>  s_reg_RDdata1,
             i_RF_op_2       =>  s_reg_RDdata2,
             i_sextend_op    =>  s_sextend_op,
             i_RS            =>  s_IFID_instruction( 25 downto 21 ),
             i_RT            =>  s_IFID_instruction( 20 downto 16 ),
             i_RD            =>  s_RF_i_Bottom,
             i_EXResult      =>  s_IFID_EXResult,
             i_fromLoad      =>  s_IFID_fromLoad,
             i_nextPC        =>  s_IFID_nextPC,
             o_instruction   =>  s_IDEX_instruction,
             o_ALU_op        =>  s_IDEX_ALU_op,
             o_ALU_src       =>  s_IDEX_ALU_src,
             o_log_arith     =>  s_IDEX_log_arith,
             o_leftOrRight   =>  s_IDEX_leftOrRight,
             o_IsJump        =>  s_IDEX_isJump,
             o_IsBranch      =>  s_IDEX_isBranch,
             o_RegWrite      =>  s_IDEX_RegWrite,
             o_MemWrite      =>  s_IDEX_MemWrite,
             o_MemRead       =>  s_IDEX_MemRead,
             o_MemToReg      =>  s_IDEX_MemToReg,
             o_RegDest       =>  s_IDEX_RegDest,
             o_W_H_B         =>  s_IDEX_W_H_B,
             o_WhichJB       =>  s_IDEX_WhichJB,
             o_shftAmount    =>  s_IDEX_shiftAmount,
             o_isLui         =>  s_IDEX_isLui,
             o_NeedsLink     =>  s_IDEX_NeedsLink,
             o_RegValue      =>  s_IDEX_RegValue,
             o_RF_op_2       =>  s_IDEX_RF_op_2,
             o_sextend_op    =>  s_IDEX_sextend_op,
             o_RS            =>  s_IDEX_RS,
             o_RT            =>  s_IDEX_RT,
             o_RD            =>  s_IDEX_RD,
             o_EXResult      =>  s_IDEX_EXResult,
             o_fromLoad      =>  s_IDEX_fromLoad,
             o_nextPC        =>  s_IDEX_nextPC          );  
 
 
 
  ------------------------------------
  --            EX / MEM            --
  ------------------------------------
  EXslashMEMpreg   :   Pipeline_Registers
  port map(  i_clk           =>  i_clk,
         --    i_flushORIDEXFlush => '0',
             i_stall         =>  '0',
             i_flush         =>  i_rst,
             i_instruction   =>  s_IDEX_instruction,
             i_ALU_op        =>  s_IDEX_ALU_Op,
             i_ALU_src       =>  s_IDEX_ALU_src,
             i_log_arith     =>  s_IDEX_log_arith,
             i_leftOrRight   =>  s_IDEX_leftOrRight,
             i_IsJump        =>  s_IDEX_IsJump,
             i_IsBranch      =>  s_IDEX_isBranch,
             i_RegWrite      =>  s_IDEX_RegWrite,
             i_MemWrite      =>  s_IDEX_MemWrite,
             i_MemRead       =>  s_IDEX_MemRead,
             i_MemToReg      =>  s_IDEX_MemToReg,
             i_RegDest       =>  s_IDEX_RegDest,
             i_W_H_B         =>  s_IDEX_W_H_B,
             i_WhichJB       =>  s_IDEX_WhichJB,
             i_shftAmount    =>  s_IDEX_shiftAmount,
             i_isLui         =>  s_IDEX_isLui,
             i_NeedsLink     =>  s_IDEX_NeedsLink,
             i_RegValue      =>  s_valueTop,
             i_RF_op_2       =>  s_valueBottom,
             i_sextend_op    =>  s_IDEX_sextend_op,
             i_RS            =>  s_IDEX_RS,
             i_RT            =>  s_IDEX_RT,
             i_RD            =>  s_IDEX_RD,
             i_EXResult      =>  s_EXResult,
             i_fromLoad      =>  s_IDEX_fromLoad,
             i_nextPC        =>  s_IDEX_nextPC,
             o_instruction   =>  s_EXMEM_instruction,
             o_ALU_op        =>  s_EXMEM_ALU_op,
             o_ALU_src       =>  s_EXMEM_ALU_src,
             o_log_arith     =>  s_EXMEM_log_arith,
             o_leftOrRight   =>  s_EXMEM_leftOrRight,
             o_IsJump        =>  s_EXMEM_isJump,
             o_IsBranch      =>  s_EXMEM_isBranch,
             o_RegWrite      =>  s_EXMEM_RegWrite,
             o_MemWrite      =>  s_EXMEM_MemWrite,
             o_MemRead       =>  s_EXMEM_MemRead,
             o_MemToReg      =>  s_EXMEM_MemToReg,
             o_RegDest       =>  s_EXMEM_RegDest,
             o_W_H_B         =>  s_EXMEM_W_H_B,
             o_WhichJB       =>  s_EXMEM_WhichJB,
             o_shftAmount    =>  s_EXMEM_shiftAmount,
             o_isLui         =>  s_EXMEM_isLui,
             o_NeedsLink     =>  s_EXMEM_NeedsLink,
             o_RegValue      =>  s_EXMEM_RegValue,
             o_RF_op_2       =>  s_EXMEM_RF_op_2,
             o_sextend_op    =>  s_EXMEM_sextend_op,
             o_RS            =>  s_EXMEM_RS,
             o_RT            =>  s_EXMEM_RT,
             o_RD            =>  s_EXMEM_RD,
             o_EXResult      =>  s_EXMEM_EXResult,
             o_fromLoad      =>  s_EXMEM_fromLoad,
             o_nextPC        =>  s_EXMEM_nextPC          ); 

  ------------------------------------
  --            MEM / WB            --
  ------------------------------------
  MEMslashWBpreg   :   Pipeline_Registers
  port map(  i_clk           => i_clk,
        --     i_flushORIDEXFlush =>  '0',
             i_stall         =>  '0',
             i_flush         =>  i_rst,
             i_instruction   =>  s_EXMEM_instruction,
             i_ALU_op        =>  s_EXMEM_ALU_Op,
             i_ALU_src       =>  s_EXMEM_ALU_src,
             i_log_arith     =>  s_EXMEM_log_arith,
             i_leftOrRight   =>  s_EXMEM_leftOrRight,
             i_IsJump        =>  s_EXMEM_IsJump,
             i_IsBranch      =>  s_EXMEM_isBranch,
             i_RegWrite      =>  s_EXMEM_RegWrite,
             i_MemWrite      =>  s_EXMEM_MemWrite,
             i_MemRead       =>  s_EXMEM_MemRead,
             i_MemToReg      =>  s_EXMEM_MemToReg,
             i_RegDest       =>  s_EXMEM_RegDest,
             i_W_H_B         =>  s_EXMEM_W_H_B,
             i_WhichJB       =>  s_EXMEM_WhichJB,
             i_shftAmount    =>  s_EXMEM_shiftAmount,
             i_isLui         =>  s_EXMEM_isLui,
             i_NeedsLink     =>  s_EXMEM_NeedsLink,
             i_RegValue      =>  s_EXMEM_RegValue,
             i_RF_op_2       =>  s_EXMEM_RF_op_2,
             i_sextend_op    =>  s_EXMEM_sextend_op,
             i_RS            =>  s_EXMEM_RS,
             i_RT            =>  s_EXMEM_RT,
             i_RD            =>  s_EXMEM_RD,
             i_EXResult      =>  s_EXMEM_EXResult,
             i_fromLoad      =>  s_fromLoad,
             i_nextPC        =>  s_EXMEM_nextPC,
             o_instruction   =>  s_MEMWB_instruction,
             o_ALU_op        =>  s_MEMWB_ALU_op,
             o_ALU_src       =>  s_MEMWB_ALU_src,
             o_log_arith     =>  s_MEMWB_log_arith,
             o_leftOrRight   =>  s_MEMWB_leftOrRight,
             o_IsJump        =>  s_MEMWB_isJump,
             o_IsBranch      =>  s_MEMWB_isBranch,
             o_RegWrite      =>  s_MEMWB_RegWrite,
             o_MemWrite      =>  s_MEMWB_MemWrite,
             o_MemRead       =>  s_MEMWB_MemRead,
             o_MemToReg      =>  s_MEMWB_MemToReg,
             o_RegDest       =>  s_MEMWB_RegDest,
             o_W_H_B         =>  s_MEMWB_W_H_B,
             o_WhichJB       =>  s_MEMWB_WhichJB,
             o_shftAmount    =>  s_MEMWB_shiftAmount,
             o_isLui         =>  s_MEMWB_isLui,
             o_NeedsLink     =>  s_MEMWB_NeedsLink,
             o_RegValue      =>  s_MEMWB_RegValue,
             o_RF_op_2       =>  s_MEMWB_RF_op_2,
             o_sextend_op    =>  s_MEMWB_sextend_op,
             o_RS            =>  s_MEMWB_RS,
             o_RT            =>  s_MEMWB_RT,
             o_RD            =>  s_MEMWB_RD,
             o_EXResult      =>  s_MEMWB_EXResult,
             o_fromLoad      =>  s_MEMWB_fromLoad,
             o_nextPC        =>  s_MEMWB_nextPC          );  
    
    
    s_empty <=  x"00000000";
    
  end structural;
