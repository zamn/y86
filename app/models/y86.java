package models;

import java.util.*;


public class y86 {
	public static class Command {
		 String name;//The name of the command being run
		 String label;//Will be != null if a label is present on the line
		 char type;
		 int subtype;
		 String paramOne;//first arg
		 String paramTwo;//second arg
		
		 int memOffset; //Will be 0 unless we're trying to access an offset
		
		Command(){
			
		}
		
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getType() {
			return type;
		}
		public void setType(char type) {
			this.type = type;
		}
		public int getSubtype() {
			return subtype;
		}
		public void setSubtype(int subtype) {
			this.subtype = subtype;
		}
		public String getParamOne() {
			return paramOne;
		}
		public void setParamOne(String paramOne) {
			this.paramOne = paramOne;
		}
		public String getParamTwo() {
			return paramTwo;
		}
		public void setParamTwo(String paramTwo) {
			this.paramTwo = paramTwo;
		}
		
		public int getMemOffset() {
			return memOffset;
		}
		public void setMemOffset(int memOffset) {
			this.memOffset = memOffset;
		};
	}

	
	static boolean zf;//Zero flag
	static boolean of;//Overflow flag
	static boolean sf;//sign flag
	static boolean io = false;
	static int ioVal;

	static int[] registers = new int [8];
	//eax, ebx, ecx, edx, esi, edi, esp, ebp
	public static ArrayList<String> ref = new ArrayList<String>();
	public static ArrayList<Command> cmds = new ArrayList<Command>();
	public static int cmdPos = 0;
	static boolean executing = true;
	static public Stack<Integer> stack = new Stack<>();
	static int statusCode = 0;
	static String output;
	public static int processCMD(String command_string) {
		System.out.println(command_string);
		if(ref.size()==0)
		{
			ref.add("%eax");
			ref.add("%ebx");
			ref.add("%ecx");
			ref.add("%edx");
			ref.add("%esi");
			ref.add("%edi");
			ref.add("%esp");
			ref.add("%ebp");	
		}
		if(io)
		{
			ioVal = Integer.parseInt(command_string);
			return executeCMD(cmds.get(cmds.size()-1));
		}
		else{



			Command com = new y86.Command();

			if (command_string.startsWith(".align")) {



			}

			if (command_string.contains(":")) {//This means we have a label
				com.setLabel(command_string.substring(0,command_string.indexOf(":")));
				command_string = command_string.substring(command_string.indexOf(":")+1);
			}
			else { com.setLabel(null); }
			String[] parts = command_string.split(" ");
			String instruction_name = parts[0];
			//System.out.println(parts[0]);
			com.setName(instruction_name);
			command_string = command_string.substring(command_string.indexOf(" ")+1);
			String[] args;

			/*All the move instructions have 2 arguments.*/
			if (instruction_name.equals("irmovl")) {
				args = command_string.split(",");
				com.setType('A');
				com.setSubtype(0);
				com.setParamOne(args[0]);
				com.setParamTwo(args[1]);
			}
			else if(instruction_name.equals("rrmovl")) {
				args = command_string.split(",");
				com.setType('A');
				com.setSubtype(1);
				com.setParamOne(args[0]);
				com.setParamTwo(args[1]);
			}

			/*Add a check for offsets when dealing with memory*/
			else if(instruction_name.equals("mrmovl")) {
				args = command_string.split(",");
				com.setType('A');
				com.setSubtype(2);
				com.setParamOne(args[0]);
				com.setParamTwo(args[1]);
			}
			else if(instruction_name.equals("rmmovl")) {
				args = command_string.split(",");
				com.setType('A');
				com.setSubtype(3);
				com.setParamOne(args[0]);
				com.setParamTwo(args[1]);
			}
			/*The I/O instructions have 1 argument that is always a register.*/
			else if(instruction_name.equals("rdch")) {

				com.setType('B');
				com.setSubtype(0);
				com.setParamOne(parts[1]);
				com.setParamTwo(null);
			}
			else if(instruction_name.equals("rdint")) {
				com.setType('B');
				com.setSubtype(1);
				com.setParamOne(parts[1]);
				com.setParamTwo(null);
			}
			else if(instruction_name.equals("wrch")) {
				com.setType('B');
				com.setSubtype(2);
				com.setParamOne(parts[1]);
				com.setParamTwo(null);
			}
			else if(instruction_name.equals("wrint")) {
				com.setType('B');
				com.setSubtype(3);
				com.setParamOne(parts[1]);
				com.setParamTwo(null);
			}
			/*The arithmatic operations have 2 arguments, both registers.*/
			else if(instruction_name.equals("addl")) {
				args = command_string.split(",");
				com.setType('C');
				com.setSubtype(0);
				com.setParamOne(args[0]);
				com.setParamTwo(args[1]);
			}
			else if(instruction_name.equals("subl")) {
				args = command_string.split(",");
				com.setType('C');
				com.setSubtype(1);
				com.setParamOne(args[0]);
				com.setParamTwo(args[1]);
			}
			else if(instruction_name.equals("andl")) {
				args = command_string.split(",");
				com.setType('C');
				com.setSubtype(2);
				com.setParamOne(args[0]);
				com.setParamTwo(args[1]);
			}
			else if(instruction_name.equals("xorl")) {
				args = command_string.split(",");
				com.setType('C');
				com.setSubtype(3);
				com.setParamOne(args[0]);
				com.setParamTwo(args[1]);
			}
			else if(instruction_name.equals("multl")) {
				args = command_string.split(",");
				com.setType('C');
				com.setSubtype(4);
				com.setParamOne(args[0]);
				com.setParamTwo(args[1]);
			}
			else if(instruction_name.equals("divl")) {
				args = command_string.split(",");
				com.setType('C');
				com.setSubtype(5);
				com.setParamOne(args[0]);
				com.setParamTwo(args[1]);
			}
			else if(instruction_name.equals("modl")) {
				args = command_string.split(",");
				com.setType('C');
				com.setSubtype(6);
				com.setParamOne(args[0]);
				com.setParamTwo(args[1]);
			}

			/*The jumps have 1 argument which is a label.*/
			else if(instruction_name.equals("jmp")) {
				com.setType('D');
				com.setSubtype(0);
				com.setParamOne(parts[1]);
				com.setParamTwo(null);	
			}
			else if(instruction_name.equals("jle")) {
				com.setType('D');
				com.setSubtype(1);
				com.setParamOne(parts[1]);
				com.setParamTwo(null);
			}
			else if(instruction_name.equals("jl")) {
				com.setType('D');
				com.setSubtype(2);
				com.setParamOne(parts[1]);
				com.setParamTwo(null);
			}
			else if(instruction_name.equals("je")) {
				com.setType('D');
				com.setSubtype(3);
				com.setParamOne(parts[1]);
				com.setParamTwo(null);
			}
			else if(instruction_name.equals("jne")) {
				com.setType('D');
				com.setSubtype(4);
				com.setParamOne(parts[1]);
				com.setParamTwo(null);
			}
			else if(instruction_name.equals("jge")) {
				com.setType('D');
				com.setSubtype(5);
				com.setParamOne(parts[1]);
				com.setParamTwo(null);
			}
			else if(instruction_name.equals("jge")) {
				com.setType('D');
				com.setSubtype(5);
				com.setParamOne(parts[1]);
				com.setParamTwo(null);
			}
			else if(instruction_name.equals("jg")) {
				com.setType('D');
				com.setSubtype(6);
				com.setParamOne(parts[1]);
				com.setParamTwo(null);
			}

			/*Push and pop have 1 arg, a register.*/
			else if(instruction_name.equals("pushl")) {
				com.setType('E');
				com.setSubtype(0);
				com.setParamOne(parts[1]);
				com.setParamTwo(null);
			}
			else if(instruction_name.equals("popl")) {
				com.setType('E');
				com.setSubtype(1);
				com.setParamOne(parts[1]);
				com.setParamTwo(null);
			}
			else if(instruction_name.equals("halt")) {
				com.setType('F');
				com.setSubtype(0);
				com.setParamOne(null);
				com.setParamTwo(null);
			}

			cmds.add(com);
			return executeCMD(com);
		}
	}



