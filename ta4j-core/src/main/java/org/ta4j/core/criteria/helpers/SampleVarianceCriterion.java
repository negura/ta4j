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
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;

/**
 * Sample Variance criterion.
 *
 * <p>
 * Calculates the sample variance for a criterion using (n-1) as denominator.
 * This implementation provides an unbiased estimator of population variance.
 */
public class SampleVarianceCriterion extends AbstractVarianceCriterion {

    public SampleVarianceCriterion(AnalysisCriterion criterion) {
        super(criterion);
    }

    public SampleVarianceCriterion(AnalysisCriterion criterion, boolean lessIsBetter) {
        super(criterion, lessIsBetter);
    }

    /**
     * Returns the denominator for variance calculation (n-1).
     *
     * @param numberOfPositions the number of positions
     * @return the denominator to be used in variance calculation
     */
    @Override
    protected Num applyVarianceDenominator(Num variance, Num numberOfPositions) {
        if (numberOfPositions.isLessThanOrEqual(DecimalNum.valueOf(1))){
            return DecimalNum.valueOf(0);
        }
        return variance.dividedBy(
            numberOfPositions.minus(DecimalNum.valueOf(1))
        );
    }
}
