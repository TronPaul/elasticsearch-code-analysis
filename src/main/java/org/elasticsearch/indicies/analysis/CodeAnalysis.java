package org.elasticsearch.indicies.analysis;

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

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.CaseGramTokenFilter;
import org.elasticsearch.index.analysis.PreBuiltTokenFilterFactoryFactory;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;

public class CodeAnalysis extends AbstractComponent {
    @Inject
    public CodeAnalysis(Settings settings, IndicesAnalysisService indicesAnalysisService) {
        super(settings);
        indicesAnalysisService.tokenFilterFactories().put("case_gram_token_filter",
                new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
                    @Override
                    public String name() {
                        return "case_gram_token_filter";
                    }

                    @Override
                    public TokenStream create(TokenStream tokenStream) {
                        return new CaseGramTokenFilter(tokenStream);
                    }
                }));
    }
}
