/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qartf.popularmovies

import com.qartf.popularmovies.database.MovieItem
import com.qartf.popularmovies.domain.Movie
import com.qartf.popularmovies.utility.MovieFactory
import com.qartf.popularmovies.utility.asDatabaseModel
import com.qartf.popularmovies.utility.convertFromString
import com.qartf.popularmovies.utility.convertToString
import org.hamcrest.Matchers
import org.junit.Assert.assertThat
import org.junit.Test

class ConvertersTest {

    private val movieFactory = MovieFactory()
    private val movieA = movieFactory.createMovie()
    private val movieB = movieFactory.createMovie().asDatabaseModel()

    @Test
    fun moshiGenericConverterTestA() {
        val testObjectA = convertFromString<Movie>(convertToString(movieA))
        assertThat(testObjectA, Matchers.equalTo(movieA))
    }

    @Test
    fun moshiGenericConverterTestB() {
        val testObjectB = convertFromString<MovieItem>(convertToString(movieB))
        assertThat(testObjectB, Matchers.equalTo(movieB))
    }
}