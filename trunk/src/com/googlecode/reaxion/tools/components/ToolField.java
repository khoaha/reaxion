package com.googlecode.reaxion.tools.components;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.googlecode.reaxion.tools.events.ValidationEvent;
import com.googlecode.reaxion.tools.listeners.ValidationEventListener;

public class ToolField extends JTextField implements KeyListener {

	public static final String NUMBERS_ONLY = "numbers_only", TEXT_ONLY = "text_only";
	
	protected ValidationEventListener listener;
	
	protected String type;
	protected boolean valid = true;
	
	public ToolField(String type) {
		super();
		this.type = type;
		
		addKeyListener(this);
	}
	
	public ToolField(int col, String type) {
		super(col);
		this.type = type;
		
		addKeyListener(this);
	}
	
	public ToolField(String text, String type) {
		super(text);
		this.type = type;
		
		addKeyListener(this);
	}
	
	public ToolField(String text, int col, String type) {
		super(text, col);
		this.type = type;
		
		addKeyListener(this);
	}
	
	public boolean isValid() {
		return valid;
	}
	
	protected void setValid(boolean valid) {
		this.valid = valid;
		
		if (valid)
			setBackground(Color.white);
		else
			setBackground(Color.red);
	}

	public void reset() {
		setText("");
		valid = true;
	}
	
	protected void validateField() {		
		if (getText().equals("")) {
			fireValidationMade(ValidationEvent.INVALID);
		} else {
			if (type.equals(NUMBERS_ONLY)) {
				try {
					Integer.parseInt(getText());
					fireValidationMade(ValidationEvent.VALID);
				} catch (NumberFormatException ex) {
					fireValidationMade(ValidationEvent.INVALID);
				}
			} else if (type.equals(TEXT_ONLY)) {
				if (getText().matches("^[a-zA-Z]+$"))
					fireValidationMade(ValidationEvent.VALID);
				else
					fireValidationMade(ValidationEvent.INVALID);
			}	
		}
	}
	
	public synchronized void addValidationEventListener(ValidationEventListener v) {
		listener = v;
	}
	
	public synchronized void removeValidationEventListener() {
		listener = null;
	}
	
	protected synchronized void fireValidationMade(String type) {
		if (listener != null) {
			if (type.equals(ValidationEvent.VALID)) {
				setValid(true);
				listener.fieldFoundValid(new ValidationEvent(this, type));
			} else if (type.equals(ValidationEvent.INVALID)) {
				setValid(false);
				listener.fieldFoundInvalid(new ValidationEvent(this, type));
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent e) {
		validateField();		
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}