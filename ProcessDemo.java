

import java.io.BufferedReader;
import java.io.InputStreamReader;

class ProcessDemo{
	public static void main(String[] args) throws Exception{
		Process p = Runtime.getRuntime().exec("cmd /c cmd.exe /K \"java Test\" ");
		// int x = p.waitFor();
		System.out.println(p.getErrorStream().available());
		System.out.println(p.getInputStream().available());
		// BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		// BufferedReader inputReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		// System.out.println(inputReader.readLine());
	}
}