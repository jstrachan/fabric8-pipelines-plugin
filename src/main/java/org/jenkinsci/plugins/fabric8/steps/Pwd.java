/**
 * Copyright (C) Original Authors 2017
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jenkinsci.plugins.fabric8.steps;

import io.jenkins.functions.Argument;
import io.jenkins.functions.Step;
import org.jenkinsci.plugins.fabric8.CommandSupport;
import org.jenkinsci.plugins.fabric8.FailedBuildException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.function.Function;

/**
 * Helper function to return the current working directory or create a temporary new dir
 */
@Step(displayName = "Evaluates the current working directory")
public class Pwd extends CommandSupport implements Function<Pwd.Arguments, File> {
    public Pwd() {
    }

    public Pwd(CommandSupport parentStep) {
        super(parentStep);
    }

    public File apply() {
        return apply(false);
    }

    public File apply(boolean tmp) {
        return apply(new Arguments(tmp));
    }

    @Override
    @Step
    public File apply(Arguments arguments) {
        if (arguments.isTmp()) {
            try {
                return tempDir();
            } catch (IOException e) {
                throw new FailedBuildException("Failed to create temporary dir " + e, e);
            }
        }
        return getCurrentDir();
    }

    private File tempDir() throws IOException {
        // TODO use the workspace class name?
        // ws.sibling(ws.getName() + System.getProperty(WorkspaceList.class.getName(), "@") + "tmp");
        File tempFile = File.createTempFile(getCurrentDir().getName(), ".tmp");
        tempFile.mkdirs();
        return tempFile;

    }

    public static class Arguments implements Serializable {
        private static final long serialVersionUID = 1L;

        @Argument
        private boolean tmp = false;

        public Arguments() {
        }

        public Arguments(boolean tmp) {
            this.tmp = tmp;
        }

        public boolean isTmp() {
            return tmp;
        }

        public void setTmp(boolean tmp) {
            this.tmp = tmp;
        }
    }
}
