package Dao;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

class ConnectionMgr {

	  static Connection con=null;
	  static String url;
	        
	  public  static void  getConnections()
	  {
	    
	     try
	     {
	        String url = "jdbc:mysql://localhost:3306/mysql";
	        Class.forName("com.mysql.jdbc.Driver");
	        
	        try
	        {            	
	           con = DriverManager.getConnection(url,"Siri","Alcatel@1"); 	             
	        }
	        
	        catch (SQLException ex)
	        {
	           ex.printStackTrace();
	        }
	     }

	     catch(ClassNotFoundException e)
	     {
	        //System.out.println(e);
	     }
	     finally{
	    	 //System.out.println("connected"+con);
	     }

//	  return con;
	}
	}

public class ComorboditiesSwing extends JFrame  { 
	static Connection cm;
	boolean isEditTrue=true;
	public static void main(String[] args) {
		ConnectionMgr.getConnections();
		cm = ConnectionMgr.con;
		new ComorboditiesSwing();
	}
	public void changePanel(JPanel d){
	    getContentPane().removeAll();
	    add(d);
	    invalidate();
	    repaint();
	}
	
	public ComorboditiesSwing() {
		JFrame guiFrame = new JFrame();
		
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Comorb Edit Tool");
		guiFrame.setSize(450, 350);
		guiFrame.setLocationRelativeTo(null);
		ArrayList<String> rulesListList=new ArrayList<>();
		
		Statement stmt;
		try {
			stmt = cm.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery("Select distinct rule from mysql.comorbrules");
			boolean more;
			more=rs.next();
	
			if (!more) {
				//System.out.println("Sorry, No data in the Db for selected Comorb condition");	
			}
			else
			{
				int i=0;
				while(rs.next()){
					//System.out.println(rs.getString(1));
					rulesListList.add(rs.getString(1));
				}
			}
		
		}
		 catch (SQLException e) 
		 {
			e.printStackTrace();
		 }
	
		
		final String[] subRulesList =null;
		HashMap<String, String> rulesSubRulesHm= new HashMap<String, String>();
		String[] rulesList= new String[rulesListList.size()];
		rulesList=rulesListList.toArray(rulesList);
		final JPanel rulesPanel = new JPanel();
		JLabel comboLbl = new JLabel("Select Rules :");
		rulesPanel.add(comboLbl,BorderLayout.WEST);
		JComboBox rules = new JComboBox(rulesList);
		rules.setEditable(false);		    
		rulesPanel.add(rules,BorderLayout.EAST);
		final JPanel subRulesPanel = new JPanel();
		subRulesPanel.setVisible(false);
		final JTextArea ruleDescText= new JTextArea();
		final JScrollPane scroll = new JScrollPane(ruleDescText);
		final JPanel subRulesPanel2 = new JPanel();
		subRulesPanel2.setVisible(false);
		JButton Edit = new JButton("Edit");
		JButton Save = new JButton("Save");
		JButton Return = new JButton("Return");
		final JComboBox subRules = new JComboBox<String>();
		
		Edit.addActionListener(new ActionListener() 
		{@Override public void actionPerformed(ActionEvent event) 
		{ 
			changePanel(subRulesPanel2);
				ruleDescText.setEditable(true);
			}
		});
		Return.addActionListener(new ActionListener() 
		{@Override public void actionPerformed(ActionEvent event) 
		{ rulesPanel.setVisible(!rulesPanel.isVisible());
			subRulesPanel.setVisible(!subRulesPanel.isVisible());
			subRulesPanel2.setVisible(!subRulesPanel2.isVisible());
			}
		});
		final HashMap<String, String> hm= new HashMap<>();
		
		for(String s: rulesList){
			hm.put(s, "This is the initial rule description shown,for "+s);
			
		}
		
		Save.addActionListener(new ActionListener() 
		{@Override public void actionPerformed(ActionEvent event) 
		{ 			
		    Object selectedSubRule = subRules.getSelectedItem();
			changePanel(subRulesPanel2);
			
			 Statement stmtSubRule;
			    ArrayList<String> subRulesListList=new ArrayList<>();
			    try{

					stmtSubRule = cm.createStatement();
				
				ResultSet rsSubrule;
				String s=ruleDescText.getText().toString();
				//System.out.println(s);
				String stm="update  mysql.comorb set ruleDesc=\'"+s+"\' where rule=\'"+selectedSubRule.toString()+"\'";
				//System.out.println(stm);
				if(s.contains("from DB")){
					
				
				}
				
				else{
					hm.put(selectedSubRule.toString(), ruleDescText.getText());
					stmtSubRule.executeUpdate("update mysql.comorb set ruleDesc=\'"+s+"\' where rule=\'"+selectedSubRule.toString()+"\'"); //
				}
//				rsSubrule=stmtSubRule.executeQuery("select subrule from mysql.comorb where rule=\'"+selectedSubRule.toString()+"\'");
//				while(rsSubrule.next()){
//					
//					//System.out.println(rsSubrule.getString(1)+"=====================================");
//						subRulesListList.add(rsSubrule.getString(1));
//					}

			    
			    }
			    catch (Exception e) {
					// TODO: handle exception
				}
			
				ruleDescText.setEditable(false);
//				//System.out.println(" Q"+ q);
				
			}
		
		});
		
		rules.addActionListener(new ActionListener() 
		{@Override public void actionPerformed(ActionEvent event) 
		{ 
			  Object oldRule;
			 JComboBox cb = (JComboBox) event.getSource();			
			    Object selectedRule = cb.getSelectedItem();
			    oldRule = selectedRule;
			    Statement stmtSubRule;
			    ArrayList<String> subRulesListList=new ArrayList<>();
			    try{

					stmtSubRule = cm.createStatement();
					ResultSet rsSubrule;
					rsSubrule = stmtSubRule.executeQuery("select subrule from mysql.comorbrules where rule=\'"+selectedRule.toString()+"\'"); 
					subRulesListList.add("Select Subrule");
					while(rsSubrule.next()){
						
					//System.out.println(rsSubrule.getString(1));
						subRulesListList.add(rsSubrule.getString(1));
					}

					String[] subrulesList= new String[subRulesListList.size()];
					subrulesList=subRulesListList.toArray(subrulesList);
					final DefaultComboBoxModel cbm = new DefaultComboBoxModel(subrulesList);
					subRules.setModel(cbm);
			    subRulesPanel.add(subRules, BorderLayout.WEST );
			    subRulesPanel.add(Box.createHorizontalStrut(50));
			    subRulesPanel.add(scroll, BorderLayout.EAST);
				subRulesPanel.setVisible(!subRulesPanel.isVisible());
				subRulesPanel2.setVisible(!subRulesPanel2.isVisible());
				ruleDescText.setRows(7);
				ruleDescText.setAutoscrolls(true);
				ruleDescText.setText(" ");
//				ruleDescText.setAutoscrolls(false);
				ruleDescText.setColumns(18);
				ruleDescText.setEditable(false);
				selectedRule=selectedRule.toString();
				changePanel(subRulesPanel2);
				rulesPanel.setVisible(!rulesPanel.isVisible());
				
			    }
			    catch (Exception e) {
			    	e.printStackTrace();
				}
		
		}});		
		subRules.addActionListener(new ActionListener() 
		{@Override public void actionPerformed(ActionEvent event) 
		{
			 JComboBox cb = (JComboBox) event.getSource();			
			    Object selectedSubRule = cb.getSelectedItem();
			    subRules.setSelectedItem(selectedSubRule);

			    cb.setToolTipText("This gives a brief overview of the "+selectedSubRule.toString());
			    changePanel(subRulesPanel2);
			    if(hm.keySet().contains(selectedSubRule.toString())){
			    	ruleDescText.setText(hm.get(selectedSubRule.toString()));
			    }
			    else{
			    	
			    	 Statement stmtSubRule;
					    ArrayList<String> subRulesListList=new ArrayList<>();
					    try{
					    stmtSubRule = cm.createStatement();
						
						ResultSet rsSubrule;
						
						rsSubrule = stmtSubRule.executeQuery("select ruleDesc from mysql.comorb where rule=\'"+selectedSubRule.toString()+"\'"); //

						boolean moreSub;
//						subRulesListList.add("Select Subrule");
							while(rsSubrule.next()){
								String str= rsSubrule.getString(1).replace("ConditionOR", "\n(OR)");
							//System.out.println(rsSubrule.getString(1));
							ruleDescText.setText(str);
							ruleDescText.setRows(5);
							scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
							scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
							ruleDescText.setDragEnabled(true);
							ruleDescText.setAutoscrolls(true);
							}
					    }
							catch (Exception e) {
								// TODO: handle exception
							}
			    }
				
			}
		});
		subRulesPanel.add(subRules, BorderLayout.EAST );
		JPanel jp= new JPanel();
		JPanel j= new JPanel();
		//System.out.println("jskdfhksjdnfcskjdcnfsdjn"+isEditTrue);
		if( isEditTrue)
		{
		j.add(Edit);
		}
		j.add(Save);
		j.add(Return);
		guiFrame.add(j,BorderLayout.PAGE_END);
		guiFrame.add(rulesPanel, BorderLayout.PAGE_START );
		guiFrame.add(subRulesPanel, BorderLayout.WEST);
		guiFrame.setVisible(true);
	}
}


class MyActionListener implements ActionListener {
	  Object oldRule;
	  Object selectedSubRule;
//	  JTextPane jp ;
	  public void actionPerformed(ActionEvent evt) {
	    JComboBox cb = (JComboBox) evt.getSource();
	    selectedSubRule = cb.getSelectedItem();
	    oldRule = selectedSubRule;
	    //System.out.println(selectedSubRule);
	    if ("comboBoxEdited".equals(evt.getActionCommand())) {
	    } else if ("comboBoxChanged".equals(evt.getActionCommand())) {
	    }
	  }
	}
