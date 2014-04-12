package net.stormdev.MTA.SM.test;

public class TestResult {
	private TestOperation operation;
	private boolean pass;
	private String in;
	private String out;
	
	public TestResult(TestOperation operation, boolean passed, String in, String out){
		this.operation = operation;
		this.pass = passed;
		this.in = in;
		this.out = out;
	}
	
	public TestOperation getType(){
		return operation;
	}
	
	public boolean didPass(){
		return pass;
	}
	
	public String getInput(){
		return in;
	}
	
	public String getOutput(){
		return out;
	}
	
	public void print(){
		TestManager.logger.info(getSummary());
	}
	
	public void printIfFailed(){
		if(!didPass()){
			print();
		}
	}
	
	public String getSummary(){
		return operation.name()+": Passed: "+pass+" In: '"+in+"' Out: '"+out+"'";
	}
}
