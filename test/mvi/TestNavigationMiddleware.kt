/*
  Copyright (c) 2018-present, SurfStudio LLC, Georgiy Kartashov.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.arzonpay.android.ui.test.mvi

import io.reactivex.Observable
import ru.arzonpay.android.ui.mvi.navigation.base.NavigationMiddleware
import ru.arzonpay.android.ui.mvi.navigation.event.NavCommandsEvent

/**
 * Stub implementation of [NavigationMiddleware] for testing.
 */
class TestNavigationMiddleware : NavigationMiddleware {

    override fun transform(eventStream: Observable<NavCommandsEvent>): Observable<out NavCommandsEvent> {
        return skip()
    }
}