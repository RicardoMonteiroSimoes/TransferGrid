/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.rs.reflectorgrid.util;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.beans.NamedArg;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;
import javafx.util.converter.BigDecimalStringConverter;

/**
 *
 * @author Ricardo
 */
public class NumberSpinnerValueFactory extends SpinnerValueFactory<BigDecimal> {

    private Type type = BigDecimal.class;
    private BigDecimal currentNumber;
    private BigDecimal min;
    private BigDecimal max;
    private BigDecimal amountToStepBy;

    /**
     * Constructs a new BigDecimalSpinnerValueFactory that sets the initial
     * value to be equal to the min value, and a default {@code amountToStepBy}
     * of one.
     *
     * @param min The minimum allowed double value for the Spinner.
     * @param max The maximum allowed double value for the Spinner.
     */
    public NumberSpinnerValueFactory(@NamedArg("min") Number min,
            @NamedArg("max") Number max, Type type) {
        this(min, max, min, type);
    }

    /**
     * Constructs a new BigDecimalSpinnerValueFactory with a default
     * {@code amountToStepBy} of one.
     *
     * @param min The minimum allowed double value for the Spinner.
     * @param max The maximum allowed double value for the Spinner.
     * @param initialValue The value of the Spinner when first instantiated,
     * must be within the bounds of the min and max arguments, or else the min
     * value will be used.
     */
    public NumberSpinnerValueFactory(@NamedArg("min") Number min,
            @NamedArg("max") Number max,
            @NamedArg("initialValue") Number initialValue,
            Type type) {
        this(min, max, initialValue, 1, type);
    }

    /**
     * Constructs a new BigDecimalSpinnerValueFactory.
     *
     * @param min The minimum allowed double value for the Spinner.
     * @param max The maximum allowed double value for the Spinner.
     * @param initialValue The value of the Spinner when first instantiated,
     * must be within the bounds of the min and max arguments, or else the min
     * value will be used.
     * @param amountToStepBy The amount to increment or decrement by, per step.
     */
    public NumberSpinnerValueFactory(@NamedArg("min") Number min,
            @NamedArg("max") Number max,
            @NamedArg("initialValue") Number initialValue,
            @NamedArg("amountToStepBy") Number amountToStepBy,
            Type type) {
        this.type = type;
        setMin(turnIntoBD(min));
        setMax(turnIntoBD(max));
        setAmountToStepBy(turnIntoBD(amountToStepBy));
        super.setConverter((StringConverter<BigDecimal>) new BigDecimalStringConverter());

        valueProperty().addListener((o, oldValue, newValue) -> {
            if (isSmallerOrEqualThan(newValue, getMin())) {
                setNumber(getMin());
            } else if (isLargerOrEqualThan(newValue, getMax())) {
                setNumber(getMax());
            }
        });
        setNumber((isLargerOrEqualThan(initialValue, getMin())
                && isSmallerOrEqualThan(initialValue, getMax()))
                ? turnIntoBD(initialValue) : getMin());
    }

    private BigDecimal turnIntoBD(Number number) {
        return BigDecimal.valueOf(number.doubleValue());
    }

    private boolean isSmallerOrEqualThan(BigDecimal isSmaller, BigDecimal than) {
        return (isSmaller.compareTo(than) <= 0);
    }

    private boolean isSmallerOrEqualThan(Object isSmaller, Object than) {
        return (((BigDecimal) isSmaller).compareTo((BigDecimal) than) <= 0);
    }

    private boolean isLargerOrEqualThan(BigDecimal isLarger, BigDecimal than) {
        return (isLarger.compareTo(than) >= 0);
    }

    private boolean isLargerOrEqualThan(Object isLarger, Object than) {
        return (((BigDecimal) isLarger).compareTo((BigDecimal) than) >= 0);
    }

    public void setAmountToStepBy(BigDecimal amountToStepBy) {
        this.amountToStepBy = amountToStepBy;
    }

    public BigDecimal getAmountToStepBy() {
        return amountToStepBy;
    }

    private final void setMin(BigDecimal min) {
        this.min = min;
    }

    public final BigDecimal getMin() {
        return min;
    }

    private final void setMax(BigDecimal max) {
        this.max = max;
    }

    public final BigDecimal getMax() {
        return max;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decrement(int steps) {
        final BigDecimal min = getMin();
        final BigDecimal max = getMax();
        final BigDecimal newIndex
                = (getValue()).subtract(turnIntoBD(steps)
                        .multiply(getAmountToStepBy()));

        setNumber(isLargerOrEqualThan(newIndex, getMin())
                ? newIndex : (isWrapAround()
                        ? wrapValue(newIndex, min, max).add(BigDecimal.ONE) : getMin()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increment(int steps) {
        final BigDecimal min = getMin();
        final BigDecimal max = getMax();
        final BigDecimal currentValue = (BigDecimal) getValue();
        final BigDecimal newIndex
                = ((BigDecimal) getValue()).add(turnIntoBD(steps)
                        .multiply(getAmountToStepBy()));
        setNumber((isSmallerOrEqualThan(newIndex, getMax())
                ? newIndex : (isWrapAround()
                        ? wrapValue(newIndex, min, max).add(BigDecimal.ONE) : getMax())));

    }

    private BigDecimal wrapValue(BigDecimal value, BigDecimal min, BigDecimal max) {
        if (max.doubleValue() == 0) {
            throw new RuntimeException();
        }
        if (value.compareTo(min) < 0) {
            return max;
        } else if (value.compareTo(max) > 0) {
            return min;
        }
        return value;
    }

    private void setNumber(BigDecimal number) {
        if (type == int.class || type == long.class) {
            setValue(number.setScale(0, RoundingMode.DOWN));
            return;
        }
        setValue(number);
    }

}
