/*******************************************************************************
 * Created by o.drachuk on 10/01/2014.
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
package com.drk.terminal.util.exceptions;

import java.io.File;
import java.io.IOException;

/**
 * The custom exception for determining situation when file already exist
 */
public class FileExistsException extends IOException {
    private static final long serialVersionUID = 1673522248510624402L;

    /**
     * Constructor that create an instance with the specified message.
     * @param message The error message
     */
    public FileExistsException(String message) {
        super(message);
    }

    /**
     * Constructor that create an instance with the specified file.
     * @param file The file that exists
     */
    public FileExistsException(File file) {
        super("File " + file + " exists");
    }

}
