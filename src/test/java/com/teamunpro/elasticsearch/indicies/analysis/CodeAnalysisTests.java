package com.teamunpro.elasticsearch.indicies.analysis;

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

import com.teamunpro.elasticsearch.index.analysis.AlphaNumericTokenizerFactory;
import com.teamunpro.elasticsearch.index.analysis.CaseGramTokenFilterFactory;
import com.teamunpro.elasticsearch.index.analysis.CodeAnalysisBinderProcessor;
import com.teamunpro.elasticsearch.index.analysis.CodeAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.LuceneTestCase;
import org.elasticsearch.Version;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.inject.ModulesBuilder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsModule;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.EnvironmentModule;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.IndexNameModule;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.index.settings.IndexSettingsModule;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
import org.elasticsearch.test.ESTestCase;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.elasticsearch.common.settings.Settings.settingsBuilder;

public class CodeAnalysisTests extends ESTestCase {
    @Test
    public void testCodeAnalysis() {
        AnalysisService analysisService = createAnalysisService();
        TokenFilterFactory tokenFilterFactory = analysisService.tokenFilter("caseGram");
        MatcherAssert.assertThat(tokenFilterFactory, Matchers.instanceOf(CaseGramTokenFilterFactory.class));

        Analyzer analyzer = analysisService.analyzer("code").analyzer();
        MatcherAssert.assertThat(analyzer, Matchers.instanceOf(CodeAnalyzer.class));

        TokenizerFactory tokenizerFactory = analysisService.tokenizer("alphaNumeric");
        MatcherAssert.assertThat(tokenizerFactory, Matchers.instanceOf(AlphaNumericTokenizerFactory.class));
    }

    private AnalysisService createAnalysisService() {
        Settings settings = settingsBuilder()
                .put("path.home", LuceneTestCase.createTempDir())
                .put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT)
                .build();
        Index index = new Index("test");

        Injector parentInjector = new ModulesBuilder().add(new SettingsModule(settings), new EnvironmentModule(new Environment(settings))).createInjector();
        Injector injector = new ModulesBuilder().add(
                new IndexSettingsModule(index, settings),
                new IndexNameModule(index),
                new AnalysisModule(settings, parentInjector.getInstance(IndicesAnalysisService.class)).addProcessor(new CodeAnalysisBinderProcessor()),
                new CodeAnalysisModule())
                .createChildInjector(parentInjector);
        return injector.getInstance(AnalysisService.class);
    }
}