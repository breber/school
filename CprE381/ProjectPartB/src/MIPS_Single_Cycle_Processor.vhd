-------------------------------------------------------------------------
-- Group 9
-- Project Part B 
-- Due November 9th, 2011
-------------------------------------------------------------------------

-- MIPS_Single_Cycle_Processor.vhd
-------------------------------------------------------------------------
-- DESCRIPTION:  This is it! This is the big deal.  This is the MIPS
--               Single-Cycle Processor!
--               This is the structural layout of the processor as defined
--               in our diagrams using our components.
-------------------------------------------------------------------------

library IEEE;
use IEEE.std_logic_1164.all;
use WORK.mips_package.all;

-- MIPS Single-Cycle Processor
entity MIPS_sc_Processor is
  
port( i_clk   :   std_logic;
      i_rst   :   std_logic   );
      
end MIPS_sc_Processor;

architecture structural of MIPS_sc_Processor is

  -------------------------------------------
  --              Components :             --
  -------------------------------------------

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

  -- Instruction Fetch Component
  component Instruction_Fetch is
      port( clock       :     in  std_logic;
            RegValue    :     in  std_logic_vector( 31 downto 0 );
            zeroFromALU :     in  std_logic;
            isBranch    :     in  std_logic;
            isJump      :     in  std_logic;
            ALUResult   :     in  std_logic_vector( 31 downto 0 );
            whichJB     :     in  std_logic_vector( 3  downto 0 );
            reset       :     in  std_logic;
            PCplus4     :     out std_logic_vector( 31 downto 0 );
            instruction :     out std_logic_vector( 31 downto 0 )    );
  end component;
 
  -- Load Mux (this goes right after the DMEM for load instructions)
  component load_mux is
    port(
			   i_Addr 	 			: in std_logic_vector(1 downto 0);
			   i_SelW_HW_B	: in std_logic_vector(2 downto 0);
			   i_Word				  : in std_logic_vector(31 downto 0);
			   o_S						: out std_logic_vector(31 downto 0)
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
  
  -- Mem (for DMEM only, IMEM is inside of IF)
  component mem is
	 generic(depth_exp_of_2 	: integer := 10;
			 mif_filename 	: string := "bubblesort_data.mif");
	 port   (address			: IN STD_LOGIC_VECTOR (depth_exp_of_2-1 DOWNTO 0) := (OTHERS => '0');
			 byteena			: IN STD_LOGIC_VECTOR (3 DOWNTO 0) := (OTHERS => '1');
			 clock			: IN STD_LOGIC := '1';
			 data			: IN STD_LOGIC_VECTOR (31 DOWNTO 0) := (OTHERS => '0');
			 wren			: IN STD_LOGIC := '0';
			 q				: OUT STD_LOGIC_VECTOR (31 DOWNTO 0));         
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
    generic( N  :   integer := 5 );
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


  -------------------------------------------
  --               Signals :               --
  -------------------------------------------  
  
  signal  s_instruction  :   std_logic_vector( 31 downto 0 ); --the instruction being processed
  
  -------------------
  -- Control Signals:
  signal  s_ALU_src, s_log_arith, s_leftOrRight, s_IsJump, s_IsBranch, s_RegWrite,
          s_MemWrite, s_MemRead, s_NeedsLink, s_isLui       :   std_logic;
  signal  s_MemToReg, s_RegDest, s_shftAmount               :   std_logic_vector( 1 downto 0 );
  signal  s_ALU_Op, s_W_H_B                                 :   std_logic_vector( 2 downto 0 );
  signal  s_WhichJB                                         :   std_logic_vector( 3 downto 0 );
  -------------------        
  
  -------------------
  -- Intermediates:
  signal  s_RegValue        :   std_logic_vector( 31 downto 0 ); -- 1
  signal  s_ALUZero         :   std_logic;                       -- 2
  signal  s_ALUResult       :   std_logic_vector( 31 downto 0 ); -- 3
  signal  s_LinkLoc         :   std_logic_vector( 31 downto 0 ); -- no # on schematic
  signal  s_dmem_op         :   std_logic_vector( 31 downto 0 ); -- 4
  signal  s_FromLoad        :   std_logic_vector( 31 downto 0 ); -- 5
  signal  s_byteEnable      :   std_logic_vector( 3  downto 0 ); -- 6
  signal  s_fromSet         :   std_logic_vector( 31 downto 0 ); -- 7
  signal  s_RF_op2          :   std_logic_vector( 31 downto 0 ); -- 8
  signal  s_ALUB            :   std_logic_vector( 31 downto 0 ); -- 9
  signal  s_shamt           :   std_logic_vector( 4  downto 0 ); -- 10
  signal  s_shift_op        :   std_logic_vector( 31 downto 0 ); -- 11
  signal  s_mult_op         :   std_logic_vector( 31 downto 0 ); -- 12
  signal  s_sextend_op      :   std_logic_vector( 31 downto 0 ); -- 13
  signal  s_finalmux_op     :   std_logic_vector( 31 downto 0 ); -- 14
  signal  s_RFwdata         :   std_logic_vector( 31 downto 0 ); -- 15
  signal  s_wRegIn          :   std_logic_vector( 4  downto 0 ); -- 16
  signal  s_dataForBS       :   std_logic_vector( 31 downto 0 ); -- 17
  -------------------
  
  -------------------
  -- Signals that don't matter:
  signal  s_CarryOut, s_Overflow  : std_logic;
  -------------------
    
begin
  
  -- Control Logic Component
  g_ControlLogic: Control_Logic
  port MAP( instruction   =>    s_instruction,
            ALU_op        =>    s_ALU_Op,
            ALU_src       =>    s_ALU_Src,
            log_arith     =>    s_log_arith,
            leftOrRight   =>    s_leftOrRight,
            IsJump        =>    s_IsJump,
            IsBranch      =>    s_IsBranch,
            RegWrite      =>    s_RegWrite,
            MemWrite      =>    s_MemWrite,
            MemRead       =>    s_MemRead,
            MemToReg      =>    s_MemToReg,
            RegDest       =>    s_RegDest,
            W_H_B         =>    s_W_H_B,
            WhichJB       =>    s_WhichJB,
            shftAmount    =>    s_shftAmount,
            isLui         =>    s_isLui,
            NeedsLink     =>    s_NeedsLink   );
  
  -- Instruction Fetch Component
  g_InstructionFetch: Instruction_Fetch
  port MAP( clock       =>    i_clk,
            RegValue    =>    s_RegValue,
            zeroFromALU =>    s_ALUZero,
            isBranch    =>    s_IsBranch,
            isJump      =>    s_IsJump,
            ALUResult   =>    s_ALUResult,
            whichJB     =>    s_WhichJB,
            reset       =>    i_rst,
            PCplus4     =>    s_LinkLoc,
            instruction =>    s_instruction   );
           
  -- Load Mux.  Goes right after the DMEM 
  g_LoadMux:  load_mux
  port MAP( i_Addr      =>  s_ALUResult( 1 downto 0 ),
            i_SelW_HW_B =>  s_W_H_B,
            i_Word      =>  s_dmem_op,
            o_S         =>  s_FromLoad    );
  
  -- Store Mux.  Goes right before the DMEM.    
  g_StoreMux: store_mux
  port MAP( i_Addr      =>  s_ALUResult( 1 downto 0 ),
            i_SelW_HW_B =>  s_W_H_B,
            i_Word      =>  s_RF_op2,
            o_S         =>  s_fromSet,
            o_ByteEn    =>  s_byteEnable    );
  
  -- 32-bit ALU.  Does all of the arithmetic operations.          
  g_ALU32:  ALU32
  port MAP( i_A         =>  s_RegValue,
            i_B         =>  s_ALUB,
            i_Opcode    =>  s_ALU_Op,
            o_F         =>  s_ALUResult,
            o_CryOut    =>  s_CarryOut,
            o_OverFlow  =>  s_overflow,
            o_Zero      =>  s_ALUZero   );
            
  -- Barrel Shifter component
  g_BarrelShifter:  barrel_shifter
  port MAP( i_in        =>  s_dataForBS,
            shiftAmount =>  s_shamt,
            log_arith   =>  s_log_arith,
            leftOrRight =>  s_leftOrRight,
            o_F         =>  s_shift_op    );
  
  -- Array Multiplier component
  g_ArrayMultiplier:  ArrayMult
  port MAP( i_x         =>  s_RegValue,
            i_y         =>  s_ALUB,
            o_F         =>  s_mult_op   );
            
  -- 16 : 32 sign extender
  g_SignExtender: sixteen_to_32_extender
  port MAP( i_In        =>  s_instruction( 15 downto 0 ),
            i_S         =>  '1', -- hardcoded 1 to sign-extend
            o_F         =>  s_sextend_op    );
            
  -- 2-to-1 mux 32-bit, Left
  -- Inputs: 0 - s_finalmux_op, from output after dmem
  --         1 - s_LinkLoc
  -- Control: s_needsLink
  -- Output: wdata in the Register File
  g_2to1muxLeft:  mux_2to1n
  port MAP( i_A       =>  s_finalmux_op,
            i_B       =>  s_LinkLoc,
            sel       =>  s_needsLink,
            o_R       =>  s_RFwdata   );
  
  -- 2-to-1 mux 32-bit, Right
  -- Inputs: 0 - s_RF_op2
  --         1 - s_sextend_op
  -- Control: s_ALU_src
  -- Output: s_ALUB
  g_2to1muxRight:  mux_2to1n
  port MAP( i_A       =>  s_RF_op2,
            i_B       =>  s_sextend_op,
            sel       =>  s_ALU_src,
            o_R       =>  s_ALUB   );
            
  -- 2-to-1 mux 32-bit, Barrel Shifter Input
  -- Inputs: 0 - s_RF_op2
  --         1 - s_sextend_op
  -- Control: s_isLui
  -- Output: s_ALUB
  g_2to1muxForBS:  mux_2to1n
  port MAP( i_A       =>  s_RF_op2,
            i_B       =>  s_sextend_op,
            sel       =>  s_isLui,
            o_R       =>  s_dataForBS   );


  -- 3-to-1 mux 32-bit, Bottom
  -- Inputs: 00 - 16
  --         01 - s_instruction( 10 downto 6 )
  --         10 - s_RegValue( 4 downto 0 )
  -- Control: s_shftAmount
  -- Output:  s_shamt
  g_3to1muxBottom:  mux_3to1n5
  port MAP( i_A       =>  "10000", -- 16
            i_B       =>  s_instruction( 10 downto 6 ),
            i_C       =>  s_RegValue( 4 downto 0 ),
            sel       =>  s_shftAmount,
            o_R       =>  s_shamt   );

  -- 3-to-1 mux 5-bit
  -- Inputs: 00 - s_instruction( 20 downto 16 )
  --         01 - s_instruction( 15 downto 11 )
  --         10 - 31
  -- Control: s_RegDest
  -- Output: s_wRegIn
  g_3to1mux:  mux_3to1n
  port MAP( i_A       =>  s_instruction( 20 downto 16 ),
            i_B       =>  s_instruction( 15 downto 11 ),
            i_C       =>  "11111", -- 31, for register $31
            sel       =>  s_RegDest,
            o_R       =>  s_wregIn   );
            
  -- 4-to-1 mux 32-bit - "Final Mux"
  -- Inputs: 00 - s_fromLoad
  --         01 - s_ALUResult
  --         10 - s_shift_op
  --         11 - s_mult_op
  -- Control: s_MemToReg
  -- Output: s_finalmux_op       
  g_4to1mux:  mux_4to1n
  port MAP( i_A     =>  s_fromLoad,
            i_B     =>  s_ALUResult,
            i_C     =>  s_shift_op,
            i_D     =>  s_mult_op,
            sel     =>  s_MemToReg,
            o_R     =>  s_finalmux_op   );
  
  -- Data Memory   
  g_dmem: mem
  port MAP( 	address		=>   s_ALUResult( 11 downto 2 ),
			       byteena		=>   s_byteEnable,
			       clock			 =>   i_clk,
			       data			  =>   s_fromSet,
			       wren			  =>   s_MemWrite,
			       q				    =>   s_dmem_op   );
	
	-- Register File		       
	g_register_file: register_file
	port MAP(  i_CLK       =>      i_clk,
             i_AdIn1     =>      s_instruction( 25 downto 21 ),
             i_AdIn2     =>      s_instruction( 20 downto 16 ),
             i_AdOut     =>      s_wregIn,
             i_WData     =>      s_RFwdata,
             i_WE        =>      s_RegWrite,
             i_rst       =>      i_rst,
             o_RData1    =>      s_RegValue,
             o_RData2    =>      s_RF_op2    );
			       
end structural;