package com.teamunpro.elasticsearch.plugin.analysis.codeanalysis;

/*
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

import com.teamunpro.elasticsearch.indicies.analysis.CodeAnalysisModule;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.AnalysisModule;
import com.teamunpro.elasticsearch.index.analysis.CodeAnalysisBinderProcessor;
import org.elasticsearch.plugins.Plugin;

import java.util.Collection;
import java.util.Collections;

public class CodeAnalysisPlugin extends Plugin {
    @Override
    public String name() {
        return "code-analysis";
    }

    @Override
    public String description() {
        return "Tools for code analysis";
    }

    @Override
    public Collection<Module> nodeModules() {
        return Collections.<Module>singletonList(new CodeAnalysisModule());
    }

    public void onModule(AnalysisModule module) {
        module.addProcessor(new CodeAnalysisBinderProcessor());
    }
}
