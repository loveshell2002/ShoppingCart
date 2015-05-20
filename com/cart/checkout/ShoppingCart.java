package com.cart.checkout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import com.layout.manager.SpringUtilities;

public class ShoppingCart extends JFrame {
	JTextField appleCountTF;
	JTextField orangeCountTF;
	JTextField totalAmount;
	JCheckBox applyOffer;
	JLabel errorLabel;
	double perApplePrice = 0.60;
	double perOrangePrice = 0.25; 


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() { 
			public void run() {
				new ShoppingCart().createGUI();
			}
		});
	} 


	public ShoppingCart() { 
		super("CheckOut System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		setSize(350,250);
		setLocationRelativeTo(null); 
	}

	protected void createGUI() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getDisplayScreen(),BorderLayout.CENTER); 
		errorLabel = new JLabel("",SwingUtilities.CENTER);
		errorLabel.setPreferredSize(new Dimension(getWidth(),35));
		getContentPane().add(errorLabel,BorderLayout.SOUTH);
	}


	private JPanel getDisplayScreen() {
		JPanel displayPanel = new JPanel();  
		displayPanel.setLayout(new SpringLayout());

		displayPanel.add(new JLabel("Apples"));
		appleCountTF = new JTextField(10);
		displayPanel.add(appleCountTF);
		appleCountTF.setFont(getFont());
		displayPanel.add(new JLabel("Orange"));
		orangeCountTF = new JTextField(10);
		orangeCountTF.setFont(getFont());
		displayPanel.add(orangeCountTF);
		displayPanel.add(new JLabel("Apply Offer"));
		applyOffer = new JCheckBox(); 
		displayPanel.add(applyOffer);
		JButton calculate = new JButton("CHECK OUT");
		calculate.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				calculateTotalAmount();
			} 
		});
		displayPanel.add(calculate);
		totalAmount = new JTextField(10);
		totalAmount.setFont(getFont());
		displayPanel.add(totalAmount);
		displayPanel.setPreferredSize(new Dimension(this.getWidth()-50,this.getHeight()-50));
		
		SpringUtilities.makeCompactGrid(displayPanel, 4, 2, 15, 15, 15, 15);
		return displayPanel;
	}



	private void calculateTotalAmount() { 
		try{
			long totalAppleCount = Long.parseLong(appleCountTF.getText().trim().equals("")  ?   "0" : appleCountTF.getText());

			if(totalAppleCount >= 0){
				try{
					long totalOrangeCount = Long.parseLong(orangeCountTF.getText().trim().equals("")  ?   "0" : orangeCountTF.getText());

					if(totalOrangeCount >= 0){
						if(applyOffer.isSelected()){ 
							errorLabel.setText("");
							totalAmount.setText(getTotalAmount(totalAppleCount,totalOrangeCount,true));   
						}
						else{
							errorLabel.setText("");
							totalAmount.setText(getTotalAmount(totalAppleCount,totalOrangeCount,false)); 
						}
					}else{
						setErrorString("PLEASE ENTER VALID NUMBER OF ORRANGE's");
					}

				}catch(NumberFormatException nfe){
					setErrorString("PLEASE ENTER VALID NUMBER OF ORRANGE's");
				}
			}else{
				setErrorString("PLEASE ENTER VALID NUMBER OF APPLE's");
			} 

		}catch(NumberFormatException nfe){
			setErrorString("PLEASE ENTER VALID NUMBER OF APPLE's");
		}



	} 

	private String getTotalAmount(long totalAppleCount, long totalOrangeCount, boolean isOffer) {
		double totalAmount = 0.0;

		if(isOffer){
			double appleQuotient = totalAppleCount / 2;
			double appleRemainder = totalAppleCount % 2;
			double appleOfferPrice = (appleQuotient*perApplePrice) + (appleRemainder*perApplePrice);

			double orangeQuotient = totalOrangeCount / 3;
			double orangeRemainder = totalOrangeCount % 3;
			double orangeOfferPrice = (orangeQuotient*(perOrangePrice*2)) + (orangeRemainder*perOrangePrice);

			totalAmount = appleOfferPrice + orangeOfferPrice;
		}
		else{
			totalAmount = (totalAppleCount*perApplePrice) + (totalOrangeCount*perOrangePrice); 
		}



		return (new DecimalFormat("##.##").format(totalAmount))+" £";
	}




	private void refreshComponent(JComponent componentToRefresh){
		componentToRefresh.repaint();
		componentToRefresh.revalidate(); 
	}

	public void setErrorString(String errorString){ 
		errorLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
		totalAmount.setText("");
		errorLabel.setForeground(Color.RED);
		errorLabel.setText(errorString);
		refreshComponent(errorLabel);
	}


	public Font getFont(){
		return new Font(Font.MONOSPACED,Font.BOLD,20);
	}

}
