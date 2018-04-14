import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Main {
	
	public static final String XLSX_FILE_PATH = "./data200.xlsx";
	// Liste des initialisations
	private static ArrayList<Initialisation> list_init = new ArrayList<Initialisation>();

	public static ArrayList<Initialisation> getList_init() {
		return list_init;
	}

	public void setList_init(ArrayList<Initialisation> list_init) {
		this.list_init = list_init;
	}

	public static void main(String[] args) throws IOException, InvalidFormatException {
		Scanner sc = new Scanner(System.in);
		
		boolean turbines_enable;
		boolean turbines_SameStreamFlow;
		
		// To- Do récupérer les données (nivAmont, Qturb, débitTurbine(i) = bool isActive)
		
		//Recuperation des données du fichier excel
		/*ArrayList<Double> test_nivAmont=new ArrayList<Double>();
		ArrayList<Double> test_Qturb=new ArrayList<Double>();
		ArrayList<Double> turbine_debit_1=new ArrayList<Double>();
		ArrayList<Double> turbine_debit_2=new ArrayList<Double>();
		ArrayList<Double> turbine_debit_3=new ArrayList<Double>();
		ArrayList<Double> turbine_debit_4=new ArrayList<Double>();
		ArrayList<Double> turbine_debit_5=new ArrayList<Double>();
		Workbook workbook = WorkbookFactory.create(new File(XLSX_FILE_PATH));
		System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
		Sheet sheet = workbook.getSheet("Tests");
		//System.out.println("\n\nIterating over Rows and Columns using for-each loop\n");
		for (Row row : sheet) {
			test_nivAmont.add(row.getCell(0).getNumericCellValue());
			test_Qturb.add(row.getCell(1).getNumericCellValue());
			turbine_debit_1.add(row.getCell(2).getNumericCellValue());
			turbine_debit_2.add(row.getCell(3).getNumericCellValue());
			turbine_debit_3.add(row.getCell(4).getNumericCellValue());
			turbine_debit_4.add(row.getCell(5).getNumericCellValue());
			turbine_debit_5.add(row.getCell(6).getNumericCellValue());
		}
		//workbook.close();
		System.out.println(test_nivAmont.size()+" "+test_Qturb.size());
		 
		
		// To-do : Make a list_initialisation de n turbines fonctionnelles
		Sheet resultSheet=workbook.getSheet("Resultats");
		for(int i=0;i<test_nivAmont.size();i++) {
			long start=System.nanoTime();
			
			list_init.clear();
			
			if(turbine_debit_1.get(i) > 0.1f) {
				list_init.add(new Initialisation(1, 180, 0));
			}
			if(turbine_debit_2.get(i) > 0.1f) {
				list_init.add(new Initialisation(2, 180, 0));
			}
			if(turbine_debit_3.get(i) > 0.1f) {
				list_init.add(new Initialisation(3, 180, 0));
			}
			if(turbine_debit_4.get(i) > 0.1f) {
				list_init.add(new Initialisation(4, 180, 0));
			}
			if(turbine_debit_5.get(i) > 0.1f) {
				list_init.add(new Initialisation(5, 180, 0));
			}
			
			Programm programm_ = new Programm(test_Qturb.get(i).floatValue(),test_nivAmont.get(i).floatValue(), list_init);
			programm_.RunBackward();
			programm_.RunForward();
			
			long end=System.nanoTime();
	        System.out.println("Temps de calcul du test numéro "+(i+1)+": "+(end-start)+" ns");
		}*/
		
		
		// Begin Programm
		/*Programm programm = new Programm(Float.parseFloat(str), Float.parseFloat(str2), turbines_enable, turbines_SameStreamFlow);
		programm.RunBackward();
		programm.RunForward();*/
		
		Interface frame = new Interface();
	}
		
	
		/* Dépassé
		// Get variable
		System.out.println("Quel est le débit que vous voulez optimiser ? (multiple de 5)");
		String str = sc.nextLine();
		while(Integer.parseInt(str)%5 != 0 || Integer.parseInt(str) < 0) {
			sc.reset();
			System.out.println("Nombre choisi incorrect ! Quel est le débit que vous voulez optimiser ?");
			str = sc.nextLine();
		}
		
		System.out.println("Quel est l'élevation amont ?");
		String str2 = sc.nextLine();
		while(Integer.parseInt(str2) <= 0) {
			System.out.println("Nombre choisi incorrect ! Quel est l'élevation amont ?");
			str2 = sc.nextLine();
		}
		System.out.println("Vous avez saisi : " + str + " et " + str2 +"\n");
		
		System.out.println("Mise en place des turbines:");
		System.out.println("Toutes les turbines fonctionnent-elle? o/n");
		String turbines = sc.nextLine();
		while(!turbines.equalsIgnoreCase("o") && !turbines.equalsIgnoreCase("n")) {
			System.out.println("Réponse incorrect ! Toutes les turbines fonctionnent-elle ? o/n");
			turbines = sc.nextLine();
		}
		if(turbines.equalsIgnoreCase("o")) {
			turbines_enable = true;
		}else {
			turbines_enable = false;
		}
		
		System.out.println("Toutes les turbines possedent-elle un débit max = 180m3/s ? o/n");
		turbines = sc.nextLine();
		while(!turbines.equalsIgnoreCase("o") && !turbines.equalsIgnoreCase("n")) {
			System.out.println("Réponse incorrect ! Toutes les turbines possedent-elle un débit max = 180m3/s ? o/n");
			turbines = sc.nextLine();
		}
		if(turbines.equalsIgnoreCase("o")) {
			turbines_SameStreamFlow = true;
		}else {
			turbines_SameStreamFlow = false;
		}
	}*/

}
