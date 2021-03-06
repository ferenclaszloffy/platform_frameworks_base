/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.keyguard;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.support.test.filters.SmallTest;
import android.testing.AndroidTestingRunner;

import com.android.systemui.SysuiTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@RunWith(AndroidTestingRunner.class)
@SmallTest
public class WakefulnessLifecycleTest extends SysuiTestCase {

    private WakefulnessLifecycle mWakefulness;
    private WakefulnessLifecycle.Observer mWakefulnessObserver;

    @Before
    public void setUp() throws Exception {
        mWakefulness = new WakefulnessLifecycle();
        mWakefulnessObserver = mock(WakefulnessLifecycle.Observer.class);
        mWakefulness.addObserver(mWakefulnessObserver);
    }

    @Test
    public void baseState() throws Exception {
        assertEquals(WakefulnessLifecycle.WAKEFULNESS_ASLEEP, mWakefulness.getWakefulness());

        verifyNoMoreInteractions(mWakefulnessObserver);
    }

    @Test
    public void dispatchStartedWakingUp() throws Exception {
        mWakefulness.dispatchStartedWakingUp();

        assertEquals(WakefulnessLifecycle.WAKEFULNESS_WAKING, mWakefulness.getWakefulness());

        verify(mWakefulnessObserver).onStartedWakingUp();
    }

    @Test
    public void dispatchFinishedWakingUp() throws Exception {
        mWakefulness.dispatchStartedWakingUp();
        mWakefulness.dispatchFinishedWakingUp();

        assertEquals(WakefulnessLifecycle.WAKEFULNESS_AWAKE, mWakefulness.getWakefulness());

        verify(mWakefulnessObserver).onFinishedWakingUp();
    }

    @Test
    public void dispatchStartedGoingToSleep() throws Exception {
        mWakefulness.dispatchStartedWakingUp();
        mWakefulness.dispatchFinishedWakingUp();
        mWakefulness.dispatchStartedGoingToSleep();

        assertEquals(WakefulnessLifecycle.WAKEFULNESS_GOING_TO_SLEEP,
                mWakefulness.getWakefulness());

        verify(mWakefulnessObserver).onStartedGoingToSleep();
    }

    @Test
    public void dispatchFinishedGoingToSleep() throws Exception {
        mWakefulness.dispatchStartedWakingUp();
        mWakefulness.dispatchFinishedWakingUp();
        mWakefulness.dispatchStartedGoingToSleep();
        mWakefulness.dispatchFinishedGoingToSleep();

        assertEquals(WakefulnessLifecycle.WAKEFULNESS_ASLEEP,
                mWakefulness.getWakefulness());

        verify(mWakefulnessObserver).onFinishedGoingToSleep();
    }

    @Test
    public void dump() throws Exception {
        mWakefulness.dump(null, new PrintWriter(new ByteArrayOutputStream()), new String[0]);
    }

}