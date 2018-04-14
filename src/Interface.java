import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.text.NumberFormatter;

public class Interface extends JFrame implements MouseListener, ActionListener {

	long startTime;
	long endTime;
	
	private static final long serialVersionUID = 1L;
	private final int width = 1080;
	private final int heigth = 720;

	private final int widthTurbine = 150;
	private final int heigthTurbine = 150;

	// Les images du bouton et des diffèrentes faces des dés
	private BufferedImage turbineImage;
	private BufferedImage turbineImageGrey;

	//création du bouton valider
	private JButton validate;

	// texte
	private JLabel turbineTextLabelMaxStreamFlow;
	private JLabel streamFlowText;
	private JLabel elevationAvalText;
	// Images
	private ArrayList<JLabel> turbineLabel = new ArrayList<JLabel>();
	// Results
	private JLabel resultText = new JLabel();
	private JLabel streamFlowExceed = new JLabel();
	private ArrayList<JLabel> turbineResultStreamFlow = new ArrayList<JLabel>();
	private ArrayList<JLabel> turbineResultPower = new ArrayList<JLabel>();
	// CheckBox
	private ArrayList<JCheckBox> turbineCheckBox = new ArrayList<JCheckBox>();
	private ArrayList<JFormattedTextField> maximumStreamFlowTurbine = new ArrayList<JFormattedTextField>();
	private JFormattedTextField streamFlow;
	private JFormattedTextField elevationAval;

	// Liste des initialisations
	private ArrayList<Initialisation> initialisation_ = new ArrayList<Initialisation>();
	private float qTurbine, elevAval;

