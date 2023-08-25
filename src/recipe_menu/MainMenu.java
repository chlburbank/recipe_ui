package recipe_menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;




/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Utilisateur
 */
public class MainMenu extends JFrame {
    
    MainMenu() {
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(750,500);
        
//      Buttons
        JButton submitSearch = new JButton("Search");
        submitSearch.setBounds(10,10,100,30);
        
//      Search Box
        JTextField searchBox = new JTextField();
        searchBox.setBounds(120,10,200,30);

//      List
        DefaultListModel<String> myList = new DefaultListModel<>();
        
        HashMap<Long,String> recipes = new HashMap<>();
        try {
            URL url = new URL("https://api.spoonacular.com/recipes/complexSearch?apiKey=ef8982b1cabf4a7fa61da7af6de01e04");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            
            //Checking if connection is made
            int responseCode = conn.getResponseCode();
            
            //200
            if (responseCode != 200) {
                throw new RuntimeException("Error: " + responseCode);
            } else {
                StringBuilder information = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                
                while(scanner.hasNext()) {
                    information.append(scanner.nextLine());
                }
                
                //Closes the Scanner
                scanner.close();

                JSONParser parser = new JSONParser();  
                JSONObject json = (JSONObject) parser.parse(information.toString());
                Object result =  json.get("results"); //Gives me an array containing various objects
                JSONArray array = (JSONArray) result;
                
                //Retrieving the first element
                for (Object object : array) {
                    JSONObject json2 = (JSONObject) parser.parse(object.toString());
                    myList.addElement(json2.get("title").toString());
                    
                    recipes.put((Long)json2.get("id"), json2.get("title").toString());
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        JList<String> list = new JList<>(myList);
        list.setBounds(10,50,500,300);
        list.setBackground(Color.LIGHT_GRAY);
        
        //Panel Information
        JTextPane informationPanel = new JTextPane();
        informationPanel.setBounds(520,50,200,300);
        informationPanel.setBackground(Color.LIGHT_GRAY);
        informationPanel.setEditable(false);
//        informationPanel.setLineWrap(true);
        
        list.getSelectionModel().addListSelectionListener(e -> {
            //Empty the Informational Panel
            informationPanel.setText("");
            
            //Retrieve the summary of the Selected Item
//            for (Long x : recipes.keySet()) {
////                If(x == )
//            }
            
            try {
                URL summaryUrl = new URL("https://api.spoonacular.com/recipes//summary");
            } catch (Exception e2) {
                
            }
            
            
            String recipeTitle = list.getSelectedValue();
            
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setBold(attr, true);
            
            Document doc = informationPanel.getStyledDocument();
            
            try {
                doc.insertString(doc.getLength(), recipeTitle, attr);
            }catch(BadLocationException w) {
                
            }
            
            
//            informationPanel.setText(recipeTitle);
        });
        
        this.add(submitSearch);
        this.add(searchBox);
        this.add(list);
        this.add(informationPanel);
        this.setVisible(true);
        
    }

}
