package com.example.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple calculator application
 *
 * @author Brian Reber (breber)
 */
public class MainActivity extends Activity {

	/**
	 * A string builder to represent the first number entered in the series of entries
	 */
	private StringBuilder expression1;

	/**
	 * A string builder to represent the second number entered in the series of entries
	 */
	private StringBuilder expression2;

	/**
	 * A string to represent the last operator performed
	 */
	private char oldOperator;

	/**
	 * Was an operator the last button pressed?
	 */
	private boolean operatorLast = true;

	/**
	 * Was the equal sign the last operator pressed?
	 */
	private boolean equalLast = false;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// declare all buttons used within the layout
		Button zero = (Button) findViewById(R.id.button0);
		Button one = (Button) findViewById(R.id.button1);
		Button two = (Button) findViewById(R.id.button2);
		Button three = (Button) findViewById(R.id.button3);
		Button four = (Button) findViewById(R.id.button4);
		Button five = (Button) findViewById(R.id.button5);
		Button six = (Button) findViewById(R.id.button6);
		Button seven = (Button) findViewById(R.id.button7);
		Button eight = (Button) findViewById(R.id.button8);
		Button nine = (Button) findViewById(R.id.button9);
		Button times = (Button) findViewById(R.id.buttontimes);
		Button clear = (Button) findViewById(R.id.buttonClear);
		Button equal = (Button) findViewById(R.id.buttonEqual);
		Button decimal = (Button) findViewById(R.id.buttonDecimal);
		Button divide = (Button) findViewById(R.id.buttondivide);
		Button add = (Button) findViewById(R.id.buttonplus);
		Button subtract = (Button) findViewById(R.id.buttonminus);

		// declare main text view
		final TextView main = (TextView) findViewById(R.id.CalculatorText);

		// Main Strings to represent the expressions
		expression1 = new StringBuilder();
		expression2 = new StringBuilder();
		main.setText(expression1.append("0"));

		/*
		 * Set up all key listener events
		 */
		zero.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				numberPressed(0);
			}
		});

		one.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				numberPressed(1);
			}
		});

		two.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				numberPressed(2);
			}
		});

		three.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				numberPressed(3);
			}
		});

		four.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				numberPressed(4);
			}
		});

		five.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				numberPressed(5);
			}
		});

		six.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				numberPressed(6);
			}
		});

		seven.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				numberPressed(7);
			}
		});

		eight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				numberPressed(8);
			}
		});

		nine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				numberPressed(9);
			}
		});

		times.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				operationPressed("*");
			}
		});

		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				operationPressed("C");
			}
		});

		equal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				operationPressed("=");
			}
		});

		decimal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				numberPressed(-1);
			}
		});

		divide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				operationPressed("/");
			}
		});

		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				operationPressed("+");
			}
		});

		subtract.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				operationPressed("-");
			}
		});
	}

	/**
	 * Called when a number (or decimal) key is pressed
	 *
	 * @param number a number between 0 and 9, or -1 for '.'
	 */
	public void numberPressed(int number) {
	    // If we are starting over (expecting a new number), set the currentDisplay
	    // value to whatever the command value is
	    if (operatorLast) {
	        expression2 = new StringBuilder();
	    }

	    if (number == -1) {
	    	// Only one decimal character allowed
	    	if (!expression2.toString().contains(".")) {
	    		expression2.append(".");
	    	}
	    } else {
	    	expression2.append(number);
	    }

	    // If the last button pressed was the equal operator, and the user
	    // is now entering numbers, we should clear the accumulated value
	    if (operatorLast && equalLast) {
	    	expression2 = new StringBuilder("0");
	        oldOperator = '+';
	        equalLast = false;
	    }

	    operatorLast = false;

	    updateDisplay();
	}

	/**
	 * Called when an operator button is pressed
	 *
	 * @param command the string representing the operator
	 */
	public void operationPressed(String command) {
	    // If we are given 'C', clear the previous display, the current display,
	    // and any stored operation. Essentially clear everything.

	    operatorLast = true;

	    if ("C".equals(command)) {
	    	expression1 = new StringBuilder("0");
	        expression2 = new StringBuilder("0");
	        oldOperator = '+';
	    } else {
	        // On any other operation, follow this line
	        String result = evaluate(command);

	        // Update the display, store the current value as the previous value,
	        // indicate we are waiting for a new number, and update the currentOperation
	        expression1 = new StringBuilder(result);

	        if (!"=".equals(command)) {
	        	oldOperator = command.charAt(0);
	        	expression2 = new StringBuilder("0");
	            equalLast = false;
	        } else {
	        	equalLast = true;
	        }
	    }

	    updateDisplay();
	}

	/**
	 * This method will evaluate an operation using expression1 and expression2
	 *
	 * @param command or operation to be performed
	 * @return the result of the operation
	 */
	public String evaluate(String currentOperation) {
	    double result = 0;
	    double value1 = Double.parseDouble(expression1.toString());
	    double value2 = Double.parseDouble(expression2.toString());

	    // If we are currently storing an operation, perform the operation
	    // on the values we stored
	    if (equalLast && !"=".equals(currentOperation)) {
	        result = value1;
	    } else {
	        switch (oldOperator) {
	            case '+':
	                result = value1 + value2;
	                break;

	            case '-':
	                result = value1 - value2;
	                break;

	            case '*':
	                result = value1 * value2;
	                break;

	            case '/':
	                result = value1 / value2;
	                break;

	            default:
	                result = value2;
	                break;
	        }
	    }

	    return result + "";
	}

	/**
	 * Update what is displayed on the screen
	 */
	private void updateDisplay() {
		final TextView main = (TextView) findViewById(R.id.CalculatorText);
		if (operatorLast) {
			main.setText(expression1);
		} else {
			main.setText(expression2);
		}
	}
}
