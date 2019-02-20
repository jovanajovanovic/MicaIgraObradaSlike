package com.mica.main;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.mica.gui.GlavniProzor;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				Controller controller = new Controller();
				GlavniProzor glavniProzor = new GlavniProzor(controller);
				controller.setGlavniProzor(glavniProzor);
				glavniProzor.setVisible(true);
				//glavniProzor.prikaziDialogZaCekanje("Podaci se ucitavaju...", true);
				//controller.zapocniNovuIgru();
				glavniProzor.dialogPocetni(null);
			}
		});
		

	}

}
