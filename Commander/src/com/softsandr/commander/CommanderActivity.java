/*******************************************************************************
 * Created by o.drachuk on 09/01/2014. 
 *
 * Copyright Oleksandr Drachuk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.softsandr.commander;

/**
 * This interface declare public features of specific activity for commander
 */
public interface CommanderActivity {
    /**
     * Used for declaration common exit operation
     */
    void exitActivity();

    /**
     * Used when show cancel button on action bar for interactive command termination
     */
    void setCancelBtnEnabled();

    /**
     * Used when needs hide/close keyboard
     */
    void hideSoftKeyboard();

    /**
     * Used when needs show/open keyboard
     */
    void showSoftKeyboard();
}
