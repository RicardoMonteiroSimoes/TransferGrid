/**
 * MIT License
 * <p>
 * Copyright (c) 2017 Ricardo Daniel Monteiro Simoes
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */
package ch.rs.reflectorgrid.util;

import javafx.beans.NamedArg;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;
import javafx.util.converter.BigDecimalStringConverter;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This Class is used to create a generic SpinnerValueFactor for usage with {@link javafx.scene.control.Spinner}.
 * It converts every Number internally into a BigDecimal using the conversion trough .doubleValue().
 * It is not built with the intention of keeping as much precision as possible - it is merely used to give users a simple
 * way of visualizing any kind of Numeric Variable.
 * @author Ricardo DAniel Monteiro Simoes
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
     * @param type the Type of a variable
     */
    public NumberSpinnerValueFactory(@NamedArg("min") Number min,
            @NamedArg("max") Number max, Type type) {
        this(min, max, min, type);
    }


    public NumberSpinnerValueFactory(@NamedArg("min") Number min,
            @NamedArg("max") Number max,
            @NamedArg("initialValue") Number initialValue,
            Type type) {
        this(min, max, initialValue, 1, type);
    }


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

    /**
     * The following functions are used for cleaner comparison of {@link BigDecimal} variables.
     */

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

    /**
     * Comparisonfunctions terminated
     */

    /**
     * {@inheritDoc}
     */
    public void setAmountToStepBy(BigDecimal amountToStepBy) {
        this.amountToStepBy = amountToStepBy;
    }

    /**
     * {@inheritDoc}
     */
    public BigDecimal getAmountToStepBy() {
        return amountToStepBy;
    }

    /**
     * {@inheritDoc}
     */
    private final void setMin(BigDecimal min) {
        this.min = min;
    }

    /**
     * {@inheritDoc}
     */
    public final BigDecimal getMin() {
        return min;
    }

    /**
     * {@inheritDoc}
     */
    private final void setMax(BigDecimal max) {
        this.max = max;
    }

    /**
     * {@inheritDoc}
     */
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
