package com.mica.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.mica.gui.GlavniProzor;

public class Main {

	public static void main(String[] args) {
		 /*ProcessBuilder pb = new ProcessBuilder("python", "-c", "print 42");
	        pb.redirectErrorStream(true);
	        try {
	            Process p = pb.start();
	            String s;
	            BufferedReader stdout = new BufferedReader (
	                new InputStreamReader(p.getInputStream()));
	            while ((s = stdout.readLine()) != null) {
	                System.out.println(s);
	            }
	            System.out.println("Exit value: " + p.waitFor());
	            p.getInputStream().close();
	            p.getOutputStream().close();
	            p.getErrorStream().close();
	         } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		*/
		new Klijent("assssssssssssssaaaaaaaaaaa");
		
		Controller controller = new Controller();
		GlavniProzor glavniProzor = new GlavniProzor(controller);
		controller.setGlavniProzor(glavniProzor);
		glavniProzor.setVisible(true);
		
		//controller.zapocniNovuIgru();
		glavniProzor.dialogPocetni(null);

	}

}
