/*
 * Copyright (c) 2018 m2049r
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.m2049r.xmrwallet.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class RestoreHeight {
    // XMC parameters
    static final int DIFFICULTY_TARGET = 120; // seconds per block
    static final long XMC_GENESIS_TIMESTAMP = 1734424522L; // 2024-12-17 08:35:22 UTC (seconds)
    
    static private RestoreHeight Singleton = null;

    static public RestoreHeight getInstance() {
        if (Singleton == null) {
            synchronized (RestoreHeight.class) {
                if (Singleton == null) {
                    Singleton = new RestoreHeight();
                }
            }
        }
        return Singleton;
    }

    RestoreHeight() {
        // No predefined checkpoints needed for XMC
    }

    public long getHeight(String date) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        parser.setTimeZone(TimeZone.getTimeZone("UTC"));
        parser.setLenient(false);
        try {
            return getHeight(parser.parse(date));
        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public long getHeight(final Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.DST_OFFSET, 0);
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -4); // give it some leeway

        // Check if date is before XMC genesis
        if (cal.get(Calendar.YEAR) < 2024)
            return 0;
        if ((cal.get(Calendar.YEAR) == 2024) && (cal.get(Calendar.MONTH) < 10)) // November is month 10 (0-based)
            return 0;
        if ((cal.get(Calendar.YEAR) == 2024) && (cal.get(Calendar.MONTH) == 10) && (cal.get(Calendar.DAY_OF_MONTH) < 30))
            return 0;

        long targetTimestamp = cal.getTimeInMillis() / 1000; // Convert to seconds
        
        // Calculate estimated height based on time difference
        long timeDiff = targetTimestamp - XMC_GENESIS_TIMESTAMP;
        long estimatedHeight = timeDiff / DIFFICULTY_TARGET;

        // Return estimated height with some safety buffer (subtract ~30 day worth of blocks)
        return Math.max(0, estimatedHeight - 21600); // 21600 blocks ≈ 30 day at 120s per block
    }
}
