package com.cipres.mrBayesPlugin.models;

import java.awt.ComponentOrientation;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.biomatters.geneious.publicapi.plugin.Options;

/**
 * Login display model where it asks for the user's CIPRES login information
 * @author rjzheng
 *
 */
public class LoginGUIModel extends Options {

    final JPasswordField passwordField;
    final StringOption username;
    JLabel usernameLabel;
    JLabel passwordLabel;
    
    public LoginGUIModel() {
	    super(LoginGUIModel.class);
	    username = addStringOption("username","User Name","Please enter your username");
	    passwordField = new JPasswordField();
    }
    protected JPanel createPanel() {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        
        usernameLabel = new JLabel(username.getLabel());
        passwordLabel = new JLabel("Password");
        
        panel.add(username.getComponent());

        panel.add(passwordLabel);
        panel.add(passwordField);
        
        layout.setHorizontalGroup(
    		layout.createSequentialGroup()
    			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(usernameLabel)
					.addComponent(passwordLabel))
    			.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(username.getComponent())
					.addComponent(passwordField))
        );
        
        layout.setVerticalGroup(
        		layout.createParallelGroup()
        		.addGroup(layout.createSequentialGroup()
    				.addComponent(usernameLabel)
    				.addComponent(passwordLabel))
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(username.getComponent())
        			.addComponent(passwordField))
		);
        return panel;
   }
    
    /**
     * Add username and password fields to the Login interface
     */
    

    /**
     * Get the user name
     * @return user name
     */
    public String getUsername() {
        return username.getValue();
    }

    /**
     * Get the password
     * @return password
     */
    public String getPassword() {
        return new String(passwordField.getPassword());
     }
}