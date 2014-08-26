package net.agmodel.metbroker_common.physical;

import java.io.IOException;
import java.io.StringWriter;

import javax.measure.unit.BaseUnit;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;

import org.jscience.physics.amount.Amount;

/**
 * Length class 
 * Amount class is finalized in JScience so it can not be extended.
 * Langth class should be implemented using Amount class.
 * Please download JScience pakage.
 * 
 * @author honda
 * @author kiura
 * 
 */
public class Length extends AbstractQuantity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Phisical Amount
	 */
	private Amount<javax.measure.quantity.Length> m;

	/**
	 * Constractor 
	 * uses Unit class defind in JScience
	 * 
	 * @param v value
	 * @param u unit
	 */
	public Length(double v, Unit<javax.measure.quantity.Length> u) {
		m = Amount.valueOf(v, u);
	}

	/**
	 * Constractor 
	 * default unit is METER.
	 * 
	 * @param v value
	 */
	public Length(double v) {
		m = Amount.valueOf(v, SI.METER);
	}

	/**
	 * 
	 * Constractor
	 * uses JScience Amount class.
	 * 
	 * @param m Amoount
	 */
	public Length(Amount<javax.measure.quantity.Length> m) {
		this.m = m;
	}

	/**
	 * returns value in specified unit
	 * 
	 * @param u unit
	 * @return value in specified unit
	 */
	public double getValue(Unit<javax.measure.quantity.Length> u) {
		return m.doubleValue(u);
	}

	/**
	 * Set length in specified unit.
	 * 
	 * @param v value
	 * @param u unit
	 */
	public void setValue(double v, Unit<javax.measure.quantity.Length> u) {
		m = Amount.valueOf(v, u);
	}

	/**
	 * Add specified Length and returns new result Length object.
	 * 
	 * @param x Length
	 * @return Length
	 */
	public Length add(Length x) {
		return new Length(this.m.plus(x.m));
	}

	/**
	 * 
	 * Returns substracted Length
	 * 
	 * @param x Length substract lenght
	 * @return substracted Length 
	 */
	public Length substract(Length x) {
		return new Length(this.m.minus(x.m));
	}

	/**
	 * return multiplied Length by scalor value
	 * 
	 * @param coefficient scalor
	 * @return Lenght new Result Lenght Object
	 */
	public Length multiply(double coefficient) {
		return new Length(this.m.times(coefficient));
	}

	/**
	 * To check Lenght x Length is not Area.
	 * This methoed should be removed after Area class is defined
	 * 
	 * @param Length 
	 * @return Amount (is Area?)
	 */
	public Amount<javax.measure.quantity.Area> multiply(Length x) {
		double d1 = this.m.doubleValue(SI.METER);
		double d2 = x.getValue(SI.METER);
		Amount<javax.measure.quantity.Area> a = Amount.valueOf(d1 * d2,
				SI.SQUARE_METRE);
		return a;
	}

	/*
	 * Shall be enabled after Area class is defined.
	 * public Area multiply( Length x ) { }
	 */

	/*
	 * Shall be enabled after Area class is defined.
	 * public Volume multiply(Area x) { return new
	 * Volume(this.inMeter * x.getValue(Area.SQUARE_METER), Volume.CUBIC_METER);
	 * }
	 **/

	/**
	 * Length object devided by scalor 
	 * @param coefficient scalor
	 * @return new resulting Length
	 */
	public Length divide(double coefficient) {
		return new Length(this.m.divide(coefficient));
	}

	/**
	 * Returns radius Length 
	 * @param x X Length  
	 * @param y Y Length
	 * @return Lenght from orgiine
	 */
	public static Length magnitude(Length x, Length y) {
		double d1 = y.getValue(SI.METER);
		double d2 = x.getValue(SI.METER);

		return new Length(Math.sqrt(d1 * d1 + d2 * d2), SI.METER);
	}

	/*
	 * Enabled after Speed class defined
	 * public Speed divide(Duration d) { return new Speed(this.inMeter /
	 * d.getValue(DurationUnit.SECOND), SpeedUnit.METERS_PER_SECOND); }
	 */
	/**
	 * Compaire to other Length
	 * @param x Length
	 * @return result -: smaller than x , 0: same Length, +: longer than x
	 */
	public int compareTo(Length x) {
		return this.m.compareTo(x.m);
	}

	/**
	 * compare to other object
	 * @param o object
	 * @return result
	 */
	public int compareTo(Object o) {
		return this.compareTo((Length) o);
	}

	/** 
	 * Returns value in METER 
	 * @see net.agmodel.physical.AbstractQuantity#getValueSI()
	 */
	protected double getValueSI() {
		return this.getValue(SI.METRE);
	}

	/**
	 * Gets the SI dimension of this Length.
	 * 
	 * @return LENGTH
	 */
	protected int getDimensionSI() {
		return LENGTH;
	}

	/** 
	 * Use toString methods provided by JScience Package.
	 * @see net.agmodel.physical.AbstractQuantity#toString()
	 */
	public String toString() {
		return this.m.toString();
	}
	
	/** 
	 * Return SI Unit Symbol using JScience.
	 * @see net.agmodel.physical.AbstractQuantity#getSymbolSI()
	 */
	public String getSymbolSI() {
		try {
			StringWriter a = new StringWriter();
			UnitFormat.getInstance().format(SI.METRE, a);
			return a.toString();
		} catch (IOException e) {
			// ignore
			return e.getMessage();
		}
	}

	
	/**
	 * Return current unit symbol
	 * @return unit symbol
	 * @throws IOException
	 */
	public String getSymbol() throws IOException {
		StringWriter a = new StringWriter();
		Unit<javax.measure.quantity.Length> u = this.m.getUnit();
		UnitFormat.getInstance().format(u, a);
		return a.toString();
	}
}
