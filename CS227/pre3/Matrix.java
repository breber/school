package pre3;

import java.util.ArrayList;

/**
 * @author brianreber
 *
 */
public class Matrix {
	
	private int rows;
	private int cols;
	private int[][] matrix;
	
	public Matrix(int rows, int cols)
	{
		this.rows = rows;
		this.cols = cols;
		matrix = new int[rows][cols];
	}
	
	/**
	 * Gets the number at the given row and col
	 * 
	 * @param row
	 * @param col
	 * @return the value of the Matrix at the given row and col
	 */
	public int get(int row, int col)
	{
		return matrix[row][col];
	}
	
	/**
	 * Sets the given row and col to val
	 * 
	 * @param row
	 * @param col
	 * @param val
	 */
	public void set(int row, int col, int val)
	{
		matrix[row][col] = val;
	}
	
	/**
	 * Adds this and the other Matrix
	 * 
	 * @param other
	 * @return a new Matrix that is the sum of this and the other Matrix
	 */
	public Matrix add(Matrix other)
	{
		if (rows != other.getNumRows() || cols != other.getNumColumns())
			return null;
		
		Matrix ret = new Matrix(this.rows, this.cols);
		for (int row = 0; row < this.rows; row++)
		{
			for (int col = 0; col < this.cols; col++)
			{
				ret.set(row, col, get(row, col) + other.get(row, col));
			}
		}
		
		return ret;
	}
	
	/**
	 * Multiplies the Matrix by a scalar value
	 * 
	 * @param val
	 * @return A new Matrix which was this multiplied by the scalar val
	 */
	public Matrix scalarMultiply(int val)
	{
		Matrix ret = new Matrix(rows, cols);
		for (int row = 0; row < this.rows; row++)
		{
			for (int col = 0; col < this.cols; col++)
			{
				ret.set(row, col, get(row, col) * val);
			}
		}
		return ret;		
	}
	
	/**
	 * Sums the Matrix objects in the list
	 * 
	 * @param list
	 * @return The sum of all the Matrix objects in the list
	 */
	public static Matrix sum(ArrayList<Matrix> list)
	{
		Matrix sum = list.get(0);
		
		for (int i = 1; i < list.size(); i++)
		{
			sum = sum.add(list.get(i));
		}
		
		return sum;
	}
	
	/**
	 * Gets the number of rows
	 * @return Number of rows
	 */
	private int getNumRows()
	{
		return rows;
	}
	
	/**
	 * Gets the number of columns
	 * @return number of columns
	 */
	private int getNumColumns()
	{
		return cols;
	}
}
