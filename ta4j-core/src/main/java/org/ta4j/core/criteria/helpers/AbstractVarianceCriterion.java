/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2025 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.criteria.helpers;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Position;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.criteria.AbstractAnalysisCriterion;
import org.ta4j.core.criteria.NumberOfPositionsCriterion;
import org.ta4j.core.num.Num;

/**
 * Variance criterion.
 *
 * <p>
 * Calculates the variance for a Criterion.
 */
public abstract class AbstractVarianceCriterion extends AbstractAnalysisCriterion {

  /**
   * If true, then the lower the criterion value the better, otherwise the higher
   * the criterion value the better. This property is only used for
   * {@link #betterThan(Num, Num)}.
   */
  private final boolean lessIsBetter;
  private final AnalysisCriterion criterion;
  private final NumberOfPositionsCriterion numberOfPositionsCriterion = new NumberOfPositionsCriterion();

  /**
   * Constructor with {@link #lessIsBetter} = false.
   *
   * @param criterion the criterion from which the "variance" is calculated
   */
  public AbstractVarianceCriterion(AnalysisCriterion criterion) {
    this.criterion = criterion;
    this.lessIsBetter = false;
  }

  /**
   * Constructor.
   *
   * @param criterion    the criterion from which the "variance" is calculated
   * @param lessIsBetter the {@link #lessIsBetter}
   */
  public AbstractVarianceCriterion(AnalysisCriterion criterion, boolean lessIsBetter) {
    this.criterion = criterion;
    this.lessIsBetter = lessIsBetter;
  }

  @Override
  public Num calculate(BarSeries series, Position position) {
    Num criterionValue = criterion.calculate(series, position);
    Num numberOfPositions = numberOfPositionsCriterion.calculate(series, position);

    Num variance = series.numFactory().zero();
    Num average = criterionValue.dividedBy(numberOfPositions);
    Num pow = criterion.calculate(series, position).minus(average).pow(2);
    variance = variance.plus(pow);
    variance = variance.dividedBy(numberOfPositions);
    return variance;
  }

  @Override
  public Num calculate(BarSeries series, TradingRecord tradingRecord) {
    if (tradingRecord.getPositions().isEmpty()) {
      return series.numFactory().zero();
    }
    Num criterionValue = criterion.calculate(series, tradingRecord);
    Num numberOfPositions = numberOfPositionsCriterion.calculate(series, tradingRecord);

    Num variance = series.numFactory().zero();
    Num average = criterionValue.dividedBy(numberOfPositions);

    for (Position position : tradingRecord.getPositions()) {
      Num pow = criterion.calculate(series, position).minus(average).pow(2);
      variance = variance.plus(pow);
    }
    variance = applyVarianceDenominator(variance, numberOfPositions);
    return variance;
  }

  /**
   * If {@link #lessIsBetter} == false, then the lower the criterion value, the
   * better, otherwise the higher the criterion value the better.
   */
  @Override
  public boolean betterThan(Num criterionValue1, Num criterionValue2) {
    return lessIsBetter ? criterionValue1.isLessThan(criterionValue2)
        : criterionValue1.isGreaterThan(criterionValue2);
  }

  protected abstract Num applyVarianceDenominator(Num variance, Num numberOfPositions);
}
