package com.teamunpro.elasticsearch.index.analysis;

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

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;

public class CodeAnalyzerProvider extends AbstractIndexAnalyzerProvider<CodeAnalyzer> {
    private final CodeAnalyzer analyzer;

    /**
     * Constructs a new analyzer component, with the index name and its settings and the analyzer name.
     *
     * @param index         The index name
     * @param indexSettings The index settings
     * @param name          The analyzer name
     * @param settings
     */
    @Inject
    public CodeAnalyzerProvider(Index index, Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
        analyzer = new CodeAnalyzer();
        analyzer.setVersion(version);
    }

    @Override
    public CodeAnalyzer get() {
        return this.analyzer;
    }
}
