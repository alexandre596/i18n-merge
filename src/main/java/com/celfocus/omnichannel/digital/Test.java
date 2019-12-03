package com.celfocus.omnichannel.digital;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.celfocus.omnichannel.digital.swing.JFileSearchAutoComplete;

public class Test {

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JFrame frame = new JFrame();
		frame.setTitle("Auto Completion Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(200, 200, 500, 400);

		JFileSearchAutoComplete autoComplete = new JFileSearchAutoComplete();
		autoComplete.setColumns(30);

		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(autoComplete, BorderLayout.NORTH);
		frame.setVisible(true);
	}

}