package com.celfocus.omnichannel.digital.swing.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import org.apache.commons.lang3.StringUtils;

import com.celfocus.omnichannel.digital.swing.event.SimpleDocumentListener;
import com.celfocus.omnichannel.digital.swing.model.SortedComboBoxModel;

public class JAutoComplete extends JTextField {
	
	private static final long serialVersionUID = 2679152246576562230L;
	
	private JAutoCompleteComboBox cbInput;
	private DefaultComboBoxModel<String> model;
	
	public JAutoComplete() {
		this.model = new SortedComboBoxModel<>(String.CASE_INSENSITIVE_ORDER);
		this.cbInput = new JAutoCompleteComboBox(model);
		
		this.setupAutoComplete();
	}
	
    private void setupAutoComplete() {
        cbInput.setAdjusting(false);
        cbInput.setSelectedItem(null);
        cbInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!cbInput.isAdjusting() && cbInput.getSelectedItem() != null) {
                	setText(cbInput.getSelectedItem().toString());
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	cbInput.setAdjusting(true);
                if (e.getKeyCode() == KeyEvent.VK_SPACE && cbInput.isPopupVisible()) {
                	e.setKeyCode(KeyEvent.VK_ENTER);
                }
                else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    e.setSource(cbInput);
                    cbInput.dispatchEvent(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        setText(cbInput.getSelectedItem().toString());
                        cbInput.setPopupVisible(false);
                    }
                }
                else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cbInput.setPopupVisible(false);
                }
                cbInput.setAdjusting(false);
            }
        });
        getDocument().addDocumentListener(new SimpleDocumentListener() {
			@Override
			public void update(DocumentEvent e) {
            	cbInput.setAdjusting(true);
                model.removeAllElements();
                String input = getText();
                List<String> fileNameList = getSourceList();
                
                if(!fileNameList.isEmpty() && StringUtils.isNotBlank(input)) {
                	fileNameList.stream().filter(fileName -> fileName.toLowerCase().startsWith(input.toLowerCase()))
	                	.forEach(model::addElement);
                }
                
                if(cbInput.isDisplayable()) {
	                cbInput.setPopupVisible(model.getSize() > 0);
	                cbInput.setMaximumRowCount(model.getSize());
                }
                cbInput.setAdjusting(false);
			}
        });
        setLayout(new BorderLayout());
        add(cbInput, BorderLayout.SOUTH);
    }
    
    public void hideAutoCompleteOptions() {
    	cbInput.setPopupVisible(false);
    }
    
	public List<String> getSourceList() {
		return new ArrayList<>();
	}
    
    private class JAutoCompleteComboBox extends JComboBox<String> {
    	
		private static final long serialVersionUID = 636484754461736481L;
		private boolean adjusting;

    	public JAutoCompleteComboBox(DefaultComboBoxModel<String> model) {
    		super(model);
    	}

    	@Override
        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width, 0);
        }

    	public boolean isAdjusting() {
    		return adjusting;
    	}

    	public void setAdjusting(boolean adjusting) {
    		this.adjusting = adjusting;
    	}

    }

}