	public static void main(String[] args) {
		System.out.println(cmdPos);
		processCMD("irmovl $4,%eax");
		System.out.println(cmdPos);
		processCMD("rdint %ebx");
		System.out.println(cmdPos);
		processCMD("4");
		System.out.println(cmdPos);
		processCMD("TOP: subl %eax,%ebx");
		System.out.println(cmdPos);
		processCMD("jge TOP");
		System.out.println(cmdPos);
		processCMD("halt");

		/*int max = Integer.MAX_VALUE;
		System.out.println(max);
		System.out.println(++max);
		System.out.println(++max);*/

	}

	public static int executeCMD(Command c)
	{

		//if(!io)
		cmdPos+=1;
		if(c.type == 'A'){
			if(c.subtype == 0)
			{
				//irmovl
				if(c.paramOne.startsWith("$"))
				{
					int num = new Integer(c.paramOne.substring(1)).intValue();
					registers[ref.indexOf(c.paramTwo)] = num;
				}
				else
				{
					//load label (mem at 0) into register
				}
			}
			else if(c.subtype == 1)
			{
				registers[ref.indexOf(c.paramTwo)] = registers[ref.indexOf(c.paramOne)];
			}
			else if(c.subtype == 2)
			{
				//mr
			}
			else if(c.subtype == 3)
			{
				//rm
			}
		}
		else if(c.type == 'B'){
			sf = false;
			of = false;
			zf = false;
			if((c.subtype == 0 || c.subtype == 1 )&&io==true)
			{
				io=false;
				registers[ref.indexOf(c.paramOne)] = ioVal;
				cmdPos+=1;
				return 0;
			}
			else if(c.subtype == 0)
			{
				//rdch>
				io = true;
				return 3;
			}
			else if(c.subtype == 1)
			{
				//rdint
				io = true;
				return 3;
			}
			else if(c.subtype == 2)
			{
				//wrch

				output = Character.toString(((char)registers[ref.indexOf(c.paramOne)]));
				return 5;
			}
			else if(c.subtype == 3)
			{
				//wrint
				output = new Integer(registers[ref.indexOf(c.paramOne)]).toString();
				return 5;
			}
		}
		else if(c.type == 'C'){
			boolean negative; //Is the register negative? Used to determine the sign flag
			if (registers[ref.indexOf(c.paramTwo)] < 0) {
				negative = true;
			}
			else { negative = false; }
			if(c.subtype == 0)
			{
				//add
				long test = 0;
				test = registers[ref.indexOf(c.paramOne)] + registers[ref.indexOf(c.paramTwo)];
				if(test > Integer.MAX_VALUE || test < Integer.MIN_VALUE)
					of = true;
				else
					of = false;
				registers[ref.indexOf(c.paramTwo)] = registers[ref.indexOf(c.paramOne)] + registers[ref.indexOf(c.paramTwo)];	
			}
			else if(c.subtype == 1)
			{
				long test = 0;
				test = registers[ref.indexOf(c.paramOne)] - registers[ref.indexOf(c.paramTwo)];
				if(test > Integer.MAX_VALUE || test < Integer.MIN_VALUE)
					of = true;
				else
					of = false;
				registers[ref.indexOf(c.paramTwo)] = registers[ref.indexOf(c.paramOne)] - registers[ref.indexOf(c.paramTwo)];
			}
			else if(c.subtype == 2)
			{
				//andl
				registers[ref.indexOf(c.paramTwo)] = registers[ref.indexOf(c.paramOne)] & registers[ref.indexOf(c.paramTwo)];
			}
			else if(c.subtype == 3)
			{
				//xorl
				registers[ref.indexOf(c.paramTwo)] = registers[ref.indexOf(c.paramOne)] ^ registers[ref.indexOf(c.paramTwo)];
			}
			else if(c.subtype == 4)
			{
				//multl
				long test = 0;
				test = registers[ref.indexOf(c.paramOne)] * registers[ref.indexOf(c.paramTwo)];
				if(test > Integer.MAX_VALUE || test < Integer.MIN_VALUE)
					of = true;
				else
					of = false;
				registers[ref.indexOf(c.paramTwo)] = registers[ref.indexOf(c.paramOne)] * registers[ref.indexOf(c.paramTwo)];
			}
			else if(c.subtype == 5)
			{
				//divl
				registers[ref.indexOf(c.paramTwo)] = registers[ref.indexOf(c.paramOne)] / registers[ref.indexOf(c.paramTwo)];
			}
			else if(c.subtype == 6)
			{
				//modl
				registers[ref.indexOf(c.paramTwo)] = registers[ref.indexOf(c.paramOne)] % registers[ref.indexOf(c.paramTwo)];
			}
			if(registers[ref.indexOf(c.paramTwo)]==0)
				zf = true;
			else
				zf = false;
			if (registers[ref.indexOf(c.paramTwo)] < 0) {
				if(negative == false)
					sf = true;
				else
					sf = false;
			}
			else if (registers[ref.indexOf(c.paramTwo)] > 0) {
				if(negative==true)
					sf = true;
				else
					sf = false;
			}


		}
		else if(c.type == 'D'){
			boolean jump = false;
			if(c.subtype == 0)
			{ 
				//jmp
				jump = true;
			}
			else if(c.subtype == 1)
			{
				//jle
				if((sf ^ of)|zf)
					jump = true;
			}
			else if(c.subtype == 2)
			{
				//jl
				if((sf ^ of))
					jump = true;
			}
			else if(c.subtype == 3)
			{
				//je
				if(zf)
					jump = true;
			}
			else if(c.subtype == 4)
			{
				//jne
				if(!zf)
					jump = true;
			}
			else if(c.subtype == 5)
			{
				//jge
				if(!(sf ^ of))
					jump = true;
			}
			else if(c.subtype == 6)
			{
				//jg
				if((!(sf ^ of))&&!zf)
					jump = true;
			}
			if(jump){
				for(int i = 0; i < cmds.size();i++)
				{
					if(c.paramOne.equals(cmds.get(i).label))
					{
						cmdPos = i;
						execute();
					}
					if(!c.paramOne.equals(cmds.get(i).label) && i == cmds.size()-1)
					{
						return 2;
					}
				}
			}
		}
		else if(c.type == 'E'){
			if(c.subtype == 0)
			{
				stack.push(registers[ref.indexOf(c.paramOne)]);
			}
			else if(c.subtype == 1)
			{
				if (stack.size()> 0)
					registers[ref.indexOf(c.paramOne)] = stack.pop();
				else
					return 4;
			}
		}
		else if(c.type == 'F'){
			if(c.subtype == 0)
			{
				return 6;
			}
		}
		return 0;

	}



	private static void execute() {
		while(cmdPos < cmds.size() || (cmds.get(cmdPos).type != 'F'))
		{
			executeCMD(cmds.get(cmdPos));
		}
	}
	public String getOutput()
	{
		return output;
	}
	public ArrayList getState()
	{
		ArrayList arr = new ArrayList();
		arr.add(registers);
		boolean flags[] = {zf,sf,of};
		arr.add(flags);
		arr.add(stack);
		arr.add(Integer.toHexString(cmdPos*4));
		return arr;
		
	}

}
