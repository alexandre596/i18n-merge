package com.celfocus.omnichannel.digital;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.celfocus.omnichannel.digital.ui.UploadFileUI;

@SpringBootApplication
public class MainRunner {
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(MainRunner.class).headless(false).run(args);
		
        EventQueue.invokeLater(() -> {
        	try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        	UploadFileUI ex = ctx.getBean(UploadFileUI.class);
	            ex.initialize();
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
	}
	
	

}
