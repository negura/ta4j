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
package org.ta4j.core.indicators;

import static org.ta4j.core.TestUtils.assertNumEquals;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeriesBuilder;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;

public class MACDIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

    public MACDIndicatorTest(NumFactory numFactory) {
        super(numFactory);
    }

    private BarSeries data;

    @Before
    public void setUp() {
        data = new MockBarSeriesBuilder().withNumFactory(numFactory)
                .withData(37.08, 36.7, 36.11, 35.85, 35.71, 36.04, 36.41, 37.67, 38.01, 37.79, 36.83, 37.10, 38.01,
                        38.50, 38.99)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsErrorOnIllegalArguments() {
        new MACDIndicator(new ClosePriceIndicator(data), 10, 5);
    }

    @Test
    public void macdUsingPeriod5And10() {
        var macdIndicator = new MACDIndicator(new ClosePriceIndicator(data), 5, 10);
        assertNumEquals(0.0, macdIndicator.getValue(0));
        assertNumEquals(-0.05757, macdIndicator.getValue(1));
        assertNumEquals(-0.17488, macdIndicator.getValue(2));
        assertNumEquals(-0.26766, macdIndicator.getValue(3));
        assertNumEquals(-0.32326, macdIndicator.getValue(4));
        assertNumEquals(-0.28399, macdIndicator.getValue(5));
        assertNumEquals(-0.18930, macdIndicator.getValue(6));
        assertNumEquals(0.06472, macdIndicator.getValue(7));
        assertNumEquals(0.25087, macdIndicator.getValue(8));
        assertNumEquals(0.30387, macdIndicator.getValue(9));
        assertNumEquals(0.16891, macdIndicator.getValue(10));

        assertNumEquals(36.4098, macdIndicator.getLongTermEma().getValue(5));
        assertNumEquals(36.1258, macdIndicator.getShortTermEma().getValue(5));

        assertNumEquals(37.0118, macdIndicator.getLongTermEma().getValue(10));
        assertNumEquals(37.1807, macdIndicator.getShortTermEma().getValue(10));
    }
}