	public Interface() {
		//Display the window.
		this.setVisible(true);
		this.setSize(1080, 720);
		//this.getContentPane().setBackground( new Color(70, 76, 4) );
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Optimisation ");
		addMouseListener(this);

		// Get the path for pictures
		URL urlTurbine = getClass().getResource("Turbine.png");
		URL urlTurbineGrey = getClass().getResource("Turbine2.png");

		//Initialize image
		File fileTurbine = new File(urlTurbine.getPath());
		turbineImage = null;
		try {
			turbineImage = ImageIO.read(fileTurbine);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Initialize image grey
		File fileTurbineGrey = new File(urlTurbineGrey.getPath());
		turbineImageGrey = null;
		try {
			turbineImageGrey = ImageIO.read(fileTurbineGrey);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		NumberFormat longFormat = NumberFormat.getIntegerInstance();

		NumberFormatter numberFormatter = new NumberFormatter(longFormat);
		numberFormatter.setValueClass(Long.class); //optional, ensures you will always get a long value
		numberFormatter.setAllowsInvalid(false); //this is the key!!
		numberFormatter.setMinimum(0l);
		numberFormatter.setMaximum(180l);

		for(int i = 0; i < 5; i++) {
			maximumStreamFlowTurbine.add(new JFormattedTextField(numberFormatter));
			maximumStreamFlowTurbine.get(i).setValue(new Double(180));
			maximumStreamFlowTurbine.get(i).setColumns(3);
		}

		NumberFormatter numberFormatter2 = new NumberFormatter(longFormat);
		numberFormatter2.setValueClass(Long.class); //optional, ensures you will always get a long value
		numberFormatter2.setAllowsInvalid(false); //this is the key!!
		numberFormatter2.setMinimum(0l);
		numberFormatter2.setMaximum(999l);

		streamFlow = new JFormattedTextField(numberFormatter2);
		streamFlow.setValue(new Double(680));
		streamFlow.setColumns(3);
		
		elevationAval = new JFormattedTextField(numberFormatter2);
		elevationAval.setValue(new Double(172));
		elevationAval.setColumns(3);

		Show();
	}

	
	
	public void Show()
	{
		// Les images de turbines (peut être fait dans une boucle)
		for(int i = 0; i < 5; i++) {
			turbineLabel.add(new JLabel(new ImageIcon(turbineImage)));
			turbineLabel.get(i).setBounds((50 + i*200), 100, widthTurbine, heigthTurbine);
			add(turbineLabel.get(i));
			
			maximumStreamFlowTurbine.get(i).setBounds((100 + i*200), 250, 50, 40);
			add(maximumStreamFlowTurbine.get(i));
			
			turbineCheckBox.add(new JCheckBox("Turbine n°" + (i+1)));
			turbineCheckBox.get(i).setSelected(true);
			turbineCheckBox.get(i).addActionListener(new ActionListener() {

	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	// To do
	            	//turbineLabel.set(i, new JLabel(new ImageIcon(turbineImageGrey)));
	            }
	        });
			turbineCheckBox.get(i).setBounds(80 + i*200, 300, 100, 40);
			add(turbineCheckBox.get(i));
		}

		streamFlowText = new JLabel();
		streamFlowText.setText("Débit total fourni (en m3/s) : ");
		streamFlowText.setBounds(225, 50, 200, 40);
		add(streamFlowText);
		
		elevationAvalText = new JLabel();
		elevationAvalText.setText(("Elevation aval (en m) :"));
		elevationAvalText.setBounds(500, 50, 300, 40);
		add(elevationAvalText);
		
		turbineTextLabelMaxStreamFlow = new JLabel();
		turbineTextLabelMaxStreamFlow.setText("Débit max : ");
		turbineTextLabelMaxStreamFlow.setBounds(20, 250, 100, 40);
		add(turbineTextLabelMaxStreamFlow);
		
		// Button
		validate = new JButton("Lancer le programme");
		validate.setBounds(400, 400, 200, 30); 

		// All SreamFlow
		streamFlow.setBounds(400, 50, 50, 40);
		add(streamFlow);
		
		// All SreamFlow
		elevationAval.setBounds(650, 50, 50, 40);
		add(elevationAval);
		
		//Ajout de l'action listener
		validate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// On commence à executer le programme
				startTime= System.currentTimeMillis();
				
				// TO-DO faire l'initialisation
				System.out.print("Traitement");
				qTurbine = Float.parseFloat(streamFlow.getText());
				elevAval = Float.parseFloat(elevationAval.getText());
				
				initialisation_.clear();
				
				for(int i = 0; i < 5; i++) {
					if(turbineCheckBox.get(i).isSelected()) {
						initialisation_.add(new Initialisation(i+1, Integer.parseInt(maximumStreamFlowTurbine.get(i).getText())));
					}
				}
				Programm programm = new Programm(qTurbine, elevAval, initialisation_);
				programm.RunBackward();
				programm.RunForward();
				
				endTime = System.currentTimeMillis();
				System.out.println("Total execution time: " + (endTime - startTime) + " ms");
				
				// Results
				resultText.setText("Puissance produite : " + String.valueOf(programm.getFinalPowerProduction()) + " MegaWatt ");
				resultText.setBounds(200, 500, 500, 100);
				add(resultText);
				streamFlowExceed.setText("Débit en excès : " + String.valueOf(programm.getStreamFlowExceed()) + " m3/s ");
				streamFlowExceed.setBounds(600, 500, 500, 100);
				add(streamFlowExceed);
				
				turbineResultStreamFlow.clear();
				for(int i = 0; i < programm.getList_turbine().size(); i++) {
					if(turbineCheckBox.get(i).isSelected()) {
						turbineResultStreamFlow.add(new JLabel(String.valueOf(programm.getList_turbine().get(i).getFinalStreamFlow())));
					}else {
						turbineResultStreamFlow.add(new JLabel("0"));
					}
					turbineResultStreamFlow.get(i).setBounds((100 + i*200), 350, 50, 40);
					add(turbineResultStreamFlow.get(i));
				}
				repaint();
			}
		});
		
		// Add to the paint
		add(validate);
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}