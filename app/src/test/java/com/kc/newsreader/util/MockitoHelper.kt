package com.kc.newsreader.util

import org.mockito.ArgumentCaptor

/**
 * Created by changk on 1/20/18.
 */

fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()